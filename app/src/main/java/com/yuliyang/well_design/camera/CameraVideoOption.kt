package com.yuliyang.well_design.camera

interface CameraVideoOption {
    abstract fun startCameraPreview()

    abstract fun stopCameraPreview()

    abstract fun startRecordingVideo(outputPath: String, successCallback: (Boolean) -> Unit)

    abstract fun stopRecordingVideo()
}