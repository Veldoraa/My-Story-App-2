package com.example.storyapp.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.storyapp.ui.login.LoginActivity
import com.example.storyapp.data.Repo.Result
import com.example.storyapp.utils.ViewModelFactory
import com.example.storyapp.utils.gone
import com.example.storyapp.utils.visible
import com.example.storyapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val factory = ViewModelFactory.getInstance(this)
        val registerVm: RegisterViewModels by viewModels { factory }
        val buttonRegister = binding.btnRegist
        playAnimation()

        buttonRegister.setOnClickListener {
            val name = binding.edName.text.toString().trim()
            val email = binding.edEmail.text.toString().trim()
            val password = binding.edPasword.text.toString().trim()
            registerVm.registerUser(
                name,
                email,
                password
            ).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            binding.regisProgressBar.visible()
                        }
                        is Result.Success -> {
                            binding.regisProgressBar.gone()
                            Toast.makeText(
                                this,
                                "register success , silahkan login ${result.data.massage}",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        }
                        is Result.Error -> {
                            binding.regisProgressBar.gone()
                            Toast.makeText(
                                this,
                                "failed register ${result.error}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

            }

        }

    }

    private fun playAnimation() {
        val image =
            ObjectAnimator.ofFloat(binding.imageRegist, View.ALPHA, 1f).setDuration(500)
        val tvJudul =
            ObjectAnimator.ofFloat(binding.tvJudul, View.ALPHA, 1f).setDuration(500)
        val tvDesc =
            ObjectAnimator.ofFloat(binding.tvDesc, View.ALPHA, 1f).setDuration(500)
        val tvName =
            ObjectAnimator.ofFloat(binding.inpName, View.ALPHA, 1f).setDuration(500)
        val tvEmail =
            ObjectAnimator.ofFloat(binding.inpRegisEmail, View.ALPHA, 1f).setDuration(500)
        val tvPass =
            ObjectAnimator.ofFloat(binding.inpRegisPassword, View.ALPHA, 1f).setDuration(500)
        val btnRegis =
            ObjectAnimator.ofFloat(binding.btnRegist, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                image, tvJudul, tvDesc, tvName, tvEmail, tvPass, btnRegis
            )
            startDelay = 500
        }.start()
    }

}