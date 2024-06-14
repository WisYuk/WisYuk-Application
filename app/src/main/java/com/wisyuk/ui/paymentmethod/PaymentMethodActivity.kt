package com.wisyuk.ui.paymentmethod

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.wisyuk.R
import com.wisyuk.data.response.PaymentMethodItem
import com.wisyuk.databinding.ActivityPaymentMethodBinding
import com.wisyuk.ui.PaymentMethodAdapter
import com.wisyuk.ui.ViewModelFactory
import com.wisyuk.ui.login.LoginActivity

class PaymentMethodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentMethodBinding
    private val viewModel by viewModels<PaymentMethodViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var errorMessage: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentMethodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupRecyclerView()
        observeViewModel()

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                viewModel.fetchPaymentMethod()
            }
        }
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

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvPay.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvPay.addItemDecoration(itemDecoration)
    }

    private fun setData(data: List<PaymentMethodItem>) {
        val adapter = PaymentMethodAdapter(this)
        adapter.submitList(data)
        binding.rvPay.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.paymentMethod.observe(this) {
            Log.d("Tes", it.toString())
            if (it != null) setData(it)
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

    companion object {
        const val PAYMENT_METHOD_ID = "payment_method_id"
        const val PAYMENT_METHOD_NAME = "payment_method_name"
        const val RESULT_CODE = 110
    }
}
