package com.example.storyapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.storyapp.data.remote.RemoteKey
import com.example.storyapp.data.remote.RemoteKeyDao

@Database(entities = [Entity::class, RemoteKey::class], version = 2, exportSchema = false)
abstract class LiveDB : RoomDatabase(){

    abstract fun storyDao(): MemberDao
    abstract fun remoteKeysDao(): RemoteKeyDao
    companion object {
        @Volatile
        private var instance: LiveDB? = null
        fun getInstance(context: Context): LiveDB =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    LiveDB::class.java, "story.db"
                ).fallbackToDestructiveMigration()
                    .build()
                    .also { instance = it }
            }
    }
}