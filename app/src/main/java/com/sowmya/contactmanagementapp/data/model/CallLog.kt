package com.sowmya.contactmanagementapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "call_logs")
data class CallLog(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val contactName: String?,
    val phoneNumber: String,
    val timestamp: Long,
    val duration: Long,
    val type: String, // "All", "Missed"
    val isMissed: Boolean = false
)
