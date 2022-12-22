package com.example.storyapp.ui.mainstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.data.Repo.Repository
import com.example.storyapp.data.local.Entity

class MainViewModel (private val repository: Repository) : ViewModel() {

    val storyList: LiveData<PagingData<Entity>> =
        repository.getStoryList().cachedIn(viewModelScope)

}