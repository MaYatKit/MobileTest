package com.example.mobiletest.data.source.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class BookingSegmentConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromLocalBookingSegmentList(segments: List<LocalBookingSegment>?): String? {
        return gson.toJson(segments)
    }

    @TypeConverter
    fun toLocalBookingSegmentList(data: String?): List<LocalBookingSegment> {
        if (data == null) return emptyList()
        val listType = object : TypeToken<List<LocalBookingSegment>>() {}.type
        return gson.fromJson(data, listType)
    }



}