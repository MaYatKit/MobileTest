package com.example.mobiletest.data.source.network

import com.google.gson.annotations.SerializedName


data class NetworkBooking(
    @SerializedName("shipToken")
    val shipToken: String,
    @SerializedName("shipReference")
    var shipReference: String,
    @SerializedName("canIssueTicketChecking")
    var canIssueTicketChecking: Boolean,
    @SerializedName("expiryTime")
    var expiryTime: String,
    @SerializedName("duration")
    var duration: Int,
    @SerializedName("segments")
    val segments: List<NetworkBookingSegment>
)


data class NetworkBookingSegment(
    @SerializedName("id")
    val id: Int,

    @SerializedName("originAndDestinationPair")
    val originAndDestinationPair: NetworkOriginAndDestinationPair
)

data class NetworkOriginAndDestinationPair(
    @SerializedName("destination")
    val destination: NetworkPlace,

    @SerializedName("destinationCity")
    val destinationCity: String,

    @SerializedName("origin")
    val origin: NetworkPlace,

    @SerializedName("originCity")
    val originCity: String
)

data class NetworkPlace(
    @SerializedName("code")
    val code: String,

    @SerializedName("displayName")
    val displayName: String,

    @SerializedName("url")
    val url: String
)
