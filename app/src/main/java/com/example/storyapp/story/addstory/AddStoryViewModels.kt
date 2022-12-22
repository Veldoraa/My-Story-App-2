package com.example.storyapp.story.addstory

import androidx.lifecycle.ViewModel
import com.example.storyapp.data.Repo.Repository
import java.io.File

class AddStoryViewModels (private val repository: Repository): ViewModel() {
    fun uploadStory(token:String ,description: String,file: File) = repository.uploadLiveStory(token,description,file)

}