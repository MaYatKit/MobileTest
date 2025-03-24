package com.example.mobiletest.data

import com.example.mobiletest.data.source.local.BookingDao
import com.example.mobiletest.data.source.network.NetworkDataSource
import com.example.mobiletest.di.ApplicationScope
import com.example.mobiletest.di.DefaultDispatcher
import com.example.mobiletest.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Concrete implementation to load tasks from the data sources into a cache.
 */
@Singleton
class DefaultBookingRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val localDataSource: BookingDao,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @ApplicationScope private val scope: CoroutineScope,
) : BookingRepository {


    /**
     * Get the tasks from the local data source, return as a flow
     */
    override fun getBookingsStream(): Flow<List<Booking>> {
        return localDataSource.observeAllWithLog().map { bookings ->
            withContext(dispatcher) {
                val olds = bookings.toExternal()
                if (olds.isNotEmpty()) {
                    val result = mutableListOf<Booking>()
                    olds.forEach { old ->
                        if (old.expiryTime.toLong() < System.currentTimeMillis()) {
                            val remoteBooking = networkDataSource.loadBookingByShipToken(old.shipToken)
                            if (remoteBooking != null) {
                                if (remoteBooking.shipToken == old.shipToken) {
                                    updateBooking(remoteBooking.toExternal())
                                }else {
                                    localDataSource.deleteByShipTokenWithLog(old.shipToken)
                                    localDataSource.upsertWithLog(remoteBooking.toLocal())
                                }
                                result.add(remoteBooking.toExternal())
                            }
                        }else {
                            result.add(old)
                        }
                    }
                    result
                }else {
                   val remoteBookings = networkDataSource.loadBookings()
                    localDataSource.upsertAll(remoteBookings.toLocal())
                    remoteBookings.toExternal()
                }
            }
        }
    }

    /**
     * Get the tasks from the local data source, blocking the current thread
     *
     */
    override suspend fun getBookings(forceUpdate: Boolean): List<Booking> {
        val result = scope.async(Dispatchers.IO) {
            val olds = localDataSource.getAllWithLog().toExternal()
            if (olds.isNotEmpty()) {
                val result = mutableListOf<Booking>()
                olds.forEach { old ->
                    if (old.expiryTime.toLong() < System.currentTimeMillis() || forceUpdate) {
                        val remoteBooking = networkDataSource.loadBookingByShipToken(old.shipToken)
                        if (remoteBooking != null) {
                            if (remoteBooking.shipToken == old.shipToken) {
                                updateBooking(remoteBooking.toExternal())
                            }else {
                                localDataSource.deleteByShipTokenWithLog(old.shipToken)
                                localDataSource.upsertWithLog(remoteBooking.toLocal())
                            }
                            result.add(remoteBooking.toExternal())
                        }
                    }else {
                        result.add(old)
                    }
                }
                return@async result
            }else {
                val remoteBookings = networkDataSource.loadBookings()
                localDataSource.upsertAll(remoteBookings.toLocal())
                return@async remoteBookings.toExternal()
            }
        }
        return result.await()
    }

    /**
     * Refresh the tasks from the network and replace the existing ones in the database
     */
    override suspend fun refresh() {
        withContext(ioDispatcher) {
            val remoteBookings = networkDataSource.loadBookings()
            localDataSource.deleteAllWithLog()
            localDataSource.upsertAll(remoteBookings.toLocal())
        }
    }

    /**
     * Get the tasks from the local data source, blocking the current thread
     */
    override fun getBookingStream(shipToken: String): Flow<Booking?> {
        return localDataSource.observeByShipToken(shipToken).map { it.toExternal() }
    }

    /**
     * Get the tasks from the local data source, blocking the current thread
     */
    override suspend fun getBooking(
        shipToken: String,
        forceUpdate: Boolean
    ): Booking? {
        if (forceUpdate) {
            refresh()
        }
        return localDataSource.getByShipToken(shipToken)?.toExternal()
    }

    /**
     * Refresh the tasks from the network and replace the existing ones in the database
     */
    override suspend fun refreshBooking(shipToken: String) {
        refresh()
    }

    /**
     * Insert or update a task in the database.
     */
    override suspend fun insertBooking(newBooking: Booking) {
        localDataSource.upsertWithLog(newBooking.toLocal())
    }

    /**
     * Update the task in the local data source
     */
    override suspend fun updateBooking(newBooking: Booking) {
        val booking = getBooking(newBooking.shipToken)?.copy(
            shipReference = newBooking.shipReference,
            canIssueTicketChecking = newBooking.canIssueTicketChecking,
            expiryTime = newBooking.expiryTime,
            duration = newBooking.duration,
        ) ?: throw Exception("Booking (shipToken ${newBooking.shipToken}) " +
                "not found, please insert it")

        localDataSource.upsertWithLog(booking.toLocal())
    }


    /**
     * Delete all tasks in the database
     */
    override suspend fun deleteAllBookings() {
        localDataSource.deleteAllWithLog()
    }

    /**
     * Delete the task by id from the database
     */
    override suspend fun deleteBooking(shipToken: String) {
        localDataSource.deleteByShipTokenWithLog(shipToken)
    }


}
