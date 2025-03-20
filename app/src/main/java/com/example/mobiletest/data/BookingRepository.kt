package com.example.mobiletest.data

import kotlinx.coroutines.flow.Flow

/**
 * Interface to the data layer.
 */
interface BookingRepository {

    fun getBookingsStream(): Flow<List<Booking>>

    suspend fun getBookings(forceUpdate: Boolean = false): List<Booking>

    suspend fun refresh()

    fun getBookingStream(shipToken: String): Flow<Booking?>

    suspend fun getBooking(shipToken: String, forceUpdate: Boolean = false): Booking?

    suspend fun refreshBooking(shipToken: String)

    suspend fun insertBooking(newBooking: Booking)

    suspend fun updateBooking(newBooking: Booking)

    suspend fun deleteAllBookings()

    suspend fun deleteBooking(shipToken: String)
}
