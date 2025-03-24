package com.example.mobiletest.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters


@Entity(tableName = "booking")
@TypeConverters(BookingSegmentConverter::class)
data class LocalBooking(
    @PrimaryKey val shipToken: String,
    var shipReference: String,
    var canIssueTicketChecking: Boolean,
    var expiryTime: String,
    var duration: Int,
    val segments: List<LocalBookingSegment>
)


data class LocalBookingSegment(
    val id: Int,
    val originAndDestinationPair: LocalOriginAndDestinationPair
)


data class LocalOriginAndDestinationPair(
    val destination: LocalPlace,
    val destinationCity: String,
    val origin: LocalPlace,
    val originCity: String
)

data class LocalPlace(
    val code: String,
    val displayName: String,
    val url: String
)
