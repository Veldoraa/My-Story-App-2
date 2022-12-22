package com.example.storyapp.data.local

import android.content.Context
import android.content.SharedPreferences

class SharedPreferences (context: Context){
    private var preferences: SharedPreferences =
        context.getSharedPreferences("data_user", Context.MODE_PRIVATE)

    fun saveDataUser(userId: String, name: String, token: String, state: Boolean) {
        val uploadData = preferences.edit()
        uploadData.putString("userId", userId)
        uploadData.putString("name", name)
        uploadData.putString("token", token)
        uploadData.putBoolean("isLogin", state)
        uploadData.apply()
    }

    fun checkState(): Boolean {
        return preferences.getBoolean("isLogin", false)
    }

    fun getToken(): String {
        return preferences.getString("token",null).toString()
    }
}