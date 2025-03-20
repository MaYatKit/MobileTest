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
)

