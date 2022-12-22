package com.example.storyapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.storyapp.data.Repo.Result
import com.example.storyapp.data.local.SharedPreferences
import com.example.storyapp.ui.mainstory.MainActivity
import com.example.storyapp.ui.register.RegisterActivity
import com.example.storyapp.utils.*
import com.example.storyapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPref: SharedPreferences

    private fun login() {
        val factoryModels = ViewModelFactory.getInstance(this)
        val loginVm: LoginViewModels by viewModels { factoryModels }
        val email = binding.edEmail.text.toString().trim()
        val pass = binding.edPassword.text.toString().trim()
        loginVm.loginUser(email, pass).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.loginProgressBar.visible()
                    }
                    is Result.Success -> {
                        binding.loginProgressBar.gone()
                        val IdUser = result.data.loginResult.userId
                        val name = result.data.loginResult.name
                        val token = result.data.loginResult.token

                        sharedPref = SharedPreferences(this)
                        sharedPref.saveDataUser(IdUser, name, token, true)
                        Intent(this@LoginActivity, MainActivity::class.java).also {
                            startActivity(it)
                            finish()
                        }
                    }
                    is Result.Error -> {
                        binding.loginProgressBar.gone()
                        Toast.makeText(
                            this,
                            "failed login" + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun editTextFilled() {
        binding.edEmail.onTextChanged { enableEditText() }
        binding.edPassword.onTextChanged { enableEditText() }

    }

    private fun enableEditText() {
        val email = binding.edEmail.text.toString().trim()
        val password = binding.edPassword.text.toString().trim()
        binding.btnLogin.isEnabled =
            email.isNotEmpty() && emailValid(email) && password.length > 6 && password.isNotEmpty()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()
        editTextFilled()

        binding.btnLogin.setOnClickListener {
            login()
        }
        binding.register.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun playAnimation() {
        val image =
            ObjectAnimator.ofFloat(binding.imageLogin, View.ALPHA, 1f).setDuration(500)
        val tvLogin =
            ObjectAnimator.ofFloat(binding.textLogin1, View.ALPHA, 1f).setDuration(500)
        val tvRegis =
            ObjectAnimator.ofFloat(binding.textToRegist, View.ALPHA, 1f).setDuration(500)
        val tvEmail =
            ObjectAnimator.ofFloat(binding.inpEmail, View.ALPHA, 1f).setDuration(500)
        val tvPass =
            ObjectAnimator.ofFloat(binding.inpPassword, View.ALPHA, 1f).setDuration(500)
        val btnLogin =
            ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val btnRegis =
            ObjectAnimator.ofFloat(binding.register, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                image, tvLogin, tvRegis, tvEmail, tvPass, btnLogin, btnRegis
            )
            startDelay = 500
        }.start()
    }
}