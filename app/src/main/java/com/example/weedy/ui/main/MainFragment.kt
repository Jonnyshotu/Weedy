package com.example.weedy.ui.main

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.RatingBar
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.example.weedy.R
import com.example.weedy.SharedViewModel
import com.example.weedy.data.entities.MasterPlant
import com.example.weedy.data.entities.Soil
import com.example.weedy.data.models.actions.RepotAction
import com.example.weedy.data.models.record.GrowthStateRecord
import com.example.weedy.data.models.record.HealthRecord
import com.example.weedy.data.models.record.LightRecord
import com.example.weedy.data.models.record.NutrientsRecord
import com.example.weedy.data.models.record.RepellentsRecord
import com.example.weedy.data.models.record.TrainingRecord
import com.example.weedy.data.models.record.WateringRecord
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.time.LocalDate

abstract class MainFragment : Fragment() {

    private val TAG = "Main Fragment"

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
    open fun showTreatmentMenu(plant: MasterPlant, v: View) {

        val popup = PopupMenu(requireContext(), v)
        popup.menuInflater.inflate(R.menu.treatment_menu, popup.menu)

        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
// Dialog Photo
                R.id.photo_option -> {
                    showPhotoOptionMenu(v)
                    true
                }
// Health Dialog
                R.id.health_option -> {
                    val dialogView =
                        LayoutInflater.from(v.context).inflate(R.layout.health_dialog, null, false)
                    val input = dialogView.findViewById<RatingBar>(R.id.healhDialogRB)

                    MaterialAlertDialogBuilder(v.context).setTitle(resources.getString(R.string.watering))
                        .setView(dialogView).setNegativeButton("Cancel") { _, _ ->
                        }.setPositiveButton("Save") { _, _ ->
                            val health: Int
                            when (input.rating) {
                                0F -> health = 0
                                1F -> health = 20
                                2F -> health = 40
                                3F -> health = 60
                                4F -> health = 80
                                5F -> health = 100
                                else -> health = 0
                            }
                            try {
                                viewModel.insertHealth(
                                    HealthRecord(
                                        id = 0,
                                        plantID = plant.id,
                                        health = health,
                                        date = LocalDate.now()
                                    )
                                )
                                Toast.makeText(
                                    requireContext(),
                                    "Healt record saved",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } catch (e: Exception) {
                                Log.e(TAG, "Failed inserting healt record", e)
                            }
                        }.show()
                    true
                }
// Dialog Water
                R.id.watering_option -> {
                    val dialogView =
                        LayoutInflater.from(v.context).inflate(R.layout.water_dialog, null, false)
                    val input = dialogView.findViewById<EditText>(R.id.waterDialogET)
                    Log.d(TAG,"Watering clicked")

                    MaterialAlertDialogBuilder(v.context).setTitle(resources.getString(R.string.watering))
                        .setView(dialogView).setNegativeButton("Cancel") { _, _ ->
                        }.setPositiveButton("Save") { _, _ ->
                            val watering = input.text.toString().toDoubleOrNull()
                            try {
                                if (watering != null) {
                                    Log.d(TAG,"Watering: $watering")

                                    viewModel.insertWatering(
                                        WateringRecord(
                                            id = 0,
                                            plantID = plant.id,
                                            amount = watering,
                                            ph = null,
                                            date = LocalDate.now()
                                        )
                                    )
                                    Toast.makeText(
                                        requireContext(),
                                        "Watering record saved",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } catch (e: Exception) {
                                Log.e(TAG, "Failed inserting watering record", e)
                            }
                        }.show()
                    true
                }
// Dialog Nutrients
                R.id.nutrients_option -> {
                    val dialogView = LayoutInflater.from(v.context)
                        .inflate(R.layout.nutrients_dialog, null, false)
                    val spinner = dialogView.findViewById<Spinner>(R.id.spinner_nutrients)
                    val input = dialogView.findViewById<EditText>(R.id.nutrientsDialogET)

                    var items: List<String> = emptyList()
                    viewModel.nutrientsList.observe(viewLifecycleOwner) { nutrientsList ->
                        items = nutrientsList.map { it.name }
                        Log.d(TAG,"Nutrients list: $items")
                    }
                    val adapter = ArrayAdapter(
                        v.context, android.R.layout.simple_spinner_dropdown_item, items
                    )
                    spinner.adapter = adapter

                    MaterialAlertDialogBuilder(v.context).setTitle("Nutrients").setView(dialogView)
                        .setNegativeButton("Cancel") { _, _ ->
                        }.setPositiveButton("Save") { _, _ ->
                            val selectedItem = spinner.selectedItem.toString()
                            val nutrient =
                                viewModel.nutrientsList.value?.find { it.name == selectedItem }
                            val amount = input.text.toString().toDoubleOrNull()
                            if (amount != null && nutrient != null) {
                                try {
                                    viewModel.insertNutrient(
                                        NutrientsRecord(
                                            id = 0,
                                            plantID = plant.id,
                                            nutrientID = nutrient.id,
                                            amount = amount,
                                            date = LocalDate.now()
                                        )
                                    )
                                    Toast.makeText(
                                        requireContext(),
                                        "Nutrient record saved",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } catch (e: Exception) {
                                    Log.e(TAG, "Failed inserting nutrient record", e)
                                }
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
                        .setNegativeButton("Cancel") { _, _ ->
                        }.setPositiveButton("Save") { _, _ ->
                            if (input.toString().isNotBlank()) {
                                try {
                                    viewModel.insertRepellent(
                                        RepellentsRecord(
                                            id = 0,
                                            plantID = plant.id,
                                            infestationType = input.toString(),
                                            date = LocalDate.now()
                                        )
                                    )
                                    Toast.makeText(
                                        requireContext(),
                                        "Repellent record saved",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } catch (e: Exception) {
                                    Log.e(TAG, "Failed inserting repellent record", e)

                                }
                            }
                        }.show()
                    true
                }
// Dialog Growth State
                R.id.growth_state_change_option -> {
                    val dialog = LayoutInflater.from(v.context)
                        .inflate(R.layout.growth_state_dialog, null, false)
                    val radioGroup = dialog.findViewById<RadioGroup>(R.id.dialogGrowthStateRG)

                    MaterialAlertDialogBuilder(v.context).setTitle("Growth State").setView(dialog)
                        .setNegativeButton("Cancel") { _, _ ->
                        }.setPositiveButton("Save") { _, _ ->
                            val selectedId = radioGroup.checkedRadioButtonId
                            val growthState = when (selectedId) {
                                R.id.seedling_radio -> 1
                                R.id.cutting_radio -> 2
                                R.id.veg_radio -> 3
                                R.id.flower_radio -> 4
                                else -> 0
                            }
                            try {
                                viewModel.insertGrowthState(
                                    GrowthStateRecord(
                                        id = 0,
                                        plantID = plant.id,
                                        growthState = growthState,
                                        date = LocalDate.now()
                                    )
                                )
                                Toast.makeText(
                                    requireContext(),
                                    "Growth state record saved",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } catch (e: Exception) {
                                Log.e(TAG, "Failed inserting growth state record", e)
                            }

                        }.show()
                    true
                }
// Dialog Repot
                R.id.repot_option -> {
                    val dialogView =
                        LayoutInflater.from(v.context).inflate(R.layout.repot_dialog, null, false)
                    val spinner = dialogView.findViewById<Spinner>(R.id.dialogRepotSpinner_pot_size)
                    val input = dialogView.findViewById<EditText>(R.id.dialogRepotPotSizeET)

                    var items: List<String> = emptyList()
                    viewModel.soilList.observe(viewLifecycleOwner) { soilList ->
                        items = soilList.map { it.name }
                        Log.d(TAG,"Soil list: $items")

                    }
                    val adapter = ArrayAdapter(
                        v.context, android.R.layout.simple_spinner_dropdown_item, items
                    )
                    spinner.adapter = adapter

                    MaterialAlertDialogBuilder(v.context).setTitle("Repot").setView(dialogView)
                        .setNegativeButton("Cancel") { _, _ ->
                        }.setPositiveButton("Save") { _, _ ->
                            val selectedItem = spinner.selectedItem.toString()
                            val soil = viewModel.soilList.value?.find { it.name == selectedItem }

                            val potSize = input.text.toString().toDoubleOrNull()
                            if (potSize != null && soil != null) {
                                try {
                                    viewModel.insertRepot(
                                        RepotAction(
                                            id = 0,
                                            plantID = plant.id,
                                            soilID = soil.id,
                                            potSize = potSize,
                                            date = LocalDate.now()
                                        )
                                    )
                                    Toast.makeText(
                                        requireContext(),
                                        "Repot action saved",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } catch (e: Exception) {
                                    Log.e(TAG, "Failed inserting repot action", e)
                                }
                            }
                        }.show()
                    true
                }
// Dialog Trainng
                R.id.training_option -> {
                    val dialogView = LayoutInflater.from(v.context)
                        .inflate(R.layout.training_dialog, null, false)
                    val input = dialogView.findViewById<EditText>(R.id.trainingDialogET)
                    val training = input.toString()

                    MaterialAlertDialogBuilder(v.context).setTitle("Training")
                        .setView(dialogView)
                        .setNegativeButton("Cancel") { _, _ ->
                        }.setPositiveButton("Save") { _, _ ->
                            if (training.isNotBlank()) {
                                try {
                                    viewModel.insertTraining(
                                        TrainingRecord(
                                            id = 0,
                                            plantID = plant.id,
                                            trainingType = training,
                                            date = LocalDate.now()
                                        )
                                    )
                                    Toast.makeText(
                                        requireContext(),
                                        "Training record saved",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } catch (e: Exception) {
                                    Log.e(TAG, "Failed inserting training record", e)
                                }
                            }
                        }.show()
                    true
                }
// Dialog Light Cycle
                R.id.light_option -> {
                    val dialogView =
                        LayoutInflater.from(v.context)
                            .inflate(R.layout.light_dialog, null, false)
                    val seekBar = dialogView.findViewById<SeekBar>(R.id.lightDialogSB)

                    seekBar.setOnSeekBarChangeListener(object :
                        SeekBar.OnSeekBarChangeListener {
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
                        .setView(dialogView).setNegativeButton("Cancel") { _, _ ->
                        }.setPositiveButton("Save") { _, _ ->
                            val selectedValue = seekBar.progress
                            if (selectedValue > 0) {
                                try {
                                    viewModel.insertLight(
                                        LightRecord(
                                            id = 0,
                                            plantID = plant.id,
                                            lightHours = selectedValue,
                                            date = LocalDate.now()
                                        )
                                    )
                                    Toast.makeText(
                                        requireContext(),
                                        "Light record saved",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } catch (e: Exception) {
                                    Log.e(TAG, "Failed inserting light record", e)
                                }
                            }
                        }.show()
                    true
                }

                // TODO Dialog MARK DEAD
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

