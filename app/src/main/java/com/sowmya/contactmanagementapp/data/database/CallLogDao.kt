package com.sowmya.contactmanagementapp.data.database

import androidx.room.*
import com.sowmya.contactmanagementapp.data.model.CallLog
import kotlinx.coroutines.flow.Flow

@Dao
interface CallLogDao {
    @Query("SELECT * FROM call_logs ORDER BY timestamp DESC")
    fun getAllCallLogs(): Flow<List<CallLog>>

    @Query("SELECT * FROM call_logs WHERE isMissed = 1 ORDER BY timestamp DESC")
    fun getMissedCallLogs(): Flow<List<CallLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCallLog(callLog: CallLog)

    @Delete
    suspend fun deleteCallLog(callLog: CallLog)

    @Query("DELETE FROM call_logs")
    suspend fun deleteAllCallLogs()
}
