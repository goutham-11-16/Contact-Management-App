package com.sowmya.contactmanagementapp.ui.calls

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sowmya.contactmanagementapp.R
import com.sowmya.contactmanagementapp.data.model.CallLog
import com.sowmya.contactmanagementapp.databinding.FragmentCallsBinding
import com.sowmya.contactmanagementapp.ui.settings.BlockFilterFragment
import com.sowmya.contactmanagementapp.ui.settings.ManageContactsFragment
import com.sowmya.contactmanagementapp.ui.settings.SettingsMainFragment
import androidx.appcompat.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager

class CallsFragment : Fragment() {
    private var _binding: FragmentCallsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCallsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var adapter: CallLogAdapter
    private var isMissedFilter = false
    private var allLogs = listOf<CallLog>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupNotch()
        setupMenu()
        
        binding.fabDialPad.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, DialPadFragment())
                .addToBackStack(null)
                .commit()
        }

        // Dummy data for testing
        allLogs = listOf(
            CallLog(1, "Sowmya", "1234567890", System.currentTimeMillis(), 120, "All", false),
            CallLog(2, "Unknown", "9876543210", System.currentTimeMillis() - 3600000, 0, "Missed", true),
            CallLog(3, "Goutham", "5556667777", System.currentTimeMillis() - 7200000, 300, "All", false),
            CallLog(4, "Amulya", "1112223333", System.currentTimeMillis() - 10800000, 0, "Missed", true)
        )
        filterLogs()
    }

    private val selectedLogs = mutableSetOf<CallLog>()

    private fun setupRecyclerView() {
        adapter = CallLogAdapter({ log ->
            // Original delete or click
        }, { log, isChecked ->
            if (isChecked) selectedLogs.add(log) else selectedLogs.remove(log)
        })
        binding.rvCallLogs.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        binding.rvCallLogs.adapter = adapter
    }

    private fun setupNotch() {
        binding.layoutNotch.tvAll.setOnClickListener {
            isMissedFilter = false
            updateNotchUI()
        }
        binding.layoutNotch.tvMissed.setOnClickListener {
            isMissedFilter = true
            updateNotchUI()
        }
        binding.layoutNotch.btnSearch.setOnClickListener {
            // Show search bar or just filter
            adapter.submitList(adapter.currentList.filter { 
                it.contactName?.contains("S", ignoreCase = true) ?: false 
            })
        }
    }

    private fun updateNotchUI() {
        if (isMissedFilter) {
            binding.layoutNotch.tvMissed.setBackgroundResource(R.drawable.notch_item_selected)
            binding.layoutNotch.tvMissed.setTextColor(requireContext().getColor(R.color.white))
            binding.layoutNotch.tvAll.setBackground(null)
            binding.layoutNotch.tvAll.setTextColor(requireContext().getColor(R.color.oneplus_text_secondary))
        } else {
            binding.layoutNotch.tvAll.setBackgroundResource(R.drawable.notch_item_selected)
            binding.layoutNotch.tvAll.setTextColor(requireContext().getColor(R.color.white))
            binding.layoutNotch.tvMissed.setBackground(null)
            binding.layoutNotch.tvMissed.setTextColor(requireContext().getColor(R.color.oneplus_text_secondary))
        }
        filterLogs()
    }

    private fun filterLogs() {
        val filtered = if (isMissedFilter) {
            allLogs.filter { it.isMissed }
        } else {
            allLogs
        }
        adapter.submitList(filtered)
    }

    private fun setupMenu() {
        binding.layoutNotch.btnMore.setOnClickListener { view ->
            val popup = androidx.appcompat.widget.PopupMenu(requireContext(), view)
            popup.menu.add(0, 1, 0, "Edit")
            popup.menu.add(0, 2, 0, "Block & filter")
            popup.menu.add(0, 3, 0, "Manage contacts")
            popup.menu.add(0, 4, 0, "Settings")
            
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    1 -> {
                        adapter.isEditMode = true
                        binding.layoutNotch.btnDelete.visibility = View.VISIBLE
                        binding.layoutNotch.btnMore.visibility = View.GONE
                        binding.layoutNotch.btnSearch.visibility = View.GONE
                        true
                    }
                    2 -> {
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, BlockFilterFragment())
                            .addToBackStack(null)
                            .commit()
                        true
                    }
                    3 -> {
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, ManageContactsFragment())
                            .addToBackStack(null)
                            .commit()
                        true
                    }
                    4 -> {
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, SettingsMainFragment())
                            .addToBackStack(null)
                            .commit()
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }

        binding.layoutNotch.btnDelete.setOnClickListener {
            if (selectedLogs.isEmpty()) {
                Toast.makeText(context, "No logs selected", Toast.LENGTH_SHORT).show()
                adapter.isEditMode = false
                binding.layoutNotch.btnDelete.visibility = View.GONE
                binding.layoutNotch.btnMore.visibility = View.VISIBLE
                binding.layoutNotch.btnSearch.visibility = View.VISIBLE
                return@setOnClickListener
            }

            AlertDialog.Builder(requireContext())
                .setTitle("Delete Call Logs")
                .setMessage("Are you sure you want to delete ${selectedLogs.size} call logs?")
                .setPositiveButton("Delete") { _, _ ->
                    val currentList = adapter.currentList.toMutableList()
                    currentList.removeAll(selectedLogs)
                    adapter.submitList(currentList)
                    selectedLogs.clear()
                    
                    adapter.isEditMode = false
                    binding.layoutNotch.btnDelete.visibility = View.GONE
                    binding.layoutNotch.btnMore.visibility = View.VISIBLE
                    binding.layoutNotch.btnSearch.visibility = View.VISIBLE
                    Toast.makeText(context, "Call logs deleted", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
