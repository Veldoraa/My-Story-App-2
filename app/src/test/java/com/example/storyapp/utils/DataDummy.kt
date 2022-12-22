package com.example.storyapp.utils

import com.example.storyapp.data.response.*
import com.example.storyapp.data.local.Entity

object DataDummy {
    fun generateDummyUserLocation(): List<StoryResponseItem> {
        val markerList = ArrayList<StoryResponseItem>()
        for (i in 0..10) {
            val marker = StoryResponseItem(
                "1",
                "Yuansyah Hardian",
                "AndroidTester",
                "https://www.google.com",
                "22-12-22",
                -5.3965658,
                105.2482376


            )
            markerList.add(marker)
        }
        return markerList
    }

    fun generateDummyLoginResponse(): LoginResult {
        return LoginResult(
            false,
            "Success",
            generateDummyLoginData()
        )
    }

    fun generateDummyRegisterReponse(): RegisterResponse {
        return RegisterResponse(
            false,
            "Success",
        )
    }

    private fun generateDummyLoginData(): LoginData {
        return LoginData(
            "1",
            "Yuansyah Hardian",
            "Bearer",
        )
    }

    fun generateUploadDataResponse(): UploadDataResponse {
        return UploadDataResponse(
            false,
            "success"
        )
    }

    fun generateDummyStoryResponse(): List<Entity> {
        val items: MutableList<Entity> = arrayListOf()
        for (i in 0..100) {
            val story = Entity(
                i.toString(),
                "name + $i",
                "description $i",
                "www.google.com",
                "22/12/22"
            )
            items.add(story)
        }
        return items
    }
}