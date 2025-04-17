package com.bookxpert.assignment.ui.main

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.bookxpert.assignment.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ImagePickerBottomSheet constructor(private val context: Context) : BottomSheetDialogFragment() {

    private lateinit var buttonCamera: Button
    private lateinit var buttonGallery: Button
    private lateinit var imageView: ImageView
    private lateinit var dialog_cancel: ImageButton
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonCamera = view.findViewById(R.id.button_camera)
        buttonGallery = view.findViewById(R.id.button_gallery)
        imageView = view.findViewById(R.id.image_view)
        dialog_cancel = view.findViewById(R.id.dialog_cancel)

        buttonCamera.setOnClickListener {
            checkPermissionsAndOpenCamera()
        }

        buttonGallery.setOnClickListener {
            checkPermissionsAndOpenGallery()
        }

        dialog_cancel.setOnClickListener {
            dismiss()
        }

        // Initialize the permission launcher

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val cameraGranted = permissions[Manifest.permission.CAMERA] ?: false
            val storageGranted = permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: false

            if (cameraGranted && storageGranted) {
                // Both permissions are granted, proceed with camera or gallery
            } else {
                // Handle the case where permissions are not granted
            }

        }

        // Initialize the ActivityResultLauncher for camera
        cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val bitmap = result.data?.extras?.get("data") as Bitmap
                imageView.setImageBitmap(bitmap) // Display the captured image
                imageView.visibility = View.VISIBLE
            }
        }


        // Initialize the ActivityResultLauncher for gallery
        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val imageUri: Uri? = result.data?.data
                imageView.setImageURI(imageUri) // Display the selected image
                imageView.visibility = View.VISIBLE
            }
        }
    }

    private fun checkPermissionsAndOpenCamera() {
        when {
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED -> {
                // Permissions are granted, open the camera
                openCamera()
            }
            else -> {
                // Request permissions
                permissionLauncher.launch(arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE))
            }
        }
    }


    private fun checkPermissionsAndOpenGallery() {
        when {
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED -> {
                // Permission is granted, open the gallery
                openGallery()
            }
            else -> {
                // Request permission
                permissionLauncher.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
            }

        }

    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(intent) // Launch the camera
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent) // Launch the camera
    }

    companion object {
        val TAG: String? = "ImagePickerBottomSheet"
        const val REQUEST_IMAGE_CAPTURE = 1
        const val REQUEST_IMAGE_PICK = 2
    }
}