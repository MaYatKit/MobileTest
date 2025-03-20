package com.example.mobiletest.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobiletest.R
import com.example.mobiletest.data.Booking
import com.example.mobiletest.data.BookingRepository
import com.example.mobiletest.util.Async
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BookingsUiState(
    val items: List<Booking> = emptyList(),
    val isLoading: Boolean = false,
    val userMessage: Int? = null
)

/**
 * ViewModel for the task list screen.
 */
@HiltViewModel
class BookingViewModel @Inject constructor(
    private val bookingRepository: BookingRepository) : ViewModel() {

    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val _isLoading = MutableStateFlow(false)
    private val _filteredTasksAsync =
        bookingRepository.getBookingsStream().map { Async.Success(it) }
            .catch<Async<List<Booking>>> {
                emit(Async.Error(R.string.loading_task_error))
            }

    val uiState: StateFlow<BookingsUiState> = combine(
         _isLoading, _filteredTasksAsync, _userMessage
    ) { isLoading, tasksAsync, _userMessage ->
        when (tasksAsync) {
            Async.Loading -> {
                BookingsUiState(isLoading = true)
            }
            is Async.Error -> {
                BookingsUiState(userMessage = tasksAsync.errorMessage)
            }
            is Async.Success -> {
                BookingsUiState(
                    items = tasksAsync.data,
                    isLoading = isLoading,
                    userMessage = _userMessage
                )
            }
        }
    }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = BookingsUiState(isLoading = true)
        )


    fun snackbarMessageShown() {
        _userMessage.value = null
    }

    private fun showSnackbarMessage(message: Int) {
        _userMessage.value = message
    }

    fun refresh() {
        _isLoading.value = true
        viewModelScope.launch {
            bookingRepository.refresh()
            _isLoading.value = false
        }
    }



}


