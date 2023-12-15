package com.example.uas_ppapb_v2.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account_room")

data class AccountRoom (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "uid")
    val uid: String = "",
)