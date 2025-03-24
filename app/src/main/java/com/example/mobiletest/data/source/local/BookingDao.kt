package com.example.mobiletest.data.source.local

import android.util.Log
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach


@Dao
interface BookingDao {

    companion object {
        const val TAG = "BookingDao"
    }

    @Query("SELECT * FROM booking")
    fun observeAll(): Flow<List<LocalBooking>>

    fun observeAllWithLog(): Flow<List<LocalBooking>> {
        return observeAll().onEach { it ->
            Log.d(TAG, "read all bookings from database: ")
            it.forEach {
                Log.d(TAG, "expiryTime = ${it.expiryTime}")
            }
        }
    }


    @Query("SELECT * FROM booking WHERE shipToken = :shipToken")
    fun observeByShipToken(shipToken: String): Flow<LocalBooking>


    @Query("SELECT * FROM booking")
    suspend fun getAll(): List<LocalBooking>

    suspend fun getAllWithLog(): List<LocalBooking> {
        return getAll().onEach { it ->
            Log.d(TAG, "read all bookings from database: ")
            Log.d(TAG, "expiryTime = ${it.expiryTime}")
        }
    }


    @Query("SELECT * FROM booking WHERE shipToken = :shipToken")
    suspend fun getByShipToken(shipToken: String): LocalBooking?


    @Upsert
    suspend fun upsert(booking: LocalBooking)

    suspend fun upsertWithLog(booking: LocalBooking) {
        Log.d(TAG, "update/insert booking from database, shipToken = ${booking.shipToken}")
        upsert(booking)
    }


    @Upsert
    suspend fun upsertAll(bookings: List<LocalBooking>)


    @Query("DELETE FROM booking WHERE shipToken = :shipToken")
    suspend fun deleteByShipToken(shipToken: String): Int

    suspend fun deleteByShipTokenWithLog(shipToken: String) {
        Log.d(TAG, "delete booking by shipToken from database, shipToken = $shipToken")
        deleteByShipToken(shipToken)
    }


    @Query("DELETE FROM booking")
    suspend fun deleteAll()

    suspend fun deleteAllWithLog() {
        Log.d(TAG, "delete all bookings from database")
        deleteAll()
    }


}
