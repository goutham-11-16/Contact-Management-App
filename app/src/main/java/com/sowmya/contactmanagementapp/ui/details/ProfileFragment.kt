package com.sowmya.contactmanagementapp.ui.details

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.sowmya.contactmanagementapp.data.model.Contact
import com.sowmya.contactmanagementapp.data.model.ProfileManager
import com.sowmya.contactmanagementapp.data.model.VCardManager
import com.sowmya.contactmanagementapp.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var profileManager: ProfileManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileManager = ProfileManager(requireContext())

        loadProfile()

        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.btnSaveProfile.setOnClickListener {
            saveProfile()
        }
    }

    private fun loadProfile() {
        val profile = profileManager.getProfile()
        profile?.let {
            binding.etMyName.setText(it.name)
            binding.etMyPhone.setText(it.phone)
            binding.etMyEmail.setText(it.email)
            updateQR(it)
        }
    }

    private fun saveProfile() {
        val name = binding.etMyName.text.toString()
        val phone = binding.etMyPhone.text.toString()
        val email = binding.etMyEmail.text.toString()

        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(context, "Name and Phone are required", Toast.LENGTH_SHORT).show()
            return
        }

        val contact = Contact(name = name, phone = phone, email = email)
        profileManager.saveProfile(contact)
        updateQR(contact)
        Toast.makeText(context, "Profile Saved", Toast.LENGTH_SHORT).show()
    }

    private fun updateQR(contact: Contact) {
        val vCard = VCardManager.toVCard(contact)
        val qrBitmap = generateQRCode(vCard)
        binding.ivMyQR.setImageBitmap(qrBitmap)
    }

    private fun generateQRCode(text: String): Bitmap? {
        val writer = QRCodeWriter()
        return try {
            val bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 512, 512)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            bmp
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
