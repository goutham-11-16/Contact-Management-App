package com.sowmya.contactmanagementapp.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sowmya.contactmanagementapp.R
import com.sowmya.contactmanagementapp.databinding.FragmentAnswerEndCallsBinding
import com.sowmya.contactmanagementapp.databinding.FragmentCallRecordingBinding
import com.sowmya.contactmanagementapp.databinding.FragmentBlockFilterBinding
import com.sowmya.contactmanagementapp.databinding.FragmentMoreSettingsBinding
import com.sowmya.contactmanagementapp.databinding.FragmentEdgeLightingBinding
import com.sowmya.contactmanagementapp.ui.home.ContactAdapter
import com.sowmya.contactmanagementapp.viewmodel.ContactViewModel

class AnswerEndCallsFragment : Fragment() {
    private var _binding: FragmentAnswerEndCallsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnswerEndCallsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class CallRecordingFragment : Fragment() {
    private var _binding: FragmentCallRecordingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCallRecordingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class BlockFilterFragment : Fragment() {
    private var _binding: FragmentBlockFilterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ContactViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBlockFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val adapter = ContactAdapter(onContactClick = { /* No action on click for now */ })
        binding.rvBlockedContacts.layoutManager = LinearLayoutManager(context)
        binding.rvBlockedContacts.adapter = adapter
        
        viewModel.allContacts.observe(viewLifecycleOwner) { contacts ->
            adapter.submitList(contacts.filter { it.isBlocked })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class MoreSettingsFragment : Fragment() {
    private var _binding: FragmentMoreSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoreSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class EdgeLightingFragment : Fragment() {
    private var _binding: FragmentEdgeLightingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEdgeLightingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
