package com.yuliyang.well_design.camera

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.yuliyang.well_design.R
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class CameraActivity : AppCompatActivity() {

    private val cameraVideoHandler = Camera2VideoHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        //全屏
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO), 100)
        }
        cameraVideoHandler.initCamera(this, textureView)

        start.setOnClickListener {
            cameraVideoHandler.startRecordingVideo(getOutputMediaFile()?.absolutePath ?: "") {
                println("record  $it")
            }
        }

        stop.setOnClickListener {
            cameraVideoHandler.stopRecordingVideo()
        }
    }

    override fun onResume() {
        super.onResume()
        cameraVideoHandler.startCameraPreview()
    }

    override fun onPause() {
        super.onPause()
        cameraVideoHandler.stopCameraPreview()
    }

    private fun getOutputMediaFile(): File? {
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
            return null
        }

//        val mediaStorageDir = File(Environment.getExternalStorageDirectory(), "CameraSample")
        val mediaStorageDir = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
            ), "CameraSample"
        )
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("CameraSample", "failed to create directory")
                return null
            }
        }

        // Create a media file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(Date())
        val mediaFile = File(
            mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4"
        )

        return mediaFile
    }
}