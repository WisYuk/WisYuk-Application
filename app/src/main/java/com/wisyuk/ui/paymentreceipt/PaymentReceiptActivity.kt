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
import com.wisyuk.R
import com.wisyuk.databinding.ActivityPaymentReceiptBinding
import com.wisyuk.ui.ViewModelFactory
import com.wisyuk.ui.yourplan.detail_plan.DetailPlanActivity


class PaymentReceiptActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentReceiptBinding
    private val viewModel by viewModels<PaymentReceiptViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var tourName = ""
    private var name = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentReceiptBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val receiptId = intent.getIntExtra(DetailPlanActivity.RECEIPT_ID, -1)
        tourName = intent.getStringExtra(DetailPlanActivity.PLAN_NAME).toString()
        name = intent.getStringExtra(DetailPlanActivity.EXTRA_NAME).toString()
        viewModel.getDetailTourism(receiptId)
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
        binding.btBack.setOnClickListener {
            finish()
        }
    }

    private fun observerViewModel() {
        viewModel.data.observe(this) {
            with(binding) {
                planName.text = tourName
                userName.text = name

                hotelNameValue.text = it.hotelName
                hotelPriceValue.text = getString(R.string.rp_format, it.hotelPrice.toFloat().toInt().toString())

                rideNameValue.text = it.rideName
                ridePriceValue.text = getString(R.string.rp_format, it.ridePrice.toString())

                guideNameValue.text = it.guideName
                guidePriceValue.text = getString(R.string.rp_format, it.tourGuidePrice.toString())

                totalPriceValue.text = getString(R.string.rp_format, it.paymentTotal.toString())
                paymentMethod.text = it.paymentMethodName
            }
        }
//        viewModel.selectedPaymentMethod.observe(this){
//            if (it != null) {
//                binding.paymentMethod.text = it.name
//            }
//        }
    }

    private fun animation(){
        val successIcon = binding.successIcon
        val spinAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.spin)
        successIcon.startAnimation(spinAnimation)
    }
}