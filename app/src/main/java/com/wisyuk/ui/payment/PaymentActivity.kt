package com.wisyuk.ui.payment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.wisyuk.R
import com.wisyuk.databinding.ActivityLoginBinding
import com.wisyuk.databinding.ActivityPaymentBinding
import com.wisyuk.databinding.ActivitySignUpBinding
import com.wisyuk.ui.ViewModelFactory
import com.wisyuk.ui.login.LoginActivity
import com.wisyuk.ui.paymentmethod.PaymentMethodActivity
import com.wisyuk.ui.signup.SignUpViewModel

class PaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentBinding
    private val viewModel by viewModels<PaymentViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var errorMessage: String = ""
    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == PaymentMethodActivity.RESULT_CODE && result.data != null) {
            val selectedValueId =
                result.data?.getIntExtra(PaymentMethodActivity.PAYMENT_METHOD_ID, 0)
            val selectedValueName =
                result.data?.getStringExtra(PaymentMethodActivity.PAYMENT_METHOD_NAME)

            if (selectedValueName != null) setMethod(selectedValueName)
        }
    }

    private fun setMethod(selectedValue: String) {
        binding.btMethodChoose.text = selectedValue
        binding.btPayment.isEnabled = true
        binding.btPayment.setBackgroundColor(ContextCompat.getColor(this, R.color.md_theme_primary))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
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
        binding.btMethodChoose.setOnClickListener {
            val paymentMethodIntent =
                Intent(this@PaymentActivity, PaymentMethodActivity::class.java)
            resultLauncher.launch(paymentMethodIntent)
        }
        binding.btPayment.setOnClickListener {
            // TODO : When Data Available, Change this
            val userID = "1"
            val tourismID = "1"
            val hotelID = "1"
            val rideID = "1"
            val tourGuideID = "1"
            val goDate = "2024-09-08"
            val status = "1"
            val paymentMethodID = "1"

            viewModel.addPaidPlan(
                userID,
                tourismID,
                hotelID,
                rideID,
                tourGuideID,
                goDate,
                status,
                paymentMethodID
            )

            viewModel.paymentResponse.observe(this) {
                val intent = Intent(this@PaymentActivity, LoginActivity::class.java)
                startActivity(intent)
                Toast.makeText(this, getString(R.string.signup_success), Toast.LENGTH_SHORT).show()
                finish()
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