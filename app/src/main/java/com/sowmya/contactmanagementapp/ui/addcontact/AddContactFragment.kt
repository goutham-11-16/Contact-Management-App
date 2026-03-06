package com.sowmya.contactmanagementapp.ui.addcontact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sowmya.contactmanagementapp.data.model.Contact
import com.sowmya.contactmanagementapp.databinding.FragmentAddContactBinding
import com.sowmya.contactmanagementapp.viewmodel.ContactViewModel

class AddContactFragment : Fragment() {
    private var _binding: FragmentAddContactBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ContactViewModel by viewModels()
    private var imageUri: String? = null

    private val pickImage = registerForActivityResult(androidx.activity.result.contract.ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            imageUri = it.toString()
            binding.ivProfile.setImageURI(it)
        }
    }

    companion object {
        private const val ARG_SCANNED_CONTACT = "scanned_contact"

        fun newInstance(contact: Contact): AddContactFragment {
            val fragment = AddContactFragment()
            val args = Bundle()
            args.putSerializable(ARG_SCANNED_CONTACT, contact)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddContactBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scannedContact = arguments?.getSerializable(ARG_SCANNED_CONTACT) as? Contact
        scannedContact?.let {
            binding.etName.setText(it.name)
            binding.etPhone.setText(it.phone)
            binding.etEmail.setText(it.email)
        }

        binding.ivProfile.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.btnSave.setOnClickListener {
            saveContact()
        }
    }


    private fun saveContact() {
        val name = binding.etName.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()

        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(context, "Please fill required fields", Toast.LENGTH_SHORT).show()
            return
        }

        val contact = Contact(name = name, phone = phone, email = email, imagePath = imageUri)
        viewModel.insert(contact)
        Toast.makeText(context, "Contact Saved", Toast.LENGTH_SHORT).show()
        parentFragmentManager.popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
