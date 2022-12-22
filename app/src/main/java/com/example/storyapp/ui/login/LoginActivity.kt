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
        val factory = ViewModelFactory.getInstance(this)
        val loginVm: LoginViewModels by viewModels { factory }
        val email = binding.edEmail.text.toString().trim()
        val password = binding.edPassword.text.toString().trim()
        loginVm.loginUser(email, password).observe(this) { result ->
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
            val intennt = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intennt)
        }
    }

    private fun playAnimation() {
        val img =
            ObjectAnimator.ofFloat(binding.imageLogin, View.ALPHA, 1f).setDuration(500)
        val text1 =
            ObjectAnimator.ofFloat(binding.textLogin1, View.ALPHA, 1f).setDuration(500)
        val text3 =
            ObjectAnimator.ofFloat(binding.textToRegist, View.ALPHA, 1f).setDuration(500)
        val email =
            ObjectAnimator.ofFloat(binding.inpEmail, View.ALPHA, 1f).setDuration(500)
        val pass =
            ObjectAnimator.ofFloat(binding.inpPassword, View.ALPHA, 1f).setDuration(500)
        val btnLogin =
            ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val signUp =
            ObjectAnimator.ofFloat(binding.register, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                img, text1, text3, email, pass, btnLogin, signUp
            )
            startDelay = 500
        }.start()
    }
}