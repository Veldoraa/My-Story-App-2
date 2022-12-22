package com.example.storyapp.data.api

import com.example.storyapp.data.response.LoginResult
import com.example.storyapp.data.response.RegisterResponse
import com.example.storyapp.data.response.StoriesResponse
import com.example.storyapp.data.response.UploadDataResponse
import com.example.storyapp.data.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun userRegister(
        @Field("name") name : String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun userLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResult

    @GET("stories")
    suspend fun getAllListStory(
        @Header("Authorization") token: String,
    ): StoriesResponse

    @Multipart
    @POST("stories")
    suspend fun uploadStory(
        @Header("Authorization") token: String,
        @Part("description") description: RequestBody,
        @Part file: MultipartBody.Part
    ): UploadDataResponse

    @GET("stories")
    suspend fun getListStoryByLocation(
        @Query("location") loc: Int,
        @Header("Authorization") token: String,
    ): StoriesResponse

    @GET("stories")
    suspend fun getListStory(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): StoriesResponse

}