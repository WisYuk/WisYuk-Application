package com.wisyuk.ui.preference


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.wisyuk.R
import com.wisyuk.databinding.ActivityPreferenceBinding
import com.wisyuk.ui.ViewModelFactory
import com.wisyuk.ui.home.MainActivity
import com.wisyuk.ui.login.LoginActivity
import kotlin.properties.Delegates

class PreferenceActivity : AppCompatActivity() {
    // TODO when preferences had been asked once, doesn't need to come to this act again

    private lateinit var binding: ActivityPreferenceBinding
    private val viewModel by viewModels<PreferenceViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var prefItems = emptyList<String>()
    private var errorMessage = ""
    private var itemPos1 = -1
    private var itemPos2 = -1
    private var itemPos3 = -1
    private var userId by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                userId = user.id
            }
        }

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



        viewModel.preferences.observe(this) { pref ->
            prefItems = pref.map { it.name }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, prefItems)
            binding.spinnerFirst.adapter = adapter
            binding.spinnerSecond.adapter = adapter
            binding.spinnerThird.adapter = adapter

            binding.spinnerFirst.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    itemPos1 = position
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    itemPos1 = -1
                }
            }

            binding.spinnerSecond.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    itemPos2 = position
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    itemPos2 = -1
                }
            }

            binding.spinnerThird.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    itemPos3 = position
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    itemPos3 = -1
                }
            }
        }
        binding.continueButton.setOnClickListener {
            val prefs = listOf(
                itemPos1,
                itemPos2,
                itemPos3
            )

            viewModel.addPreferences(userId, prefs)
        }
    }

    private fun observerViewModel() {
        viewModel.addResponse.observe(this) {
            val intent = Intent(this@PreferenceActivity, MainActivity::class.java)
            Toast.makeText(this, getString(R.string.preference_added), Toast.LENGTH_SHORT).show()
            startActivity(intent)
            finish()
        }
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