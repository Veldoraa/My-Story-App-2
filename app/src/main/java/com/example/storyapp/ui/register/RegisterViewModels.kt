package com.example.storyapp.ui.register

import androidx.lifecycle.ViewModel
import com.example.storyapp.data.Repo.Repository

class RegisterViewModels(private val repository: Repository) : ViewModel() {
    fun registerUser(
        name: String,
        email: String,
        password: String,
    ) = repository.register(name , email, password)
}