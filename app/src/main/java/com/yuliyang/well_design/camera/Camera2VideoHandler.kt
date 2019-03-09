package com.yuliyang.well_design.camera

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.graphics.Rect
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.hardware.camera2.CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM
import android.hardware.camera2.CameraDevice.TEMPLATE_RECORD
import android.media.MediaRecorder
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.view.Surface
import android.view.TextureView
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit


@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class Camera2VideoHandler : BaseCameraVideoHandler() {

    private var backgroundThread: HandlerThread? = null
    private var backgroundHandler: Handler? = null
    private val cameraOpenCloseLock = Semaphore(1)
    private var cameraDevice: CameraDevice? = null
    private lateinit var previewRequestBuilder: CaptureRequest.Builder
    private var captureSession: CameraCaptureSession? = null
    private var currentCalZoom: Float = 1F
    private var maxZoom: Float = 1F
    private lateinit var cameraViewRect: Rect
    private var scaleRight: Int = -1
    private var scaleBottom: Int = -1

    override fun initCamera(mContext: AppCompatActivity, textureView: AutoFitTextureView) {
        super.initCamera(mContext, textureView)
        textureView.scaleListener = object : AutoFitTextureViewScaleListener {
            override fun onScaleChange(ratio: Float) {
                currentCalZoom *= ratio * 0.85F
                if (currentCalZoom.toInt() > maxZoom / 2) {
                    currentCalZoom = maxZoom / 2
                }
                if (currentCalZoom < 1) {
                    currentCalZoom = 1F
                }
                scaleRight = (cameraViewRect.right / currentCalZoom).toInt()
                scaleBottom = scaleRight * cameraViewRect.bottom / cameraViewRect.right
                updatePreview()
            }
        }
    }


    //TextView监听
    private val surfaceTextureListener = object : TextureView.SurfaceTextureListener {

        override fun onSurfaceTextureAvailable(texture: SurfaceTexture, width: Int, height: Int) {
            openCamera()
        }

        override fun onSurfaceTextureSizeChanged(texture: SurfaceTexture, width: Int, height: Int) {
            configureTransform()
        }

        override fun onSurfaceTextureDestroyed(surfaceTexture: SurfaceTexture) = true

        override fun onSurfaceTextureUpdated(surfaceTexture: SurfaceTexture) = Unit

    }

    /**
     * 开始预览
     */
    override fun startCameraPreview() {
        startBackgroundThread()
        if (textureView.isAvailable) {
            openCamera()
        } else {
            textureView.surfaceTextureListener = surfaceTextureListener
        }
    }

    /**
     * 关闭预览，在onStop和onPause的时候需要调用
     */
    override fun stopCameraPreview() {
        closeCamera()
        stopBackgroundThread()
    }

    private fun stopBackgroundThread() {
        backgroundThread?.apply {
            quitSafely()
            try {
                join()
            } catch (e: InterruptedException) {
            }
        }
        backgroundThread = null
        backgroundHandler = null
    }

    private fun closeCamera() {
        try {
            cameraOpenCloseLock.acquire()
            closePreviewSession()
            cameraDevice?.close()
            cameraDevice = null
        } catch (e: InterruptedException) {
            errorCodeLiveData.postValue(CAMERA_ERROR_CODE_DISABLE)
        } finally {
            cameraOpenCloseLock.release()
        }
    }

    private fun startBackgroundThread() {
        backgroundThread = HandlerThread("CameraBackground").apply {
            start()
            backgroundHandler = Handler(looper)
        }
    }

    @SuppressLint("MissingPermission")
    private fun openCamera() {
        if (mContext.get()?.isFinishing != false) return
        val cameraActivity = mContext.get()!!
        val manager = cameraActivity.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            if (!cameraOpenCloseLock.tryAcquire(2000, TimeUnit.MILLISECONDS)) {
                throw RuntimeException("Time out waiting to lock camera opening.")
            }

            val cameraId = manager.cameraIdList[0]
            val characteristics = manager.getCameraCharacteristics(cameraId)
            maxZoom = characteristics.get(SCALER_AVAILABLE_MAX_DIGITAL_ZOOM) ?: 1F
            cameraViewRect = characteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE)

            val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                ?: throw RuntimeException("Cannot get available preview/video sizes")
            sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)

            //视频尺寸
            val videoSizes = arrayListOf<CamereSizeInfo>()
            var cameraSizeInfo: CamereSizeInfo
            for (size in map.getOutputSizes(MediaRecorder::class.java)) {
                //格式化视频尺寸
                cameraSizeInfo = CamereSizeInfo(size.width, size.height)
                videoSizes.add(cameraSizeInfo)
            }
            videoSize = chooseVideoSize(videoSizes)

            //预览尺寸
            val previewSizes = arrayListOf<CamereSizeInfo>()
            for (size in map.getOutputSizes(SurfaceTexture::class.java)) {
                //格式化预览尺寸
                cameraSizeInfo = CamereSizeInfo(size.width, size.height)
                previewSizes.add(cameraSizeInfo)
            }
            previewSize = choosePreviewSize(previewSizes, videoSize)

            setTextureViewRatio()
            manager.openCamera(cameraId, stateCallback, null)
        } catch (e: Exception) {
            errorCodeLiveData.postValue(CAMERA_ERROR_CODE_DISABLE)
        } finally {
            cameraOpenCloseLock.release()
        }
    }

    private val stateCallback = object : CameraDevice.StateCallback() {

        override fun onOpened(cameraDevice: CameraDevice) {
            cameraOpenCloseLock.release()
            this@Camera2VideoHandler.cameraDevice = cameraDevice
            configureTransform()
            startPreview()
        }

        override fun onDisconnected(cameraDevice: CameraDevice) {
            cameraOpenCloseLock.release()
            cameraDevice.close()
            this@Camera2VideoHandler.cameraDevice = null
        }

        override fun onError(cameraDevice: CameraDevice, error: Int) {
            cameraOpenCloseLock.release()
            cameraDevice.close()
            this@Camera2VideoHandler.cameraDevice = null
            errorCodeLiveData.postValue(CAMERA_ERROR_CODE_DISABLE)
        }
    }

    /**
     * 开始预览
     */
    private fun startPreview() {
        if (cameraDevice == null || !textureView.isAvailable) return
        try {
            closePreviewSession()
            val texture = textureView.surfaceTexture
            texture.setDefaultBufferSize(previewSize.width, previewSize.height)
            previewRequestBuilder = cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            previewRequestBuilder.set(
                CaptureRequest.CONTROL_AF_MODE,
                CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_VIDEO
            )

            val previewSurface = Surface(texture)
            previewRequestBuilder.addTarget(previewSurface)

            cameraDevice?.createCaptureSession(
                listOf(previewSurface),
                object : CameraCaptureSession.StateCallback() {

                    override fun onConfigured(session: CameraCaptureSession) {
                        captureSession = session
                        updatePreview()
                    }

                    override fun onConfigureFailed(session: CameraCaptureSession) {
                        errorCodeLiveData.postValue(CAMERA_ERROR_CODE_PREVIEW)
                    }
                }, backgroundHandler
            )
        } catch (e: CameraAccessException) {
            errorCodeLiveData.postValue(CAMERA_ERROR_CODE_DISABLE)
        }
    }

    private fun updatePreview() {
        if (cameraDevice == null) return

        try {
            captureSession?.stopRepeating()
            previewRequestBuilder.set(
                CaptureRequest.SCALER_CROP_REGION,
                Rect(
                    0,
                    0,
                    scaleRight,
                    scaleBottom
                )
            )
            setUpCaptureRequestBuilder(previewRequestBuilder)
            captureSession?.setRepeatingRequest(
                previewRequestBuilder.build(),
                null, backgroundHandler
            )
        } catch (e: CameraAccessException) {
            errorCodeLiveData.postValue(CAMERA_ERROR_CODE_DISABLE)
        }
    }

    private fun setUpCaptureRequestBuilder(builder: CaptureRequest.Builder?) {
        builder?.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
    }


    private fun closePreviewSession() {
        captureSession?.close()
        captureSession = null
    }

    /**
     * 开始录制
     */
    override fun startRecordingVideo(outputPath: String, successCallback: (Boolean) -> Unit) {
        if (isRecordingStatus) {
            return
        }
        if (cameraDevice == null || !textureView.isAvailable) return
        try {
            closePreviewSession()
            //初始化MediaRecorder
            if (!setUpMediaRecorder(outputPath)) {
                return
            }

            val texture = textureView.surfaceTexture.apply {
                setDefaultBufferSize(previewSize.width, previewSize.height)
            }
            val previewSurface = Surface(texture)
            val recorderSurface = mediaRecorder!!.surface

            val surfaces = ArrayList<Surface>().apply {
                add(previewSurface)
                add(recorderSurface)
            }
            previewRequestBuilder = cameraDevice!!.createCaptureRequest(TEMPLATE_RECORD).apply {
                addTarget(previewSurface)
                addTarget(recorderSurface)
            }

            cameraDevice!!.createCaptureSession(
                surfaces,
                object : CameraCaptureSession.StateCallback() {
                    override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                        captureSession = cameraCaptureSession
                        successCallback.invoke(true)
                        updatePreview()
                        mediaRecorder!!.start()
                        isRecordingStatus = true
                    }

                    override fun onConfigureFailed(cameraCaptureSession: CameraCaptureSession) {
                        successCallback.invoke(false)
                    }
                }, backgroundHandler
            )
        } catch (e: Exception) {
            successCallback.invoke(false)
            releaseMediaRecorder()
        }
    }

    /**
     * 初始化MediaReocorder
     */
    private fun setUpMediaRecorder(outputFilePath: String): Boolean {
        val rotation = mContext.get()?.windowManager?.defaultDisplay?.rotation ?: 0
        mediaRecorder = MediaRecorder()
        mediaRecorder!!.apply {
            when (sensorOrientation ?: 90) {
                SENSOR_ORIENTATION_DEFAULT_DEGREES ->
                    setOrientationHint(DEFAULT_ORIENTATIONS.get(rotation))
                SENSOR_ORIENTATION_INVERSE_DEGREES ->
                    setOrientationHint(INVERSE_ORIENTATIONS.get(rotation))
            }
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setVideoSource(MediaRecorder.VideoSource.SURFACE)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setVideoEncodingBitRate(videoBitRate)
            setVideoFrameRate(frameRate)
            setVideoSize(videoSize.width, videoSize.height)
            setVideoEncoder(MediaRecorder.VideoEncoder.H264)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(outputFilePath)
            try {
                prepare()
            } catch (e: Exception) {
                errorCodeLiveData.postValue(CAMERA_ERROR_CODE_RECORD)
                releaseMediaRecorder()
                return false
            }
        }
        return true
    }

    /**
     * 停止录制
     */
    override fun stopRecordingVideo() {
        if (!isRecordingStatus) {
            return
        }
        mediaRecorder?.apply {
            releaseMediaRecorder()
            isRecordingStatus = false
        }
        startPreview()
    }
}