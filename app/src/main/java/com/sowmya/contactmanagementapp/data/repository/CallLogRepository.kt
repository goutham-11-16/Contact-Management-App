package com.sowmya.contactmanagementapp.data.repository

import com.sowmya.contactmanagementapp.data.database.CallLogDao
import com.sowmya.contactmanagementapp.data.model.CallLog
import kotlinx.coroutines.flow.Flow

class CallLogRepository(private val callLogDao: CallLogDao) {
    val allCallLogs: Flow<List<CallLog>> = callLogDao.getAllCallLogs()
    val missedCallLogs: Flow<List<CallLog>> = callLogDao.getMissedCallLogs()

    suspend fun insert(callLog: CallLog) {
        callLogDao.insertCallLog(callLog)
    }

    suspend fun delete(callLog: CallLog) {
        callLogDao.deleteCallLog(callLog)
    }

    suspend fun deleteAll() {
        callLogDao.deleteAllCallLogs()
    }
}
