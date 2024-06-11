package com.wisyuk.ui.home.detail_home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.wisyuk.R
import com.wisyuk.data.response.DataHotelsItem
import com.wisyuk.data.response.ListTourismItem
import com.wisyuk.databinding.ActivityDetailBinding
import com.wisyuk.ui.ViewModelFactory
import com.wisyuk.ui.login.LoginActivity
import com.wisyuk.utils.Utils.dateFormatted

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }
    companion object {
        const val TOURISM = "tourism"
    }

    private var errorMessage = ""
    private var itemHotelId = -1
    private var itemRideId = -1
    private var itemTourGuideId = -1
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(
                    Intent(this, LoginActivity::class.java)
                )
                finish()
            }
        }

        setupView()
        setupAction()
        observerViewModel()

        val tourism = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra<ListTourismItem>(TOURISM, ListTourismItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<ListTourismItem>(TOURISM)
        }

        if (tourism != null) {
            Glide.with(this).load(tourism.image).into(binding.ivDetailPhoto)
            binding.tvDetailName.text = tourism.name
            binding.tvDetailDate.text = tourism.createdAt.dateFormatted()
            binding.tvDetailDescription.text = tourism.description
            viewModel.getDetailTourism(tourism.id)
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

    private fun setupAction() {
        viewModel.dataHotels.observe(this) { hotel ->
            val items = hotel.map { it.name }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
            binding.spinnerHotel.adapter = adapter

            binding.spinnerHotel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    itemHotelId = hotel.find {it.name == items[position] }?.id ?: -1
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    itemHotelId = -1
                }
            }
        }

        viewModel.dataRides.observe(this) { ride ->
            val items = ride.map { it.name }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
            binding.spinnerRide.adapter = adapter

            binding.spinnerRide.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    itemRideId = ride.find {it.name == items[position] }?.id ?: -1
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    itemRideId = -1
                }
            }
        }

        viewModel.dataTourGuides.observe(this) { tourGuide ->
            val items = tourGuide.map { it.name }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
            binding.spinnerTourGuide.adapter = adapter

            binding.spinnerTourGuide.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    itemTourGuideId = tourGuide.find {it.name == items[position] }?.id ?: -1
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    itemTourGuideId = -1
                }
            }
        }

        binding.continuePaymentButton.setOnClickListener {
            //...
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
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }


}