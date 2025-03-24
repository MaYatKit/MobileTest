package com.example.mobiletest.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobiletest.R
import com.example.mobiletest.data.Booking
import com.example.mobiletest.data.BookingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BookingsUiState(
    val items: List<Booking> = emptyList(),
    val isLoading: Boolean = false,
    val loadingError: Int? = null
)

/**
 * ViewModel for the task list screen.
 */
@HiltViewModel
class BookingViewModel @Inject constructor(
    private val bookingRepository: BookingRepository) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)

    private val _uiState = MutableStateFlow(BookingsUiState())
    val uiState: StateFlow<BookingsUiState> = _uiState

    init {
        refreshBookings(forceUpdate = false)
    }

    fun refreshBookings(forceUpdate: Boolean = false) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val bookings = bookingRepository.getBookings(forceUpdate)
                _uiState.value = _uiState.value.copy(
                    items = bookings,
                    isLoading = false,
                    loadingError = null
                )
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    loadingError = R.string.loading_task_error
                )
            }
        }
    }

    fun refresh() {
        _isLoading.value = true
        viewModelScope.launch {
            refreshBookings(forceUpdate = true)
            _isLoading.value = false
        }
    }

}


