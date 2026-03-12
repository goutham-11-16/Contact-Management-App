package com.sowmya.contactmanagementapp.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.sowmya.contactmanagementapp.databinding.FragmentManageContactsBinding
import com.sowmya.contactmanagementapp.viewmodel.ContactViewModel

class ManageContactsFragment : Fragment() {
    private var _binding: FragmentManageContactsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ContactViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManageContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.switchProfilePic.isChecked = viewModel.showProfilePic.value ?: true
        binding.switchOnlyNumbers.isChecked = viewModel.onlyWithNumbers.value ?: false
        
        binding.switchProfilePic.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setShowProfilePic(isChecked)
        }
        
        binding.switchOnlyNumbers.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setOnlyWithNumbers(isChecked)
        }

        binding.rowSortBy.apply {
            tvTitle.text = "Sort contacts by: ${viewModel.sortBy.value}"
            root.setOnClickListener {
                val current = viewModel.sortBy.value
                val next = if (current == "First Name") "Last Name" else "First Name"
                viewModel.setSortBy(next)
                tvTitle.text = "Sort contacts by: $next"
            }
        }
        
        binding.rowImportExport.tvTitle.text = "Import/Export contacts"
        binding.rowSimContacts.tvTitle.text = "SIM card contacts"
        binding.rowMergeContacts.tvTitle.text = "Merge duplicate contacts"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
