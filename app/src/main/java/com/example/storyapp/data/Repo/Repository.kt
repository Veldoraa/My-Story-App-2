package com.example.storyapp.data.Repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.example.storyapp.data.api.ApiService
import com.example.storyapp.data.response.LoginResult
import com.example.storyapp.data.response.RegisterResponse
import com.example.storyapp.data.response.StoryResponseItem
import com.example.storyapp.data.response.UploadDataResponse
import com.example.storyapp.data.local.Entity
import com.example.storyapp.data.local.LiveDB
import com.example.storyapp.data.remote.RemoteMediator
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class Repository private constructor(
    private val apiService: ApiService,
    private val database: LiveDB
    ) {

    fun uploadLiveStory(
        token: String,
        description: String,
        file: File
    ): LiveData<Result<UploadDataResponse>> = liveData {
        emit(Result.Loading)
        try {
            val desc = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )
            val response = apiService.uploadStory(
                "Bearer $token",
                desc,
                imageMultipart
            )
            if (response.error) {
                emit(Result.Error(response.message))

            } else {
                emit(Result.Success(response))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getListStoryByMaps(location: Int, token: String): LiveData<Result<List<StoryResponseItem>>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.getListStoryByLocation(location, "Bearer $token")
                if (response.error){
                    emit(Result.Error(response.message))
                }else{
                    emit(Result.Success(response.storyResponseItems))
                }
            } catch (e: java.lang.Exception) {
                Log.d("Signup", e.message.toString())
                emit(Result.Error(e.message.toString()))
            }
        }
    fun getStoryList(): LiveData<PagingData<Entity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = RemoteMediator(database, apiService),
            pagingSourceFactory = {
                this.database.storyDao().getStory()
            }
        ).liveData
    }

    fun register(
        name : String,
        email : String,
        password : String,): LiveData<Result<RegisterResponse>> = liveData {
            emit(Result.Loading)
        try {
            val responseRegis = apiService.userRegister(
                name,
                email,
                password,
            )
            if (responseRegis.error){
                emit(Result.Error(responseRegis.massage))
            }else {
                emit(Result.Success(responseRegis))
            }
        }catch (e : Exception){
            emit(Result.Error(e.message.toString()))
        }
    }

    fun login(
        email : String,
        password : String
    ): LiveData<Result<LoginResult>> = liveData {
        emit(Result.Loading)
        try {
            val responseLogin = apiService.userLogin(
                email,
                password
            )
            if (responseLogin.error){
                Log.d("error response", "true: ${responseLogin.error} ")
                emit(Result.Error(responseLogin.message))
            }else {
                Log.d("error response", "false: ${responseLogin.error} ")
                emit(Result.Success(responseLogin))
            }
    } catch (e : Exception){
            Log.d("StoryRepository", "error: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
            database: LiveDB
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService, database)
            }.also { instance = it }
    }
}