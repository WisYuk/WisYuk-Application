package com.wisyuk.ui.payment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.wisyuk.R
import com.wisyuk.data.extras.BookingDetails
import com.wisyuk.databinding.ActivityPaymentBinding
import com.wisyuk.ui.ViewModelFactory
import com.wisyuk.ui.login.LoginActivity
import com.wisyuk.ui.paymentmethod.PaymentMethodActivity
import com.wisyuk.ui.transaction.TransactionActivity
import java.lang.Integer.parseInt

class PaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentBinding
    private val viewModel by viewModels<PaymentViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var errorMessage: String = ""
    private lateinit var bookingDetails: BookingDetails
    private var userId: String = ""
    private var selectedValueId: Int = -1

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == PaymentMethodActivity.RESULT_CODE && result.data != null) {
            selectedValueId =
                result.data?.getIntExtra(PaymentMethodActivity.PAYMENT_METHOD_ID, 0) ?: -1
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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {

                userId = user.id.toString()
            }
        }
        val bookingDetail: BookingDetails? =
            intent?.extras?.getParcelable("BOOKING_DETAILS", BookingDetails::class.java)

        if (bookingDetail != null) {
            bookingDetails = bookingDetail
        }

        setupView()
        setupAction()
        observerViewModel()
    }

    @RequiresApi(Build.VERSION_CODES.O)
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

        Glide.with(this).load(bookingDetails.photoUrl).into(binding.ivPlanPhoto)
        Log.d("Check", bookingDetails.date.toString())
        val formattedDate = bookingDetails.date

        binding.tvPlanHotelName.text = bookingDetails.hotelName
        val hotelPrice = "Rp ${bookingDetails.hotelPrice}"
        binding.tvPlanHotelPriceValue.text = hotelPrice
        binding.tvPlanHotelDate.text = formattedDate

        binding.tvPlanRideName.text = bookingDetails.rideName
        val ridePrice = "Rp ${bookingDetails.ridePrice}"
        binding.tvPlanRidePriceValue.text = ridePrice
        binding.tvPlanRideDate.text = formattedDate

        binding.tvPlanGuideName.text = bookingDetails.tourGuideName
        val tourGuidePrice = "Rp ${bookingDetails.tourGuidePrice}"
        binding.tvPlanGuidePriceValue.text = tourGuidePrice
        binding.tvPlanGuideDate.text = formattedDate

        val totalPrices = bookingDetails.hotelPrice + bookingDetails.ridePrice + bookingDetails.tourGuidePrice
        val totalPrice = "Rp $totalPrices"
        binding.tvPlanTotalPriceValue.text = totalPrice
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupAction() {
        binding.btMethodChoose.setOnClickListener {
            val paymentMethodIntent =
                Intent(this@PaymentActivity, PaymentMethodActivity::class.java)
            resultLauncher.launch(paymentMethodIntent)
        }
        binding.btPayment.setOnClickListener {
            val userID = parseInt(userId)
            val tourismID = bookingDetails.tourismId
            val hotelID = bookingDetails.hotelId
            val rideID = bookingDetails.rideId
            val tourGuideID = bookingDetails.tourGuideId
            val goDate = bookingDetails.date ?: ""
            val status = "1"
            val paymentMethodID = selectedValueId

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
        }
    }

    private fun observerViewModel() {
        viewModel.paymentResponse.observe(this) {
            it?.let {
                startActivity(Intent(this@PaymentActivity, TransactionActivity::class.java))
                finish()
            }
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