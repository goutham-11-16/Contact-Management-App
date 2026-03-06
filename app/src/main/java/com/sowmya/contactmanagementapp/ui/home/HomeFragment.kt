package com.sowmya.contactmanagementapp.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sowmya.contactmanagementapp.R
import com.sowmya.contactmanagementapp.data.model.Contact
import com.sowmya.contactmanagementapp.data.model.VCardManager
import com.sowmya.contactmanagementapp.databinding.FragmentHomeBinding

import com.sowmya.contactmanagementapp.ui.addcontact.AddContactFragment
import com.sowmya.contactmanagementapp.ui.details.ContactDetailFragment
import com.sowmya.contactmanagementapp.ui.details.ProfileFragment
import com.sowmya.contactmanagementapp.ui.details.ScannerActivity
import com.sowmya.contactmanagementapp.ui.details.SettingsFragment
import com.sowmya.contactmanagementapp.viewmodel.ContactViewModel


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ContactViewModel by viewModels()
    private lateinit var adapter: ContactAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeContacts()
        setupSearchView()

        binding.fabAdd.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AddContactFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnQRScanner.setOnClickListener {
            startQRScanner()
        }

        binding.btnProfile.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ProfileFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnSettings.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SettingsFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun startQRScanner() {
        // We'll use the ScannerActivity from the implementation plan
        val intent = Intent(requireContext(), ScannerActivity::class.java)
        qrScannerLauncher.launch(intent)
    }

    private val qrScannerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val scannnedData = result.data?.getStringExtra("SCAN_RESULT")
            scannnedData?.let { data ->
                val scannedContact = VCardManager.fromVCard(data)
                scannedContact?.let { contact ->
                    val fragment = AddContactFragment.newInstance(contact)
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit()
                }
            }
        }
    }


    private fun setupRecyclerView() {
        adapter = ContactAdapter { contact ->
            val fragment = ContactDetailFragment.newInstance(contact.id)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
    }

    private fun observeContacts() {
        viewModel.allContacts.observe(viewLifecycleOwner) { contacts ->
            adapter.submitList(contacts)
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    viewModel.searchContacts(newText).observe(viewLifecycleOwner) { filtered ->
                        adapter.submitList(filtered)
                    }
                }
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
