package com.wisyuk.ui.home.detail_home

import android.annotation.SuppressLint
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
import com.wisyuk.ui.home.MainActivity
import com.wisyuk.ui.login.LoginActivity
import com.wisyuk.utils.Utils.dateFormatted
import com.wisyuk.utils.Utils.dateFormattedGoAt

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
    private var userID = -1
    private var tourismID = -1
    private var goAt = ""
    private var isFavorite = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tourism = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra<ListTourismItem>(TOURISM, ListTourismItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<ListTourismItem>(TOURISM)
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
                Glide.with(this).load(tourism.image).into(binding.ivDetailPhoto)
                binding.tvDetailName.text = tourism.name
                binding.tvDetailDate.text = tourism.createdAt.dateFormatted()
                binding.tvDetailDescription.text = tourism.description
                tourismID = tourism.id
                viewModel.getDetailTourism(tourismID)
                goAt = tourism.createdAt.dateFormattedGoAt()
                viewModel.getFavData(userID, tourismID, goAt)
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

        var hotelName: String? = null
        var rideName: String? = null
        var tourGuideName: String? = null

        viewModel.favoriteData.observe(this) {
            if (it != null) {
                binding.favoriteButton.setImageResource(R.drawable.baseline_favorite_24)
                isFavorite = true
                hotelName = it.hotelName
                rideName = it.rideName
                tourGuideName = it.tourGuideName
            }

        }



        viewModel.dataHotels.observe(this) { hotel ->
            val items = hotel.map { it.name }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
            binding.spinnerHotel.adapter = adapter

            if (hotelName != null) {
                val selectedItem = hotelName
                val initPos = items.indexOf(selectedItem)
                if (initPos != -1) {
                    binding.spinnerHotel.setSelection(initPos)
                }
            }


            binding.spinnerHotel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                @SuppressLint("SetTextI18n")
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val item = hotel.find {it.name == items[position] }
                    itemHotelId = item?.id ?: -1
                    binding.priceValueHotel.text = "Rp ${item?.price}"
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

            if (rideName != null) {
                val selectedItem = rideName
                val initPos = items.indexOf(selectedItem)
                if (initPos != -1) {
                    binding.spinnerRide.setSelection(initPos)
                }
            }


            binding.spinnerRide.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                @SuppressLint("SetTextI18n")
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val item = ride.find {it.name == items[position] }
                    itemRideId = item?.id ?: -1
                    binding.priceValueRide.text = "Rp ${item?.price}"
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

            if (tourGuideName != null) {
                val selectedItem = tourGuideName
                val initPos = items.indexOf(selectedItem)
                if (initPos != -1) {
                    binding.spinnerTourGuide.setSelection(initPos)
                }
            }

            binding.spinnerTourGuide.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                @SuppressLint("SetTextI18n")
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val item = tourGuide.find {it.name == items[position] }
                    itemTourGuideId = item?.id ?: -1
                    binding.priceValueTourGuide.text = "Rp ${item?.price}"
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    itemTourGuideId = -1
                }
            }
        }

        binding.continuePaymentButton.setOnClickListener {
            //...
        }

        binding.backButton.setOnClickListener {
            val intent = Intent(this@DetailActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.favoriteButton.setOnClickListener{
            if (!isFavorite) {
                viewModel.addFavorite(userID, tourismID, itemHotelId, itemRideId, itemTourGuideId, goAt)
                Toast.makeText(this, getString(R.string.favorite_added), Toast.LENGTH_SHORT).show()
                viewModel.getFavData(userID, tourismID, goAt)
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
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }


}