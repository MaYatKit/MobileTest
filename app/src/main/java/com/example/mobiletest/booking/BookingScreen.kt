package com.example.mobiletest.booking

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobiletest.R
import com.example.mobiletest.data.Booking
import com.example.mobiletest.data.BookingSegment
import com.example.mobiletest.ui.theme.MobileTestTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    modifier: Modifier = Modifier,
    viewModel: BookingViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        val state = rememberPullToRefreshState()

        BookingContent(
            loading = uiState.isLoading,
            bookings = uiState.items,
            onRefresh = viewModel::refresh,
            pullToRefreshState = state,
            modifier = Modifier.padding(paddingValues)
        )

        uiState.loadingError?.let { error ->
            val message = stringResource(error)
            LaunchedEffect(snackbarHostState, viewModel, message) {
                snackbarHostState.showSnackbar(message)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookingContent(
    loading: Boolean,
    bookings: List<Booking>,
    onRefresh: () -> Unit,
    pullToRefreshState: PullToRefreshState,
    modifier: Modifier = Modifier
) {
    PullToRefreshBox(
        modifier = modifier.fillMaxSize()
            .padding(horizontal = dimensionResource(id = R.dimen.horizontal_margin)),
        onRefresh = onRefresh,
        isRefreshing = loading,
        state = pullToRefreshState
    ) {
        Log.d("loading = ", loading.toString())
        if (bookings.isEmpty()) {
            BookingsEmptyContent(modifier = modifier
                .fillMaxSize().verticalScroll(rememberScrollState())
                .padding(horizontal = dimensionResource(id = R.dimen.horizontal_margin)),
                noBookingsLabel = R.string.no_data,
                noBookingsIconRes = android.R.drawable.ic_menu_help)
        }else {
            val booking = bookings[0]
            Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
                Text(
                    text = "Ship Reference: ${booking.shipReference}",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Ship Token: ${booking.shipToken}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Expiry Time: ${booking.expiryTime}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Duration: ${booking.duration} mins",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))
                // Segment List Section
                Text(
                    text = "Segments",
                    style = MaterialTheme.typography.titleLarge
                )
                if (booking.segments.isNotEmpty()) {
                    SegmentList(segments = booking.segments)
                } else {
                    Text(
                        text = "No segments available",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}


@Composable
fun SegmentList(segments: List<BookingSegment>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxWidth()) {
        items(segments) { segment ->
            SegmentItem(segment = segment, modifier = modifier)
        }
    }
}

@Composable
fun SegmentItem(segment: BookingSegment, modifier: Modifier = Modifier) {
    val pair = segment.originAndDestinationPair
    Column(modifier = modifier.fillMaxWidth().padding(8.dp)) {
        Text(text = "Segment ID: ${segment.id}", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Origin: ${pair.origin.displayName} (${pair.origin.code}) - ${pair.originCity}",
            style = MaterialTheme.typography.bodyMedium)
        Text(text = "Destination: ${pair.destination.displayName} (${pair.destination.code}) - ${pair.destinationCity}",
            style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider()
    }
}

@Composable
private fun BookingsEmptyContent(
    @StringRes noBookingsLabel: Int,
    @DrawableRes noBookingsIconRes: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.wrapContentSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = noBookingsIconRes),
            contentDescription = stringResource(R.string.no_data),
            modifier = Modifier.size(96.dp)
        )
        Text(stringResource(id = noBookingsLabel))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun BookingsContentPreview() {
    MaterialTheme {
        Surface {
            BookingContent(
                loading = false,
                onRefresh = { },
                pullToRefreshState = rememberPullToRefreshState(),
                bookings = listOf(
                    Booking(
                        shipToken = "ship Token 1",
                        shipReference = "ship Reference 1",
                        canIssueTicketChecking = true,
                        expiryTime = "10pm",
                        duration = 10,
                        segments = emptyList()
                    )
                ),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun BookingsContentEmptyPreview() {
    MaterialTheme {
        Surface {
            BookingContent(
                loading = false,
                bookings = emptyList(),
                onRefresh = { },
                pullToRefreshState = rememberPullToRefreshState(),
            )
        }
    }
}

@Preview
@Composable
private fun BookingsEmptyContentPreview() {
    MobileTestTheme {
        Surface {
            BookingsEmptyContent(
                noBookingsLabel = R.string.no_data,
                noBookingsIconRes = android.R.drawable.ic_secure
            )
        }
    }
}


