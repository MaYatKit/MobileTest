package com.example.mobiletest.data


data class Booking(
    val shipToken: String,
    var shipReference: String,
    var canIssueTicketChecking: Boolean,
    var expiryTime: String,
    var duration: Int,
) {


}
