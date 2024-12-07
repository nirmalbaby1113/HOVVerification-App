package com.nirmal.baby.hovverification.features.hovVerification.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.nirmal.baby.hovverification.R
import com.nirmal.baby.hovverification.databinding.ActivityMainBinding
import com.nirmal.baby.hovverification.features.hovVerification.interactor.HOVInteractor
import com.nirmal.baby.hovverification.features.hovVerification.presenter.HOVPresenter
import com.nirmal.baby.hovverification.features.hovVerification.router.HOVRouter
import com.nirmal.baby.hovverification.network.ApiService
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class HOVActivity : AppCompatActivity(), HOVViewInterface {

    private lateinit var presenter: HOVPresenter
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageCapture: ImageCapture
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize VIPER components
        val apiService = ApiService.create()
        val interactor = HOVInteractor(apiService)
        val router = HOVRouter(this)
        presenter = HOVPresenter(this, interactor, router)

        // Initialize CameraX
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermission.launch(Manifest.permission.CAMERA)
        } else {
            startCamera()
        }

        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()

        // Button click listener for image capture
        binding.captureButton.setOnClickListener {
            captureImage()
        }
    }

    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            startCamera()
        } else {
            Toast.makeText(this, "Camera permission is required to use this feature", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = androidx.camera.core.Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .build()

            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT) // FRONT camera
                .build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )
            } catch (e: Exception) {
                Log.e("CameraX", "Use case binding failed", e)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun captureImage() {
        val imageFile = File(
            outputDirectory,
            SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US).format(System.currentTimeMillis()) + ".jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(imageFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Toast.makeText(this@HOVActivity, "Image Saved: ${imageFile.absolutePath}", Toast.LENGTH_SHORT).show()
                    processCapturedImage(imageFile) // Pass the image file for further processing
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(this@HOVActivity, "Error capturing image: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun processCapturedImage(imageFile: File) {
        // Pass the captured image to the presenter for API processing
        presenter.onImageCaptured(imageFile)
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    override fun showFaceCount(count: Int) {
        binding.faceCountTextView.text = "Faces detected: $count"
    }

    override fun showLoading() {
        binding.loadingSpinner.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.loadingSpinner.visibility = View.GONE
    }

    override fun showError(message: String) {
        Log.d("HOVActivity", "Check: $message")
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
