package com.example.weedy.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.weedy.SharedViewModel
import com.example.weedy.databinding.FragmentCheckupBinding

class CheckupFragment : Fragment() {

    private lateinit var binding: FragmentCheckupBinding
    private val viewModel: SharedViewModel by activityViewModels()

    private val TAG = "Debug_CheckupFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCheckupBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}