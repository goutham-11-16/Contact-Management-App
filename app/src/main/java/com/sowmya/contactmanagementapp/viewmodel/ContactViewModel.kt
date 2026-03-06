package com.sowmya.contactmanagementapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.sowmya.contactmanagementapp.data.database.ContactDatabase
import com.sowmya.contactmanagementapp.data.model.Contact
import com.sowmya.contactmanagementapp.data.repository.ContactRepository
import kotlinx.coroutines.launch

class ContactViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ContactRepository
    val allContacts: LiveData<List<Contact>>

    init {
        val contactDao = ContactDatabase.getDatabase(application).contactDao()
        repository = ContactRepository(contactDao)
        allContacts = repository.allContacts.asLiveData()
    }

    fun searchContacts(query: String): LiveData<List<Contact>> {
        return repository.searchContacts(query).asLiveData()
    }

    fun insert(contact: Contact) = viewModelScope.launch {
        repository.insert(contact)
    }

    fun update(contact: Contact) = viewModelScope.launch {
        repository.update(contact)
    }

    fun delete(contact: Contact) = viewModelScope.launch {
        repository.delete(contact)
    }
    
    suspend fun getContactById(id: Int): Contact? {
        return repository.getContactById(id)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}

