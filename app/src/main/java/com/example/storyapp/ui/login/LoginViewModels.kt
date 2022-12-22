package com.example.storyapp.ui.login

import androidx.lifecycle.ViewModel
import com.example.storyapp.data.Repo.Repository

class LoginViewModels(private val repository: Repository) : ViewModel() {
    fun loginUser(email: String, password: String) = repository.login(email,password)
}