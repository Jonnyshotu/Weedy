package com.example.weedy.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.example.weedy.R
import com.example.weedy.SharedViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.time.LocalDate

abstract class MainFragment : Fragment() {

    private val viewModel: SharedViewModel by activityViewModels()

    //region photo request

    val REQUEST_CODE_PERMISSIONS = 1001
    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_IMAGE_PICK = 2
    val REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    open fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // All permissions are granted
            } else {
                Toast.makeText(context, "Permissions not granted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    protected fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    protected fun dispatchPickPictureIntent() {
        val pickPhotoIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    data?.extras?.get("data")?.let {
                        if (it is Bitmap) {
                            onImageCaptured(it)
                        }
                    }
                }

                REQUEST_IMAGE_PICK -> {
                    data?.data?.let { selectedImageUri ->
                        onImagePicked(selectedImageUri)
                    }
                }
            }
        }
    }

    abstract fun onImageCaptured(imageBitmap: Bitmap)
    abstract fun onImagePicked(imageUri: Uri?)
//endregion

    /**
     * displays the treatment menu and handles the selected item.
     *
     * @param v View
     */
    open fun showTreatmentMenu(v: View) {

        val popup = PopupMenu(requireContext(), v)
        popup.menuInflater.inflate(R.menu.treatment_menu, popup.menu)

        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.photo_option -> {
                    showPhotoOptionMenu(v)
                    true
                }
                // Dialog Water
                R.id.watering_option -> {
                    val dialogView =
                        LayoutInflater.from(v.context).inflate(R.layout.water_dialog, null, false)
                    val input = dialogView.findViewById<EditText>(R.id.waterDialogET)

                    MaterialAlertDialogBuilder(v.context).setTitle(resources.getString(R.string.watering))
                        .setView(dialogView).setNegativeButton("Cancel") { dialog, which ->
                        }.setPositiveButton("Save") { dialog, which ->
                            viewModel.water(
                                Pair(
                                    input.text.toString().toDoubleOrNull(), LocalDate.now()
                                )
                            )
                        }.show()
                    true
                }
// Dialog Nutrients
                R.id.nutrients_option -> {
                    val dialogView = LayoutInflater.from(v.context)
                        .inflate(R.layout.nutrients_dialog, null, false)
                    val spinner = dialogView.findViewById<Spinner>(R.id.spinner_nutrients)
                    val input = dialogView.findViewById<EditText>(R.id.nutrientsDialogET)

                    val items = viewModel.nutrients.value?.map { it.name } ?: listOf()
                    val adapter = ArrayAdapter(
                        v.context, android.R.layout.simple_spinner_dropdown_item, items
                    )
                    spinner.adapter = adapter

                    MaterialAlertDialogBuilder(v.context).setTitle("Nutrients").setView(dialogView)
                        .setNegativeButton("Cancel") { dialog, which ->
                        }.setPositiveButton("Save") { dialog, which ->
                            val selectedItem = spinner.selectedItem.toString()
                            val amount = input.text.toString().toDoubleOrNull()
                            if (amount != null) {
                                viewModel.nutrients(Triple(selectedItem, amount, LocalDate.now()))
                            }
                        }.show()
                    true
                }
// Dialog Repellents
                R.id.repellents_option -> {
                    val dialogView = LayoutInflater.from(v.context)
                        .inflate(R.layout.repellents_dialog, null, false)
                    val input = dialogView.findViewById<EditText>(R.id.repellentsDialogET)

                    MaterialAlertDialogBuilder(v.context).setTitle("Repellents").setView(dialogView)
                        .setNegativeButton("Cancel") { dialogInterface, which ->
                        }.setPositiveButton("Save") { dialogInterface, which ->
                            viewModel.reppelents(
                                Pair(
                                    input.text.toString(), LocalDate.now()
                                )
                            )
                        }.show()
                    true
                }
// Dialog Growth State
                R.id.growth_state_change_option -> {
                    val dialog = LayoutInflater.from(v.context)
                        .inflate(R.layout.growth_state_dialog, null, false)
                    val radioGroup = dialog.findViewById<RadioGroup>(R.id.growthStateRG)

                    MaterialAlertDialogBuilder(v.context).setTitle("Growth State").setView(dialog)
                        .setNegativeButton("Cancel") { dialogInterface, which ->
                        }.setPositiveButton("Save") { dialogInterface, which ->
                            val selectedId = radioGroup.checkedRadioButtonId
                            val growthState = when (selectedId) {
                                R.id.seedling_radio -> 1
                                R.id.cutting_radio -> 2
                                R.id.adult_radio -> 3
                                R.id.flower_radio -> 4
                                else -> 0
                            }
                            viewModel.growthStateChange(Pair(growthState, LocalDate.now()))
                        }.show()
                    true
                }
// Dialog Repot
                R.id.repot_option -> {
                    val dialogView =
                        LayoutInflater.from(v.context).inflate(R.layout.repot_dialog, null, false)
                    val spinner = dialogView.findViewById<Spinner>(R.id.spinner_pot_size)
                    val input = dialogView.findViewById<EditText>(R.id.repotDialogET)

                    val items = viewModel.soilTypes.value?.map { it.name } ?: listOf()
                    val adapter = ArrayAdapter(
                        v.context, android.R.layout.simple_spinner_dropdown_item, items
                    )
                    spinner.adapter = adapter

                    MaterialAlertDialogBuilder(v.context).setTitle("Repot").setView(dialogView)
                        .setNegativeButton("Cancel") { dialog, which ->
                        }.setPositiveButton("Save") { dialog, which ->
                            val selectedItem = spinner.selectedItem.toString()
                            val amount = input.text.toString().toIntOrNull()
                            if (amount != null) {
                                viewModel.repot(Triple(selectedItem, amount, LocalDate.now()))
                            }
                        }.show()
                    true
                }
// Dialog Trainng
                R.id.training_option -> {
                    val dialogView = LayoutInflater.from(v.context)
                        .inflate(R.layout.training_dialog, null, false)
                    val input = dialogView.findViewById<EditText>(R.id.trainingDialogET)

                    MaterialAlertDialogBuilder(v.context).setTitle("Training").setView(dialogView)
                        .setNegativeButton("Cancel") { dialogInterface, which ->
                        }.setPositiveButton("Save") { dialogInterface, which ->
                            viewModel.training(
                                Pair(
                                    input.text.toString(), LocalDate.now()
                                )
                            )
                        }.show()
                    true
                }
// Dialog Light Cycle
                R.id.light_option -> {
                    val dialogView =
                        LayoutInflater.from(v.context).inflate(R.layout.light_dialog, null, false)
                    val seekBar = dialogView.findViewById<SeekBar>(R.id.lightDialogSB)

                    seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                        override fun onProgressChanged(
                            seekBar: SeekBar, progress: Int, fromUser: Boolean
                        ) {

                            val currentValue = progress
                            val lightHoursTV =
                                dialogView.findViewById<TextView>(R.id.lightDialogHoursTV)
                            val nighthours = 24 - currentValue

                            lightHoursTV.text = "$currentValue / $nighthours cycle"
                        }

                        override fun onStartTrackingTouch(seekBar: SeekBar?) {
                        }

                        override fun onStopTrackingTouch(seekBar: SeekBar?) {
                        }
                    })

                    MaterialAlertDialogBuilder(v.context).setTitle("Light cycle")
                        .setView(dialogView).setNegativeButton("Cancel") { dialogInterface, which ->
                        }.setPositiveButton("Save") { dialogInterface, which ->
                            val selectedValue = seekBar.progress
                            viewModel.light(Pair(selectedValue, LocalDate.now()))
                        }.show()
                    true
                }
                // Dialog Dead
                R.id.dead_option -> {
                    viewModel.markAsDead()
                    true
                }

                else -> false
            }
        }
        popup.show()
    }

    /**
     * displays the photo option menu and handles the selected option.
     *
     * @param v View
     */
    open fun showPhotoOptionMenu(v: View) {

        val popup = PopupMenu(requireContext(), v)
        popup.menuInflater.inflate(R.menu.add_photo_menu, popup.menu)

        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.take_photo_option -> {
                    dispatchTakePictureIntent()
                    true
                }

                R.id.add_from_libary_option -> {
                    dispatchPickPictureIntent()
                    true
                }

                else -> false
            }
        }
        popup.show()
    }
}

