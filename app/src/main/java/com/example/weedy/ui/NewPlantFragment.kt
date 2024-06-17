package com.example.weedy.ui

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.weedy.R
import com.example.weedy.SharedViewModel
import com.example.weedy.data.entities.RemoteGenetic
import com.example.weedy.databinding.FragmentNewPlantBinding
import com.example.weedy.ui.main.MainFragment
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NewPlantFragment : MainFragment() {
    private val TAG = "New Plant Fragment"
    private lateinit var binding: FragmentNewPlantBinding
    private lateinit var plantImageIV: ImageView
    private val viewModel: SharedViewModel by activityViewModels()

    private var remoteGeneticList: List<RemoteGenetic>? = null
    private var debouncedTextWatcher: TextWatcher? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewPlantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        plantImageIV = binding.newPlantPlantIV
        binding.newPlantPhotoBTN.setOnClickListener {
            showPhotoOptionMenu(it)
        }

        setupStrainInputObserver()
        setupStrainInputTextWatcher()

        val initialAdapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf("")
        )
        initialAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.newPlantStrainSpinner.adapter = initialAdapter

        binding.newPlantSeedCompanySpinner.adapter = initialAdapter

        binding.newPlantStrainSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    if (position > 0) {
                        val selectedStrain = parent.getItemAtPosition(position) as String
                        binding.newPlantStrainET.setText(selectedStrain)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }

        binding.newPlantSeedCompanySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    if (position > 0) {
                        val selectedCompany = parent.getItemAtPosition(position) as String
                        binding.newPlantSeedCompanyEt.setText(selectedCompany)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }

        viewModel.soilList.observe(viewLifecycleOwner) {soilList ->

            binding.newPlantSpinnerPotSize.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                soilList.map { it.name }
            )
        }


        binding.newPlantSpinnerPotSize.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    if (position > 0) {
                        val selectedSoil = parent.getItemAtPosition(position) as String
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
    }

    private fun setupStrainInputObserver() {
        viewModel.remoteGeneticCollection.observe(viewLifecycleOwner) {
            remoteGeneticList = it
            Log.d(TAG, "Remote genetic list size: ${it.size}")
        }
    }

    private fun setupStrainInputTextWatcher() {
        debouncedTextWatcher = object : TextWatcher {
            private var debounceJob: Job? = null

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                debounceJob?.cancel()
                debounceJob = viewLifecycleOwner.lifecycleScope.launch {
                    delay(500)
                    updateStrainSpinner(s.toString())
                    updateCompanySpinner(s.toString())
                }
            }
        }
        binding.newPlantStrainET.addTextChangedListener(debouncedTextWatcher)
    }

    private fun updateStrainSpinner(input: String) {
        val filter = remoteGeneticList?.filter { genetic ->
            genetic.strainName.contains(input, ignoreCase = true)
        }

        if (!filter.isNullOrEmpty()) {
            val strainNames = listOf("") + filter.map { it.strainName }
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                strainNames
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.newPlantStrainSpinner.adapter = adapter
            binding.newPlantStrainSpinner.visibility = View.VISIBLE
        } else {
            binding.newPlantStrainSpinner.visibility = View.INVISIBLE
        }
    }

    private fun updateCompanySpinner(input: String) {
        val filter = remoteGeneticList?.filter { genetic ->
            genetic.strainName.contains(input, ignoreCase = true)
        }

        if (!filter.isNullOrEmpty()) {
            val companyNames = listOf("") + filter.map { it.seedCompany }
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                companyNames
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.newPlantSeedCompanySpinner.adapter = adapter
            binding.newPlantSeedCompanySpinner.visibility = View.VISIBLE
        } else {
            binding.newPlantSeedCompanySpinner.visibility = View.INVISIBLE
        }
    }

    override fun onImageCaptured(imageBitmap: Bitmap) {
        plantImageIV.setImageBitmap(imageBitmap)
    }

    override fun onImagePicked(imageUri: Uri?) {
        plantImageIV.setImageURI(imageUri)
    }
}
