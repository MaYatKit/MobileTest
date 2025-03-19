package com.example.mobiletest.data

import com.example.mobiletest.data.source.local.BookingDao
import com.example.mobiletest.data.source.network.NetworkDataSource
import com.example.mobiletest.di.ApplicationScope
import com.example.mobiletest.di.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DefaultBookingRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val localDataSource: BookingDao,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
    @ApplicationScope private val scope: CoroutineScope,
) : BookingRepository {
    override fun getBookingsStream(): Flow<List<Booking>> {
        TODO("Not yet implemented")
    }

    override suspend fun getBookings(forceUpdate: Boolean): List<Booking> {
        TODO("Not yet implemented")
    }

    override suspend fun refresh() {
        TODO("Not yet implemented")
    }

    override fun getBookingStream(shipToken: String): Flow<Booking?> {
        TODO("Not yet implemented")
    }

    override suspend fun getBooking(
        shipToken: String,
        forceUpdate: Boolean
    ): Booking? {
        TODO("Not yet implemented")
    }

    override suspend fun refreshBooking(shipToken: String) {
        TODO("Not yet implemented")
    }

    override suspend fun createBooking(
        shipToken: String,
        shipReference: String,
        canIssueTicketChecking: Boolean,
        expiryTime: String,
        duration: Int
    ): String {
        TODO("Not yet implemented")
    }

    override suspend fun updateBooking(shipToken: String, expiryTime: String) {
        TODO("Not yet implemented")
    }

    override suspend fun completeBooking(shipToken: String) {
        TODO("Not yet implemented")
    }

    override suspend fun activateBooking(shipToken: String) {
        TODO("Not yet implemented")
    }

    override suspend fun clearCompletedBookings() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllBookings() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteBooking(shipToken: String) {
        TODO("Not yet implemented")
    }


}
