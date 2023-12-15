package com.example.uas_ppapb_v2.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.example.uas_ppapb_v2.database.model.AccountRoom
import com.example.uas_ppapb_v2.database.model.FavoritePost
import com.example.uas_ppapb_v2.database.model.PlannedPost


@Database(entities = [AccountRoom::class, FavoritePost::class, PlannedPost::class], version = 1, exportSchema = false)
abstract class UserDatabaseRoom: RoomDatabase() {

    abstract fun accountRoomDAO(): AccountRoomDAO?

    abstract fun favoritePostDAO(): FavoritePostDAO?

    abstract fun plannedPostDAO(): PlannedPostDAO?

    companion object {
        @Volatile
        private var INSTANCE: UserDatabaseRoom? = null

        fun getDatabase(context: Context): UserDatabaseRoom? {
            synchronized(UserDatabaseRoom::class.java) {
                if(INSTANCE == null) {
                    INSTANCE = databaseBuilder(
                        context.applicationContext,
                        UserDatabaseRoom::class.java, "user_database_room"
                    ).build()
                }
            }
            return INSTANCE
        }
    }

}