package com.wisyuk.ui.paymentreceipt

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.wisyuk.databinding.ActivityPaymentReceiptBinding
import com.wisyuk.ui.ViewModelFactory
import com.wisyuk.ui.yourplan.detail_plan.DetailPlanActivity


class PaymentReceiptActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentReceiptBinding
    private val viewModel by viewModels<PaymentReceiptViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentReceiptBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val receiptId = intent.getIntExtra(DetailPlanActivity.RECEIPT_ID, -1)

        viewModel.fetchPaymentMethod(receiptId)
        setupView()
        setupAction()
        observerViewModel()
        animation()
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

    private fun setupAction(){
        binding.btBack.setOnClickListener(){
            finish()
        }
    }

    private fun observerViewModel() {
        viewModel.data.observe(this) {
            with(binding) {
                planName.text = it.hotelName

                hotelNameValue.text = it.hotelName
                hotelPriceValue.text = it.hotelPrice.toString()

                rideNameValue.text = it.rideName
                ridePriceValue.text = it.ridePrice.toString()

                guideNameValue.text = it.guideName
                guidePriceValue.text = it.tourGuidePrice.toString()

                totalPriceValue.text = it.paymentTotal.toString()
            }
        }
        viewModel.selectedPaymentMethod.observe(this){
            if (it != null) {
                binding.paymentMethod.text = it.name
            }
        }
    }

    private fun animation(){
        val successIcon = binding.successIcon
        val spinAnimation: Animation = AnimationUtils.loadAnimation(this, com.wisyuk.R.anim.spin)
        successIcon.startAnimation(spinAnimation)
    }
}