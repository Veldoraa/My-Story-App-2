package com.example.storyapp.ui.map

import androidx.lifecycle.ViewModel
import com.example.storyapp.data.Repo.Repository

class MapViewModels(private val repository: Repository): ViewModel() {
    fun getStoryByMaps(location :Int, token:String) = repository.getListStoryByMaps(location,token)
}