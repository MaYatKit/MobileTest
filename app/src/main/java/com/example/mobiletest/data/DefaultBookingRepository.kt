package com.example.mobiletest.data

import com.example.mobiletest.data.source.local.BookingDao
import com.example.mobiletest.data.source.network.NetworkDataSource
import com.example.mobiletest.di.ApplicationScope
import com.example.mobiletest.di.DefaultDispatcher
import com.example.mobiletest.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DefaultBookingRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val localDataSource: BookingDao,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @ApplicationScope private val scope: CoroutineScope,
) : BookingRepository {


    override fun getBookingsStream(): Flow<List<Booking>> {
        return localDataSource.observeAll().map { bookings ->
            withContext(dispatcher) {
                bookings.toExternal()
            }
        }
    }

    override suspend fun getBookings(forceUpdate: Boolean): List<Booking> {
        if (forceUpdate) {
            refresh()
        }
        return withContext(dispatcher) {
            localDataSource.getAll().toExternal()
        }
    }

    override suspend fun refresh() {
        withContext(ioDispatcher) {
            val remoteBookings = networkDataSource.loadBookings()
            localDataSource.deleteAll()
            localDataSource.upsertAll(remoteBookings.toLocal())
        }
    }

    override fun getBookingStream(shipToken: String): Flow<Booking?> {
        return localDataSource.observeByShipToken(shipToken).map { it.toExternal() }
    }

    override suspend fun getBooking(
        shipToken: String,
        forceUpdate: Boolean
    ): Booking? {
        if (forceUpdate) {
            refresh()
        }
        return localDataSource.getByShipToken(shipToken)?.toExternal()
    }

    override suspend fun refreshBooking(shipToken: String) {
        refresh()
    }

    override suspend fun insertBooking(newBooking: Booking) {
        localDataSource.upsert(newBooking.toLocal())
    }

    override suspend fun updateBooking(newBooking: Booking) {
        val booking = getBooking(newBooking.shipToken)?.copy(
            shipReference = newBooking.shipReference,
            canIssueTicketChecking = newBooking.canIssueTicketChecking,
            expiryTime = newBooking.expiryTime,
            duration = newBooking.duration,
        ) ?: throw Exception("Booking (shipToken ${newBooking.shipToken}) " +
                "not found, please insert it")

        localDataSource.upsert(booking.toLocal())
    }


    override suspend fun deleteAllBookings() {
        localDataSource.deleteAll()
    }

    override suspend fun deleteBooking(shipToken: String) {
        localDataSource.deleteByShipToken(shipToken)
    }


}
