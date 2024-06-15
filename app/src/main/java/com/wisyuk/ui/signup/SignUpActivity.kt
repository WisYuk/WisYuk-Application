package com.wisyuk.ui.signup

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.wisyuk.R
import com.wisyuk.databinding.ActivitySignUpBinding
import com.wisyuk.ui.ViewModelFactory
import com.wisyuk.ui.login.LoginActivity

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val viewModel by viewModels<SignUpViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var errorMessage: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        observerViewModel()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.signUpButton.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val isSubscribe = binding.promotionCheck.isChecked

            viewModel.postData(name, email, password, isSubscribe)

            viewModel.signUpResponse.observe(this) {
                val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                startActivity(intent)
                Toast.makeText(this, getString(R.string.signup_success), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observerViewModel() {
        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
        viewModel.isError.observe(this) {
            showError(it)
        }
        viewModel.message.observe(this) {
            if (it != null) {
                errorMessage = it
            }
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(isError: Boolean) {
        if (isError) {
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.oh_no_there_is_something_wrong))
                setMessage(errorMessage)
                setNegativeButton(getString(R.string.close)) { dialog, _ ->
                    dialog.dismiss()
                }
                create()
                show()
            }
        }
    }

}