package com.example.uas_ppapb_v2.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import javax.annotation.Nonnull


@Entity(
    tableName = "planned_post",
    foreignKeys = [
        ForeignKey(
            entity = AccountRoom::class,
            parentColumns = ["uid"],
            childColumns = ["userUID"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PlannedPost(
    @PrimaryKey(autoGenerate = false)
    @Nonnull
    val id: String = "",

    @ColumnInfo(name = "userUID")
    val userUID: String = "",

    @ColumnInfo(name= "destination")
    var destination: String = "",

    @ColumnInfo(name= "startingStation")
    var startingStation: String = "Mamba Mentality",

    @ColumnInfo(name= "endStation")
    var endStation: String = "Samba Mentality",

    @ColumnInfo(name= "price")
    var price: Int = 100,

    @ColumnInfo(name= "lengthOfStay")
    var lengthOfStay: Int = 3,

    @ColumnInfo(name= "overviewDescription")
    var overviewDescription: String = "lorem ipsum dolor sit amet consectetur adipiscing elit sed do eiusmod tempor incididunt ut labore et dolore magna aliqua",

    @ColumnInfo(name= "shortDescription")
    var shortDescription: String = "lorem ipsum dolor sit amet .. ",

    @ColumnInfo(name= "imageURI")
    var imageURI: String = "",

    @ColumnInfo(name= "tag")
    var tag: String = "",

    @ColumnInfo(name = "plannedDate")
    var plannedDate: String = "",

    @ColumnInfo(name = "plannedTime")
    var plannedTime: String = "",

    @ColumnInfo(name = "notificationTime")
    var notificationTime: String = "",

    @ColumnInfo(name = "notificationDate")
    var notificationDate: String = "",
)
