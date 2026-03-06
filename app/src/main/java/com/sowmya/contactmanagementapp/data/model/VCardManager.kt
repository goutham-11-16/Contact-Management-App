package com.sowmya.contactmanagementapp.data.model

import com.sowmya.contactmanagementapp.data.model.Contact


object VCardManager {
    fun toVCard(contact: Contact): String {
        return """
            BEGIN:VCARD
            VERSION:3.0
            FN:${contact.name}
            TEL;TYPE=CELL:${contact.phone}
            EMAIL:${contact.email}
            END:VCARD
        """.trimIndent()
    }

    fun fromVCard(vCard: String): Contact? {
        val lines = vCard.lines()
        if (!lines.any { it.startsWith("BEGIN:VCARD") }) return null

        var name = ""
        var phone = ""
        var email = ""

        lines.forEach { line ->
            when {
                line.startsWith("FN:") -> name = line.substringAfter("FN:")
                line.startsWith("TEL") -> phone = line.substringAfter(":")
                line.startsWith("EMAIL:") -> email = line.substringAfter("EMAIL:")
            }
        }

        return if (name.isNotEmpty() && phone.isNotEmpty()) {
            Contact(name = name, phone = phone, email = email)
        } else null
    }
}
