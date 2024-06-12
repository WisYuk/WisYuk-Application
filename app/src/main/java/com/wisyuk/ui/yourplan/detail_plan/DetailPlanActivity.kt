package com.wisyuk.ui.yourplan.detail_plan

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.wisyuk.R
import com.wisyuk.data.response.ListTourismItem
import com.wisyuk.data.response.PlanTourismItem
import com.wisyuk.databinding.ActivityDetailPlanBinding
import com.wisyuk.ui.ViewModelFactory
import com.wisyuk.ui.home.detail_home.DetailActivity
import com.wisyuk.ui.home.detail_home.DetailViewModel
import com.wisyuk.ui.login.LoginActivity
import com.wisyuk.utils.Utils.calculateReminder
import com.wisyuk.utils.Utils.dateFormatted

class DetailPlanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPlanBinding
    private val viewModel by viewModels<DetailPlanViewModel> {
        ViewModelFactory.getInstance(this)
    }

    companion object {
        const val TOURISM = "tourism_detail_paid"
    }

    private var userID = -1
    private var tourismID = -1
    private var goAt = ""
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tourism = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(TOURISM, PlanTourismItem::class.java)
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
            if (tourism != null) {
                goAt = tourism.goAt
            }
//            viewModel.getDetailTourism()
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

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun observerViewModel() {
        viewModel.data.observe(this) {
            with(binding) {
                tvDetailName.text = it.tourismName
                tvDetailDescription.text = it.tourismDescription
                tvDetailDate.text = goAt.dateFormatted()
                selectedHotel.text = it.hotelName
                dateHotel.text = goAt.dateFormatted()
                selectedRide.text = it.rideName
                dateRide.text = goAt.dateFormatted()
                selectedTourGuide.text = it.tourGuideName
                dateTourGuide.text = goAt.dateFormatted()

                val reminderDays = calculateReminder(goAt)
                reminderHotel.text = getString(R.string.reminder_date, reminderDays.toString())
                reminderRide.text = getString(R.string.reminder_date, reminderDays.toString())
                reminderTourGuide.text = getString(R.string.reminder_date, reminderDays.toString())
            }


        }
    }
}