package com.example.mobiletest.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [LocalBooking::class], version = 3, exportSchema = false)
@TypeConverters(BookingSegmentConverter::class)
abstract class BookingDatabase : RoomDatabase() {

    abstract fun bookingDao(): BookingDao
}
