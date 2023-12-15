package com.example.uas_ppapb_v2.database.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.uas_ppapb_v2.database.model.AccountRoom

@Dao
interface AccountRoomDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAccountRoom(accountRoom: AccountRoom)

    @Query("SELECT * FROM account_room WHERE uid = :uid")
    fun getAccountRoom(uid: String): AccountRoom
}