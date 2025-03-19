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

    suspend fun createBooking(
        shipToken: String, shipReference: String,
        canIssueTicketChecking: Boolean,
        expiryTime: String,
        duration: Int,
    ): String

    suspend fun updateBooking(shipToken: String, expiryTime: String)

    suspend fun completeBooking(shipToken: String)

    suspend fun activateBooking(shipToken: String)

    suspend fun clearCompletedBookings()

    suspend fun deleteAllBookings()

    suspend fun deleteBooking(shipToken: String)
}
