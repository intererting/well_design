package com.yuliyang.well_design.camera

import android.content.res.Configuration
import android.graphics.Matrix
import android.graphics.RectF
import android.media.MediaRecorder
import android.util.SparseIntArray
import android.view.Surface
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import org.jetbrains.anko.configuration
import java.lang.ref.WeakReference
import java.util.*

const val CAMERA_ERROR_CODE_DISABLE = 0
const val CAMERA_ERROR_CODE_PREVIEW = 1
const val CAMERA_ERROR_CODE_RECORD = 2

val cameraErrorCodeMsgMap = hashMapOf(
    CAMERA_ERROR_CODE_DISABLE to "摄像头暂不可用，打开失败",
    CAMERA_ERROR_CODE_PREVIEW to "摄像头预览失败",
    CAMERA_ERROR_CODE_RECORD to "录制出现异常"
)


abstract class BaseCameraVideoHandler : CameraVideoOption {

    protected lateinit var mContext: WeakReference<AppCompatActivity>
    protected lateinit var textureView: AutoFitTextureView
    protected var mediaRecorder: MediaRecorder? = null
    val errorCodeLiveData = MutableLiveData<Int>()
    protected lateinit var previewSize: CamereSizeInfo
    protected lateinit var videoSize: CamereSizeInfo
    protected var sensorOrientation: Int? = 90
    protected val videoBitRate = 8_100_100
    protected val frameRate = 25
    protected var isRecordingStatus = false

    protected val SENSOR_ORIENTATION_DEFAULT_DEGREES = 90
    protected val SENSOR_ORIENTATION_INVERSE_DEGREES = 270
    protected val DEFAULT_ORIENTATIONS = SparseIntArray().apply {
        append(Surface.ROTATION_0, 90)
        append(Surface.ROTATION_90, 0)
        append(Surface.ROTATION_180, 270)
        append(Surface.ROTATION_270, 180)
    }
    protected val INVERSE_ORIENTATIONS = SparseIntArray().apply {
        append(Surface.ROTATION_0, 270)
        append(Surface.ROTATION_90, 180)
        append(Surface.ROTATION_180, 90)
        append(Surface.ROTATION_270, 0)
    }

    fun isRecording() = isRecordingStatus

    /**
     * 初始化摄像头
     */
    open fun initCamera(mContext: AppCompatActivity, textureView: AutoFitTextureView) {
        this.mContext = WeakReference(mContext)
        this.textureView = textureView
    }

    /**
     * 设置显示方向
     */
    protected fun configureTransform() {
        mContext.get() ?: return
        val cameraActivity = mContext.get()!!
        val rotation = cameraActivity.windowManager.defaultDisplay.rotation
        val matrix = Matrix()
        val viewRect = RectF(0f, 0f, textureView.width.toFloat(), textureView.height.toFloat())
        val bufferRect = RectF(0f, 0f, previewSize.height.toFloat(), previewSize.width.toFloat())
        val centerX = viewRect.centerX()
        val centerY = viewRect.centerY()

        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY())
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL)
            val scale = Math.max(
                textureView.height.toFloat() / previewSize.height,
                textureView.width.toFloat() / previewSize.width
            )
            with(matrix) {
                postScale(scale, scale, centerX, centerY)
                postRotate((90 * (rotation - 2)).toFloat(), centerX, centerY)
            }
        }
        textureView.setTransform(matrix)
    }

    protected fun setTextureViewRatio() {
        mContext.get() ?: return
        val cameraActivity = mContext.get()!!
        if (cameraActivity.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            textureView.setAspectRatio(previewSize.width, previewSize.height)
        } else {
            textureView.setAspectRatio(previewSize.height, previewSize.width)
        }
    }

    protected fun releaseMediaRecorder() {
        mediaRecorder?.apply {
            if (isRecordingStatus) {
                stop()
            }
            reset()
            release()
        }
        mediaRecorder = null
    }

    /**
     * 选择视频尺寸，优先满足屏幕比例
     */
    protected fun chooseVideoSize(choices: List<CamereSizeInfo>) = choices.firstOrNull {
        it.width == it.height * 2 / 1
    } ?: choices[choices.size - 1]

    /**
     * 选择预览尺寸，优先满足屏幕比例
     */
    protected fun choosePreviewSize(choices: List<CamereSizeInfo>, aspectRatio: CamereSizeInfo): CamereSizeInfo {
        val w = aspectRatio.width
        val h = aspectRatio.height
        val bigEnough = choices.filter {
            it.height == it.width * h / w
        }
        return if (bigEnough.isNotEmpty()) {
            Collections.max(bigEnough, CompareSizesByArea())
        } else {
            choices[0]
        }
    }
}

//支持尺寸信息，为了适配
data class CamereSizeInfo(val width: Int, val height: Int)
