package com.example.mobiletest.data.source.network

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class BookingNetworkDataSource @Inject constructor(
    @ApplicationContext val context: Context
) : NetworkDataSource {

    // Prevent refresh overlap
    private val accessMutex = Mutex()

    override suspend fun loadBookings(): List<NetworkBooking> = accessMutex.withLock {
        Log.d(TAG, "load bookings from network: ")
        delay(SERVICE_LATENCY_IN_MILLIS)
        val json = loadJsonFromAssets("booking.json")
        val networkBookingType = object : TypeToken<NetworkBooking>() {}.type
        val gson = Gson()

        val networkBooking: NetworkBooking = gson.fromJson(json, networkBookingType)

        networkBooking.expiryTime = System.currentTimeMillis().toString()

        Log.d(TAG, "networkBooking = $networkBooking ")

        return listOf(networkBooking)
    }


    override suspend fun loadBookingByShipToken(shipToken: String): NetworkBooking? {
        Log.d(TAG, "load booking by shipToken = $shipToken from network: ")

        val bookings = loadBookings()
        val result = bookings.firstOrNull { it.shipToken == shipToken }

        return result
    }

    private fun loadJsonFromAssets(fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }


}

private const val SERVICE_LATENCY_IN_MILLIS = 3000L

private const val TAG = "BookingNetworkDataSource"
