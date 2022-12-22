package com.example.storyapp.data.local

import android.database.Cursor
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MemberDao {
    @Query("SELECT * FROM member_story ORDER BY createdAt DESC")
    fun getStory(): PagingSource<Int, Entity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStory(story: List<Entity>)

    @Query("SELECT * FROM member_story")
    fun getAllStory(): Cursor

    @Query("DELETE FROM member_story")
    suspend fun deleteAll()
}