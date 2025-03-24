package com.example.mobiletest.data


data class Booking(
    val shipReference: String,
    val shipToken: String,
    val canIssueTicketChecking: Boolean,
    val expiryTime: String,
    val duration: Int,
    val segments: List<BookingSegment>
)

data class BookingSegment(
    val id: Int,
    val originAndDestinationPair: OriginAndDestinationPair
)

data class OriginAndDestinationPair(
    val destination: Place,
    val destinationCity: String,
    val origin: Place,
    val originCity: String
)

data class Place(
    val code: String,
    val displayName: String,
    val url: String
)
