package com.example.storyapp.data.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse (
    @field: SerializedName("error")
    val error :Boolean,

    @field: SerializedName("massage")
    val massage : String
    )

data class LoginResult(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("loginResult")
    val loginResult: LoginData

)

data class LoginData(
    @field:SerializedName("userId")
    val userId: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("token")
    val token: String
)

data class UploadDataResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)