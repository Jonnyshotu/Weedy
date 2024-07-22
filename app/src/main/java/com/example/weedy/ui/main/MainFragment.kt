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
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.weedy.R
import com.example.weedy.SharedViewModel
import com.example.weedy.data.models.record.RepotRecord
import com.example.weedy.data.models.record.GrowthStateRecord
import com.example.weedy.data.models.record.HealthRecord
import com.example.weedy.data.models.record.LightRecord
import com.example.weedy.data.models.record.NutrientsRecord
import com.example.weedy.data.models.record.RepellentsRecord
import com.example.weedy.data.models.record.TrainingRecord
import com.example.weedy.data.models.record.WateringRecord
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.time.LocalDate

/**
 * Abstract base fragment class providing common functionalities for handling plant data.
 */
abstract class MainFragment : Fragment() {

    private val TAG = "Main Fragment"

    private val viewModel: SharedViewModel by activityViewModels()

    //region Photo request constants
    val REQUEST_CODE_PERMISSIONS = 1001
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_PICK = 2
    val REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    //endregion

    /**
     * Checks if all required permissions are granted.
     *
     * @return True if all required permissions are granted, otherwise false.
     */
    open fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Handles the result of permission requests.
     *
     * @param requestCode The request code for the permission request.
     * @param permissions The requested permissions.
     * @param grantResults The grant results for the permissions.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // Permissions granted
            } else {
                Log.d(TAG, "Camera permissions not granted")
            }
        }
    }

    /**
     * Initiates an intent to capture a photo using the device's camera.
     */
    protected fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    /**
     * Initiates an intent to pick a photo from the device's gallery.
     */
    protected fun dispatchPickPictureIntent() {
        val pickPhotoIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_PICK)
    }

    /**
     * Handles the result of the photo capture or pick operation.
     *
     * @param requestCode The request code of the operation.
     * @param resultCode The result code of the operation.
     * @param data The intent containing the result data.
     */
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

    /**
     * Called when an image is captured.
     *
     * @param imageBitmap The captured image as a Bitmap.
     */
    abstract fun onImageCaptured(imageBitmap: Bitmap)

    /**
     * Called when an image is picked from the gallery.
     *
     * @param imageUri The URI of the picked image.
     */
    abstract fun onImagePicked(imageUri: Uri?)

    //region Treatment Menu

    /**
     * Displays the treatment options menu and handles the selected item.
     *
     * @param masterPlantID The ID of the plant.
     * @param v The view that triggered the menu.
     */
    open fun showTreatmentMenu(masterPlantID: Long, v: View) {

        val popup = PopupMenu(requireContext(), v)
        popup.menuInflater.inflate(R.menu.treatment_menu, popup.menu)

        var nutrientsNameList: List<String> = emptyList()

        viewModel.nutrientsList.observe(viewLifecycleOwner) { nutrientsList ->
            nutrientsNameList = nutrientsList.map { it.name }
            Log.d(TAG, "Nutrients list: $nutrientsNameList")
        }

        var soilNameList: List<String> = emptyList()

        viewModel.soilList.observe(viewLifecycleOwner) { soilList ->
            soilNameList = soilList.map { it.name }
            Log.d(TAG, "Soil list: $soilNameList")
        }

        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.photo_option -> {
                    showPhotoOptionMenu(v)
                    true
                }

                R.id.health_option -> {
                    val dialogView =
                        LayoutInflater.from(v.context).inflate(R.layout.dialog_health, null, false)
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
                                        plantID = masterPlantID,
                                        health = health,
                                        date = LocalDate.now()
                                    )
                                )
                                Toast.makeText(
                                    requireContext(),
                                    "Health record saved",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } catch (e: Exception) {
                                Log.e(TAG, "Failed inserting health record", e)
                            }
                        }.show()
                    true
                }

                R.id.watering_option -> {
                    val dialogView =
                        LayoutInflater.from(v.context).inflate(R.layout.dialog_water, null, false)
                    val input = dialogView.findViewById<EditText>(R.id.waterDialogET)
                    Log.d(TAG, "Watering clicked")

                    MaterialAlertDialogBuilder(v.context).setTitle(resources.getString(R.string.watering))
                        .setView(dialogView).setNegativeButton("Cancel") { _, _ ->
                        }.setPositiveButton("Save") { _, _ ->
                            val watering = input.text.toString().toDoubleOrNull()
                            try {
                                if (watering != null) {
                                    Log.d(TAG, "Watering: $watering")

                                    viewModel.insertWatering(
                                        WateringRecord(
                                            id = 0,
                                            plantID = masterPlantID,
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

                R.id.nutrients_option -> {
                    val dialogView = LayoutInflater.from(v.context)
                        .inflate(R.layout.dialog_nutrients, null, false)
                    val spinner = dialogView.findViewById<Spinner>(R.id.spinner_nutrients)
                    val input = dialogView.findViewById<EditText>(R.id.nutrientsDialogET)

                    val items = nutrientsNameList

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
                                            plantID = masterPlantID,
                                            nutrientName = nutrient.name,
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

                R.id.repellents_option -> {
                    val dialogView = LayoutInflater.from(v.context)
                        .inflate(R.layout.dialog_repellents, null, false)
                    val input = dialogView.findViewById<EditText>(R.id.repellentsDialogET)

                    MaterialAlertDialogBuilder(v.context).setTitle("Repellents").setView(dialogView)
                        .setNegativeButton("Cancel") { _, _ ->
                        }.setPositiveButton("Save") { _, _ ->
                            if (input.text.isNotBlank()) {
                                try {
                                    viewModel.insertRepellent(
                                        RepellentsRecord(
                                            id = 0,
                                            plantID = masterPlantID,
                                            infestationType = input.text.toString(),
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

                R.id.growth_state_change_option -> {
                    val dialog = LayoutInflater.from(v.context)
                        .inflate(R.layout.dialog_growth_state, null, false)
                    val radioGroup = dialog.findViewById<RadioGroup>(R.id.dialogGrowthStateRG)

                    MaterialAlertDialogBuilder(v.context).setTitle("Growth State").setView(dialog)
                        .setNegativeButton("Cancel") { _, _ ->
                        }.setPositiveButton("Save") { _, _ ->
                            val selectedId = radioGroup.checkedRadioButtonId
                            val growthState = when (selectedId) {
                                R.id.germination_radio -> 1
                                R.id.seedling_radio -> 2
                                R.id.cutting_radio -> 3
                                R.id.veg_radio -> 4
                                R.id.flower_radio -> 5
                                else -> 0
                            }
                            try {
                                viewModel.insertGrowthState(
                                    GrowthStateRecord(
                                        id = 0,
                                        plantID = masterPlantID,
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

                R.id.repot_option -> {
                    Log.d(TAG, "Repot option clicked")

                    val dialogView =
                        LayoutInflater.from(v.context).inflate(R.layout.dialog_repot, null, false)
                    val spinner = dialogView.findViewById<Spinner>(R.id.dialogRepotSpinner_pot_size)
                    val input = dialogView.findViewById<EditText>(R.id.dialogRepotPotSizeET)

                    var items: List<String> = soilNameList

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
                                        RepotRecord(
                                            id = 0,
                                            plantID = masterPlantID,
                                            soilID = soil.id,
                                            soilName = soil.name,
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

                R.id.training_option -> {
                    Log.d(TAG, "Training option clicked")
                    val dialogView = LayoutInflater.from(v.context)
                        .inflate(R.layout.dialog_training, null, false)
                    val input = dialogView.findViewById<EditText>(R.id.trainingDialogET)

                    MaterialAlertDialogBuilder(v.context).setTitle("Training")
                        .setView(dialogView)
                        .setNegativeButton("Cancel") { _, _ ->
                        }.setPositiveButton("Save") { _, _ ->
                            if (input.text.isNotBlank()) {
                                try {
                                    viewModel.insertTraining(
                                        TrainingRecord(
                                            id = 0,
                                            plantID = masterPlantID,
                                            trainingType = input.text.toString(),
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

                R.id.light_option -> {
                    val dialogView =
                        LayoutInflater.from(v.context)
                            .inflate(R.layout.dialog_light, null, false)
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

                        override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
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
                                            plantID = masterPlantID,
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

                R.id.dead_option -> {
                    MaterialAlertDialogBuilder(v.context).setTitle("Delete plant")
                        .setNegativeButton("Cancel") { _, _ ->
                        }.setPositiveButton("Delete") { _, _ ->
                            viewModel.deletePlantByID(masterPlantID)
                        }.show()
                    true
                }

                else -> false
            }
        }
        popup.show()
    }

    //endregion

    //region Photo Option Menu

    /**
     * Displays the photo option menu and handles the selected option.
     *
     * @param v The view that triggered the menu.
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

    //endregion

    //region View Animation

    /**
     * Animates the view to be gone with fade out and scale down effects.
     *
     * @param view The view to animate.
     */
    open fun animateViewToGone(view: View) {
        view.animate()
            .alpha(0f)
            .scaleX(0f)
            .scaleY(0f)
            .setDuration(300)
            .withEndAction {
                view.visibility = View.GONE
                view.alpha = 1f
                view.scaleX = 1f
                view.scaleY = 1f
            }
            .start()
    }

    /**
     * Animates the view to be invisible with fade out and scale down effects.
     *
     * @param view The view to animate.
     */
    open fun animateViewToInvisible(view: View) {
        view.animate()
            .alpha(0f)
            .scaleX(0f)
            .scaleY(0f)
            .setDuration(300)
            .withEndAction {
                view.visibility = View.INVISIBLE
                view.alpha = 1f
                view.scaleX = 1f
                view.scaleY = 1f
            }
            .start()
    }

    /**
     * Animates the view to be visible with fade in and scale up effects.
     *
     * @param view The view to animate.
     */
    open fun animateViewToVisible(view: View) {
        view.visibility = View.VISIBLE
        view.alpha = 0f
        view.scaleX = 0f
        view.scaleY = 0f
        view.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(300)
            .start()
    }

    //endregion
}
