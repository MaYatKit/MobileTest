package com.example.mobiletest.data.source.network


data class NetworkBooking(
    val shipToken: String,
    var shipReference: String,
    var canIssueTicketChecking: Boolean,
    var expiryTime: String,
    var duration: Int,
    val priority: Int? = null,
    val status: TaskStatus = TaskStatus.ACTIVE
)

enum class TaskStatus {
    ACTIVE,
    COMPLETE
}
