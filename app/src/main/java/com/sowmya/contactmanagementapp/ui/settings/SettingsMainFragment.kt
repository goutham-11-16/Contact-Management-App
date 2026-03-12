package com.sowmya.contactmanagementapp.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sowmya.contactmanagementapp.R
import com.sowmya.contactmanagementapp.databinding.FragmentSettingsMainBinding

class SettingsMainFragment : Fragment() {
    private var _binding: FragmentSettingsMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRows()
    }

    private fun setupRows() {
        binding.itemSpeedDial.apply {
            tvTitle.text = "Speed dial"
            root.setOnClickListener { navigateTo(SpeedDialFragment()) }
        }
        binding.itemAnswerEndCalls.apply {
            tvTitle.text = "Answer/end calls"
            root.setOnClickListener { navigateTo(AnswerEndCallsFragment()) }
        }
        binding.itemCallRecording.apply {
            tvTitle.text = "Call recording"
            root.setOnClickListener { navigateTo(CallRecordingFragment()) }
        }
        binding.itemBlockFilter.apply {
            tvTitle.text = "Block & filter"
            root.setOnClickListener { navigateTo(BlockFilterFragment()) }
        }
        binding.itemMoreSettings.apply {
            tvTitle.text = "More settings"
            root.setOnClickListener { navigateTo(MoreSettingsFragment()) }
        }
        binding.itemEdgeLighting.apply {
            tvTitle.text = "Edge lighting for incoming calls"
            root.setOnClickListener { navigateTo(EdgeLightingFragment()) }
        }
    }

    private fun navigateTo(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
