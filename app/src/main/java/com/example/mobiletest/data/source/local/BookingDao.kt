/*
 * Copyright 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.mobiletest.data.source.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow


@Dao
interface BookingDao {


    @Query("SELECT * FROM booking")
    fun observeAll(): Flow<List<LocalBooking>>


    @Query("SELECT * FROM booking WHERE shipToken = :shipToken")
    fun observeByShipToken(shipToken: String): Flow<LocalBooking>


    @Query("SELECT * FROM booking")
    suspend fun getAll(): List<LocalBooking>


    @Query("SELECT * FROM booking WHERE shipToken = :shipToken")
    suspend fun getByShipToken(shipToken: String): LocalBooking?


    @Upsert
    suspend fun upsert(booking: LocalBooking)


    @Upsert
    suspend fun upsertAll(bookings: List<LocalBooking>)


    @Query("DELETE FROM booking WHERE shipToken = :shipToken")
    suspend fun deleteByShipToken(shipToken: String): Int


    @Query("DELETE FROM booking")
    suspend fun deleteAll()


}
