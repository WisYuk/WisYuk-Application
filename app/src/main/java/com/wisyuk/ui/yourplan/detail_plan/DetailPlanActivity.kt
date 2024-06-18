package com.wisyuk.ui.yourplan.detail_plan

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.wisyuk.R
import com.wisyuk.data.response.ListTourismItem
import com.wisyuk.data.response.RecommendationsItem
import com.wisyuk.databinding.ActivityDetailPlanBinding
import com.wisyuk.ui.ViewModelFactory
import com.wisyuk.ui.login.LoginActivity
import com.wisyuk.ui.paymentreceipt.PaymentReceiptActivity
import com.wisyuk.utils.Utils.calculateReminder
import com.wisyuk.utils.Utils.convertDateFromYMDtoMDy
import com.wisyuk.utils.Utils.dateFormatted
import com.wisyuk.utils.Utils.dateFormattedGoAt
import com.wisyuk.utils.Utils.dateFormattedYYYYMMDD

class DetailPlanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPlanBinding
    private val viewModel by viewModels<DetailPlanViewModel> {
        ViewModelFactory.getInstance(this)
    }

    companion object {
        const val TOURISM = "tourism_detail_paid"
        const val EXTRA_GOAT = "goAt"
        const val EXTRA_NAME = "name"
        const val PLAN_NAME = "plan_name"
        const val RECEIPT_ID = "receipt_id"
    }

    private var userID = -1
    private var tourismID = -1
    private var tourismName = ""
    private var goAt = ""
    private var name = ""
    private var receiptId = -1
    private var errorMessage = ""
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.favoriteButton.visibility = View.GONE

        val tourism = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(TOURISM, ListTourismItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(TOURISM)
        }


        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(
                    Intent(this, LoginActivity::class.java)
                )
                finish()
            }
            userID = user.id
            name = user.name
            if (tourism != null) {
                tourismID = tourism.id
                tourismName = tourism.name
                goAt = tourism.goAt ?: ""
                Log.d("Cek", goAt)
            }
            viewModel.getDetailTourism(userID, tourismID, goAt.dateFormattedYYYYMMDD())
        }
        setupView()
        setupAction()
        observerViewModel()

    }

    override fun onResume() {
        super.onResume()
        showLoading(false)
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
        binding.backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.receipt.setOnClickListener {
            val intent = Intent(this, PaymentReceiptActivity::class.java)
            intent.putExtra(RECEIPT_ID, receiptId)
            intent.putExtra(PLAN_NAME, tourismName)
            intent.putExtra(EXTRA_NAME, name)
            startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun observerViewModel() {
        viewModel.data.observe(this) {
            with(binding) {
                Glide.with(this@DetailPlanActivity).load(it.tourismImage).into(ivDetailPhoto)
                tvDetailName.text = it.tourismName
                tvDetailDescription.text = it.tourismDescription
                tvDetailDate.text = goAt.dateFormatted()
                selectedHotel.text = it.hotelName
                dateHotel.text = goAt.dateFormatted()
                selectedRide.text = it.rideName
                dateRide.text = goAt.dateFormatted()
                selectedTourGuide.text = it.tourGuideName
                dateTourGuide.text = goAt.dateFormatted()

                val reminderDays = calculateReminder(goAt.dateFormattedGoAt())
                reminderHotel.text = getString(R.string.reminder_date, reminderDays.toString())
                reminderRide.text = getString(R.string.reminder_date, reminderDays.toString())
                reminderTourGuide.text = getString(R.string.reminder_date, reminderDays.toString())
            }
            receiptId = it.paymentReceiptId
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
        binding.main.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun showError(isError: Boolean) {
        if (isError) {
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }
}