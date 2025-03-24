package com.example.mobiletest.data

import com.example.mobiletest.data.source.local.LocalBooking
import com.example.mobiletest.data.source.local.LocalBookingSegment
import com.example.mobiletest.data.source.local.LocalOriginAndDestinationPair
import com.example.mobiletest.data.source.local.LocalPlace
import com.example.mobiletest.data.source.network.NetworkBooking
import com.example.mobiletest.data.source.network.NetworkBookingSegment
import com.example.mobiletest.data.source.network.NetworkOriginAndDestinationPair
import com.example.mobiletest.data.source.network.NetworkPlace
import kotlin.collections.map

/**
 * Data model mapping extension functions. There are three model types:
 *
 * - Booking: External model exposed to other layers in the architecture.
 * Obtained using `toExternal`.
 *
 * - NetworkBooking: Internal model used to represent a task from the network. Obtained using
 * `toNetwork`.
 *
 * - LocalBooking: Internal model used to represent a task stored locally in a database. Obtained
 * using `toLocal`.
 *
 */

fun Place.toLocal() = LocalPlace(
    code = code,
    displayName = displayName,
    url = url
)

fun NetworkPlace.toLocal() = LocalPlace(
    code = code,
    displayName = displayName,
    url = url
)


fun LocalPlace.toExternal() = Place(
    code = code,
    displayName = displayName,
    url = url
)

fun OriginAndDestinationPair.toLocal() = LocalOriginAndDestinationPair(
    destination = destination.toLocal(),
    destinationCity = destinationCity,
    origin = origin.toLocal(),
    originCity = originCity
)

fun LocalOriginAndDestinationPair.toExternal() = OriginAndDestinationPair(
    destination = destination.toExternal(),
    destinationCity = destinationCity,
    origin = origin.toExternal(),
    originCity = originCity
)

fun NetworkOriginAndDestinationPair.toLocal() = LocalOriginAndDestinationPair(
    destination = destination.toLocal(),
    destinationCity = destinationCity,
    origin = origin.toLocal(),
    originCity = originCity
)

fun BookingSegment.toLocal() = LocalBookingSegment(
    id = id,
    originAndDestinationPair = originAndDestinationPair.toLocal()
)
@JvmName("externalSegmentToLocalSegment")
fun List<BookingSegment>.toLocal() = map(BookingSegment::toLocal)

fun LocalBookingSegment.toExternal() = BookingSegment(
    id = id,
    originAndDestinationPair = originAndDestinationPair.toExternal()
)
@JvmName("localSegmentToNetworkSegment")
fun List<LocalBookingSegment>.toExternal() = map(LocalBookingSegment::toExternal)

fun NetworkBookingSegment.toLocal() = LocalBookingSegment(
    id = id,
    originAndDestinationPair = originAndDestinationPair.toLocal()
)
@JvmName("networkSegmentToLocalSegment")
fun List<NetworkBookingSegment>.toLocal() = map(NetworkBookingSegment::toLocal)

// External to local
fun Booking.toLocal() = LocalBooking(
    shipToken = shipToken,
    shipReference = shipReference,
    canIssueTicketChecking = canIssueTicketChecking,
    expiryTime = expiryTime,
    duration = duration,
    segments = segments.toLocal()
)
@JvmName("externalToLocal")
fun List<Booking>.toLocal() = map(Booking::toLocal)

// Local to External
fun LocalBooking.toExternal() = Booking(
    shipToken = shipToken,
    shipReference = shipReference,
    canIssueTicketChecking = canIssueTicketChecking,
    expiryTime = expiryTime,
    duration = duration,
    segments = segments.toExternal()
)

// Note: JvmName is used to provide a unique name for each extension function with the same name.
// Without this, type erasure will cause compiler errors because these methods will have the same
// signature on the JVM.
@JvmName("localToExternal")
fun List<LocalBooking>.toExternal() = map(LocalBooking::toExternal)


// Network to Local
fun NetworkBooking.toLocal() = LocalBooking(
    shipToken = shipToken,
    shipReference = shipReference,
    canIssueTicketChecking = canIssueTicketChecking,
    expiryTime = expiryTime,
    duration = duration,
    segments = segments.toLocal()
)

@JvmName("networkToLocal")
fun List<NetworkBooking>.toLocal() = map(NetworkBooking::toLocal)


// Network to External
fun NetworkBooking.toExternal() = toLocal().toExternal()

@JvmName("networkToExternal")
fun List<NetworkBooking>.toExternal() = map(NetworkBooking::toExternal)

