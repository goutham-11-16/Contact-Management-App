package com.sowmya.contactmanagementapp.data.model

import android.content.Context
import android.content.SharedPreferences

class ProfileManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_profile", Context.MODE_PRIVATE)

    fun saveProfile(contact: Contact) {
        prefs.edit().apply {
            putString("name", contact.name)
            putString("phone", contact.phone)
            putString("email", contact.email)
            putString("image", contact.imagePath)
            apply()
        }
    }

    fun getProfile(): Contact? {
        val name = prefs.getString("name", null) ?: return null
        val phone = prefs.getString("phone", "") ?: ""
        val email = prefs.getString("email", "") ?: ""
        val image = prefs.getString("image", null)
        return Contact(name = name, phone = phone, email = email, imagePath = image)
    }
}
