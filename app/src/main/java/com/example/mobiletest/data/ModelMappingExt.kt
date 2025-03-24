package com.example.mobiletest.data

import com.example.mobiletest.data.source.local.LocalBooking
import com.example.mobiletest.data.source.network.NetworkBooking
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


// External to local
fun Booking.toLocal() = LocalBooking(
    shipToken = shipToken,
    shipReference = shipReference,
    canIssueTicketChecking = canIssueTicketChecking,
    expiryTime = expiryTime,
    duration = duration,
)

fun List<Booking>.toLocal() = map(Booking::toLocal)

// Local to External
fun LocalBooking.toExternal() = Booking(
    shipToken = shipToken,
    shipReference = shipReference,
    canIssueTicketChecking = canIssueTicketChecking,
    expiryTime = expiryTime,
    duration = duration,
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
)

@JvmName("networkToLocal")
fun List<NetworkBooking>.toLocal() = map(NetworkBooking::toLocal)


// Network to External
fun NetworkBooking.toExternal() = toLocal().toExternal()

@JvmName("networkToExternal")
fun List<NetworkBooking>.toExternal() = map(NetworkBooking::toExternal)

