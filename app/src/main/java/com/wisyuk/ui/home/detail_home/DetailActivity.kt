package com.wisyuk.ui.home.detail_home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.wisyuk.data.extras.BookingDetails
import com.wisyuk.data.response.ListTourismItem
import com.wisyuk.data.response.RecommendationsItem
import com.wisyuk.databinding.ActivityDetailBinding
import com.wisyuk.ui.ViewModelFactory
import com.wisyuk.ui.home.MainActivity
import com.wisyuk.ui.login.LoginActivity
import com.wisyuk.ui.payment.PaymentActivity
import com.wisyuk.utils.Utils.convertDateFromYMDtoMDy

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }
    companion object {
        const val TOURISM = "tourism"
        const val EXTRA_GOAT = "goAt"
    }

    private var errorMessage = ""
    private var itemHotelId = -1
    private var itemRideId = -1
    private var itemTourGuideId = -1
    private var userID = -1
    private var tourismID = -1
    private var goAt = ""
    private var isFavorite = false
    private var photoUrl = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading(false)
        val tourism = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra<RecommendationsItem>(TOURISM, RecommendationsItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<RecommendationsItem>(TOURISM)
        }

        goAt = intent.getStringExtra(EXTRA_GOAT).toString()

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(
                    Intent(this, LoginActivity::class.java)
                )
                finish()
            }
            userID = user.id

            if (tourism != null) {
                Glide.with(this).load(tourism.details.image).into(binding.ivDetailPhoto)
                photoUrl = tourism.details.image
                binding.tvDetailName.text = tourism.details.name

                if (goAt != null) {
                    binding.tvDetailDate.text = convertDateFromYMDtoMDy(goAt)
                }
//                else {
//                    binding.tvDetailDate.text = goAt
//                }
                binding.tvDetailDescription.text = tourism.details.description
                tourismID = tourism.id
                viewModel.getDetailTourism(tourismID)

                if (goAt != null) {
                    viewModel.getFavData(userID, tourismID, goAt)
                }
            }
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupAction() {

        var hotelName: String? = null
        var rideName: String? = null
        var tourGuideName: String? = null

        //Parcelable Neccesities
        var hotelTitle: String? = null
        var rideTitle: String? = null
        var tourGuideTitle: String? = null
        var hotelPrice: Int? = null
        var ridePrice: Int? = null
        var tourGuidePrice: Int? = null

        viewModel.favoriteData.observe(this) {
            if (it != null) {
                binding.favoriteButton.setImageResource(R.drawable.baseline_favorite_24)
                isFavorite = true
                hotelName = it.hotelName
                rideName = it.rideName
                tourGuideName = it.tourGuideName
            } else {
                binding.favoriteButton.setImageResource(R.drawable.baseline_favorite_border_24)
                isFavorite = false
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

            // TODO: SESUAIKAN PRICE HOTEL (SEMENTARA AVERAGE)
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
                    val avgPrice = (item?.minPrice?.let { item.maxPrice.plus(it) })?.div(2)
                    binding.priceValueHotel.text = "Rp $avgPrice"
                    hotelPrice = avgPrice
                    hotelTitle = item?.name
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
                    ridePrice = item?.price
                    rideTitle = item?.name
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
                    tourGuidePrice = item?.price
                    tourGuideTitle = item?.name
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    itemTourGuideId = -1
                }
            }
        }

        binding.continuePaymentButton.setOnClickListener {
            val bookingDetails = BookingDetails(
                tourismID,
                photoUrl,
                hotelName = hotelTitle,
                itemHotelId,
                hotelPrice ?: 0,
                rideName = rideTitle,
                itemRideId,
                ridePrice ?: 0,
                tourGuideName = tourGuideTitle,
                itemTourGuideId,
                tourGuidePrice ?: 0,
                date = goAt
            )

            val intent = Intent(this@DetailActivity, PaymentActivity::class.java)
            intent.putExtra("PREV_ACTIVITY_CLASS", DetailActivity::class.java)
            intent.putExtra("BOOKING_DETAILS", bookingDetails)
            startActivity(intent)
        }

        binding.backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.favoriteButton.setOnClickListener{
            if (!isFavorite) {
                viewModel.addFavorite(userID, tourismID, itemHotelId, itemRideId, itemTourGuideId, goAt)
                Toast.makeText(this, getString(R.string.favorite_added), Toast.LENGTH_SHORT).show()
            } else {
                viewModel.deleteFavorite(userID, tourismID, goAt)
                Toast.makeText(this, getString(R.string.favorite_deleted), Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun observerViewModel() {
        viewModel.favResponse.observe(this) {
            if (it != null) {
                viewModel.getFavData(userID, tourismID, goAt)
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
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }


}