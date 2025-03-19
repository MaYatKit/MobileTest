package com.example.mobiletest.data.source.network

import javax.inject.Inject

class BookingNetworkDataSource @Inject constructor() : NetworkDataSource {
    override suspend fun loadBookings(): List<NetworkBooking> {
        TODO("Not yet implemented")
    }

    override suspend fun saveBookings(tasks: List<NetworkBooking>) {
        TODO("Not yet implemented")
    }


}