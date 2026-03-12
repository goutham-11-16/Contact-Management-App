package com.sowmya.contactmanagementapp.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sowmya.contactmanagementapp.databinding.FragmentSpeedDialBinding

class SpeedDialFragment : Fragment() {
    private var _binding: FragmentSpeedDialBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSpeedDialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRows()
    }

    private fun setupRows() {
        binding.sd1.tvKey.text = "1"
        binding.sd1.tvContactName.text = "Voicemail"
        binding.sd1.tvDescription.text = "Set by system"
        
        binding.sd2.tvKey.text = "2"
        binding.sd3.tvKey.text = "3"
        binding.sd4.tvKey.text = "4"
        binding.sd5.tvKey.text = "5"
        binding.sd6.tvKey.text = "6"
        binding.sd7.tvKey.text = "7"
        binding.sd8.tvKey.text = "8"
        binding.sd9.tvKey.text = "9"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
