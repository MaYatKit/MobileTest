package com.example.mobiletest.data.source.network

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class BookingNetworkDataSource @Inject constructor(
    @ApplicationContext val context: Context
) : NetworkDataSource {

    // Prevent refresh overlap
    private val accessMutex = Mutex()

    override suspend fun loadBookings(): List<NetworkBooking> = accessMutex.withLock {
        val json = loadJsonFromAssets("booking.json")
        val networkBookingType = object : TypeToken<NetworkBooking>() {}.type
        val gson = Gson()

        val networkBooking: NetworkBooking = gson.fromJson(json, networkBookingType)

        return listOf(networkBooking)
    }


    private fun loadJsonFromAssets(fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }


}

private const val SERVICE_LATENCY_IN_MILLIS = 3000L
