package com.example.mobiletest.di

import android.content.Context
import androidx.room.Room
import com.example.mobiletest.data.BookingRepository
import com.example.mobiletest.data.DefaultBookingRepository
import com.example.mobiletest.data.source.local.BookingDao
import com.example.mobiletest.data.source.local.BookingDatabase
import com.example.mobiletest.data.source.network.BookingNetworkDataSource
import com.example.mobiletest.data.source.network.NetworkDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindBookingRepository(repository: DefaultBookingRepository): BookingRepository
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Singleton
    @Binds
    abstract fun bindNetworkDataSource(dataSource: BookingNetworkDataSource): NetworkDataSource
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): BookingDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            BookingDatabase::class.java,
            "Booking.db"
        ).build()
    }

    @Provides
    fun provideBookingDao(database: BookingDatabase): BookingDao = database.bookingDao()
}
