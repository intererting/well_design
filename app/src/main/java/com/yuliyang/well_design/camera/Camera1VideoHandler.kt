package com.yuliyang.well_design.camera

import android.hardware.Camera
import android.hardware.Camera.Parameters.*
import android.media.CamcorderProfile
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.max


class Camera1VideoHandler : BaseCameraVideoHandler() {

    private var mCamera: Camera? = null
    private var currentZoom: Float = 1F

    override fun initCamera(mContext: AppCompatActivity, textureView: AutoFitTextureView) {
        super.initCamera(mContext, textureView)
        textureView.scaleListener = object : AutoFitTextureViewScaleListener {
            override fun onScaleChange(ratio: Float) {
                mCamera?.apply {
                    val mParameter = parameters
                    if (mParameter.isZoomSupported) {
                        currentZoom *= ratio * 0.85F
                        if (mParameter.zoom == currentZoom.toInt()) {
                            return
                        }
                        if (currentZoom.toInt() > mParameter.maxZoom / 2) {
                            currentZoom = mParameter.maxZoom / 2.toFloat()
                        }
                        if (currentZoom < 1) {
                            currentZoom = 1f
                        }
                        mParameter.zoom = currentZoom.toInt()
                        parameters = mParameter
                    }
                }
            }
        }
    }

    /**
     * 开始预览
     */
    override fun startCameraPreview() {
        mCamera = Camera.open()
        if (mCamera == null) {
            errorCodeLiveData.postValue(CAMERA_ERROR_CODE_DISABLE)
            return
        }
        mCamera!!.apply {
            val mParameters = parameters
            //视频尺寸
            val videoSizes = arrayListOf<CamereSizeInfo>()
            var cameraSizeInfo: CamereSizeInfo
            for (size in mParameters.supportedPreviewSizes) {
                //格式化视频尺寸
                cameraSizeInfo = CamereSizeInfo(size.width, size.height)
                videoSizes.add(cameraSizeInfo)
            }

            videoSize = chooseVideoSize(videoSizes)
            previewSize = choosePreviewSize(videoSizes, videoSize)
            mParameters.setPreviewSize(previewSize.width, previewSize.height)
            mParameters.focusMode = FOCUS_MODE_CONTINUOUS_VIDEO
            parameters = mParameters
            try {
                setPreviewTexture(textureView.surfaceTexture)
                setDisplayOrientation(90)
                setTextureViewRatio()
                configureTransform()
                startPreview()
            } catch (e: Exception) {
                errorCodeLiveData.postValue(CAMERA_ERROR_CODE_PREVIEW)
            }
        }
    }

    override fun startRecordingVideo(outputPath: String, successCallback: (Boolean) -> Unit) {
        if (isRecordingStatus) {
            return
        }
        mCamera?.apply {
            if (setUpMediaRecorder(outputPath, mCamera!!)) {
                //初始化成功
                try {
                    mediaRecorder!!.start()
                    isRecordingStatus = true
                    successCallback.invoke(true)
                } catch (e: Exception) {
                    successCallback.invoke(false)
                    releaseMediaRecorder()
                }
            }
        }
    }

    /**
     * 初始化MediaReocorder
     */
    private fun setUpMediaRecorder(outputFilePath: String, mCamera: Camera): Boolean {
        val rotation = mContext.get()?.windowManager?.defaultDisplay?.rotation ?: 0
        mediaRecorder = MediaRecorder()
        mediaRecorder!!.apply {
            when (sensorOrientation ?: 90) {
                SENSOR_ORIENTATION_DEFAULT_DEGREES ->
                    setOrientationHint(DEFAULT_ORIENTATIONS.get(rotation))
                SENSOR_ORIENTATION_INVERSE_DEGREES ->
                    setOrientationHint(INVERSE_ORIENTATIONS.get(rotation))
            }
            mCamera.unlock()
            setCamera(mCamera)
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setVideoSource(MediaRecorder.VideoSource.CAMERA)
            setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH))
            setVideoFrameRate(frameRate)
            setVideoEncodingBitRate(videoBitRate)
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

    override fun stopRecordingVideo() {
        if (!isRecordingStatus) {
            return
        }
        mediaRecorder?.apply {
            releaseMediaRecorder()
            isRecordingStatus = false
        }
    }

    private fun releaseCamera() {
        mCamera?.release()
        mCamera = null
    }

    /**
     * 关闭预览
     */
    override fun stopCameraPreview() {
        releaseMediaRecorder()
        releaseCamera()
    }

}