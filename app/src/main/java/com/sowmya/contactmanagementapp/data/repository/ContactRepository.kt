package com.sowmya.contactmanagementapp.data.repository

import com.sowmya.contactmanagementapp.data.database.ContactDao
import com.sowmya.contactmanagementapp.data.model.Contact
import kotlinx.coroutines.flow.Flow

class ContactRepository(private val contactDao: ContactDao) {
    val allContacts: Flow<List<Contact>> = contactDao.getAllContacts()

    fun searchContacts(query: String): Flow<List<Contact>> {
        return contactDao.searchContacts("%$query%")
    }

    suspend fun insert(contact: Contact) {
        contactDao.insertContact(contact)
    }

    suspend fun update(contact: Contact) {
        contactDao.updateContact(contact)
    }

    suspend fun delete(contact: Contact) {
        contactDao.deleteContact(contact)
    }

    suspend fun getContactById(id: Int): Contact? {
        return contactDao.getContactById(id)
    }

    suspend fun deleteAll() {
        contactDao.deleteAllContacts()
    }
}

