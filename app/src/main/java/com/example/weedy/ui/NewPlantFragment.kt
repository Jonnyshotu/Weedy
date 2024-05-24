package com.example.weedy.ui

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.weedy.R
import com.example.weedy.databinding.FragmentNewPlantBinding

class NewPlantFragment : MainFragment() {

    private lateinit var binding: FragmentNewPlantBinding
    private lateinit var plantImageIV: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewPlantBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        plantImageIV = binding.newPlantPlantIV
        binding.newPlantPhotoBTN.setOnClickListener {
            showPhotoOptionMenu(it)
        }
    }

    override fun onImageCaptured(imageBitmap: Bitmap) {
        plantImageIV.setImageBitmap(imageBitmap)
    }

    override fun onImagePicked(imageUri: Uri?) {
        plantImageIV.setImageURI(imageUri)
    }
}