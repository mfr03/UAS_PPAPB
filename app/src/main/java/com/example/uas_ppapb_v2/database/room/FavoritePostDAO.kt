package com.example.uas_ppapb_v2.database.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.uas_ppapb_v2.database.model.FavoritePost

@Dao
interface FavoritePostDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFavorite(favorite: FavoritePost)

    @Delete
    fun deleteFavorite(favorite: FavoritePost)

    @Query("SELECT * FROM favorite_post WHERE userUID = :userUID")
    fun getAllFavorites(userUID: String): LiveData<List<FavoritePost>>
}