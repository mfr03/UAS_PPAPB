package com.example.uas_ppapb_v2.database.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.uas_ppapb_v2.database.model.PlannedPost


@Dao
interface PlannedPostDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPlannedPost(plannedPost: PlannedPost)

    @Delete
    fun deletePlannedPost(plannedPost: PlannedPost)

    @Query("SELECT * FROM planned_post WHERE userUID = :userUID")
    fun getAllPlannedPosts(userUID: String): LiveData<List<PlannedPost>>
}