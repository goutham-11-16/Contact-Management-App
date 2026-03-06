package com.sowmya.contactmanagementapp.ui.details

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.sowmya.contactmanagementapp.R
import com.sowmya.contactmanagementapp.data.model.Contact
import com.sowmya.contactmanagementapp.databinding.FragmentContactDetailBinding
import com.sowmya.contactmanagementapp.viewmodel.ContactViewModel
import com.sowmya.contactmanagementapp.ui.edit.EditContactFragment
import kotlinx.coroutines.launch

class ContactDetailFragment : Fragment() {
    private var _binding: FragmentContactDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ContactViewModel by viewModels()
    private var contactId: Int = -1
    private var currentContact: Contact? = null

    companion object {
        private const val ARG_CONTACT_ID = "contact_id"
        private const val REQUEST_CALL_PERMISSION = 123

        fun newInstance(id: Int): ContactDetailFragment {
            val fragment = ContactDetailFragment()
            val args = Bundle()
            args.putInt(ARG_CONTACT_ID, id)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contactId = arguments?.getInt(ARG_CONTACT_ID) ?: -1
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentContactDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadContact()

        binding.btnBack.setOnClickListener { parentFragmentManager.popBackStack() }
        binding.btnCall.setOnClickListener { makeCall() }
        binding.btnMessage.setOnClickListener { sendMessage() }
        binding.btnFavorite.setOnClickListener { toggleFavorite() }
        binding.btnDelete.setOnClickListener { deleteContact() }
        binding.btnEdit.setOnClickListener { navigateToEdit() }
        binding.btnShare.setOnClickListener { shareContact() }
    }


    private fun navigateToEdit() {
        val fragment = EditContactFragment.newInstance(contactId)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun loadContact() {
        lifecycleScope.launch {
            currentContact = viewModel.getContactById(contactId)
            currentContact?.let { populateUI(it) }
        }
    }

    private fun populateUI(contact: Contact) {
        binding.tvName.text = contact.name
        binding.tvPhone.text = contact.phone
        
        Glide.with(this)
            .load(contact.imagePath)
            .placeholder(android.R.drawable.ic_menu_myplaces)
            .into(binding.ivProfile)

        updateFavoriteUI(contact.isFavorite)
    }

    private fun updateFavoriteUI(isFavorite: Boolean) {
        if (isFavorite) {
            binding.btnFavorite.setImageResource(android.R.drawable.star_big_on)
        } else {
            binding.btnFavorite.setImageResource(android.R.drawable.star_big_off)
        }
    }

    private fun makeCall() {
        currentContact?.let {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CALL_PHONE), REQUEST_CALL_PERMISSION)
            } else {
                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:${it.phone}"))
                startActivity(intent)
            }
        }
    }

    private fun sendMessage() {
        currentContact?.let {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:${it.phone}"))
            startActivity(intent)
        }
    }

    private fun toggleFavorite() {
        currentContact?.let {
            val updated = it.copy(isFavorite = !it.isFavorite)
            viewModel.update(updated)
            currentContact = updated
            updateFavoriteUI(updated.isFavorite)
        }
    }

    private fun deleteContact() {
        currentContact?.let {
            viewModel.delete(it)
            Toast.makeText(context, "Contact Deleted", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }
    }

    private fun shareContact() {
        currentContact?.let { contact ->
            val fragment = QRShareFragment()
            val args = Bundle().apply {
                putSerializable("contact", contact)
            }
            fragment.arguments = args
            
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
