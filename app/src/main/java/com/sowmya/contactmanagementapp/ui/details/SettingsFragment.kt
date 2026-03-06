package com.sowmya.contactmanagementapp.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sowmya.contactmanagementapp.R
import com.sowmya.contactmanagementapp.databinding.FragmentSettingsBinding
import com.sowmya.contactmanagementapp.viewmodel.ContactViewModel

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ContactViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.btnAccountSettings.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ProfileFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnClearData.setOnClickListener {
            showClearDataDialog()
        }

        binding.getAbout.setOnClickListener {
            Toast.makeText(context, "Contact Manager v1.0\nDramatic Theme Edition", Toast.LENGTH_LONG).show()
        }
    }

    private fun showClearDataDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Clear All Contacts")
            .setMessage("Are you sure you want to delete all contacts? This cannot be undone.")
            .setPositiveButton("Clear") { _, _ ->
                viewModel.deleteAll()
                Toast.makeText(context, "All contacts cleared", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
