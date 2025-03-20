package com.example.mobiletest.data.source.network

/**
 * Main entry point for accessing tasks data from the network.
 *
 */
interface NetworkDataSource {

    suspend fun loadBookings(): List<NetworkBooking>

}
