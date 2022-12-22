package com.example.storyapp.data.Repo

import android.content.Context
import com.example.storyapp.data.api.ApiConfig
import com.example.storyapp.data.local.LiveDB

object Injection {
    fun provideRepository(context : Context) : Repository {
        val apiService = ApiConfig.getApiService()
        val database = LiveDB.getInstance(context)
        return Repository.getInstance(apiService, database)
    }
}