package com.wisyuk.ui.home.detail_home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.wisyuk.data.pref.UserModel
import com.wisyuk.data.repository.UserRepository
import com.wisyuk.data.response.FavoriteResponse
import com.wisyuk.data.response.PlanDataItem
import com.wisyuk.data.response.DataHotelsItem
import com.wisyuk.data.response.DataRidesItem
import com.wisyuk.data.response.DataTourGuidesItem
import com.wisyuk.data.response.ErrorResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class DetailViewModel(private val repository: UserRepository) : ViewModel() {
    private val _dataTourGuides = MutableLiveData<List<DataTourGuidesItem>>()
    val dataTourGuides: LiveData<List<DataTourGuidesItem>> = _dataTourGuides

    private val _dataRides = MutableLiveData<List<DataRidesItem>>()
    val dataRides: LiveData<List<DataRidesItem>> = _dataRides

    private val _dataHotels = MutableLiveData<List<DataHotelsItem>>()
    val dataHotels: LiveData<List<DataHotelsItem>> = _dataHotels

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _message = MutableLiveData<String?>()
    val message : LiveData<String?> = _message

    private val _favoriteData = MutableLiveData<PlanDataItem?>()
    val favoriteData : LiveData<PlanDataItem?> = _favoriteData

    private val _favResponse = MutableLiveData<FavoriteResponse>()
    val favResponse : LiveData<FavoriteResponse> = _favResponse

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getFavData(userId: Int, tourismId: Int, goAt: String) {
        viewModelScope.launch {
            try {
                val response = repository.getDetaiLFavoritePlan(userId, tourismId, goAt)
                _favoriteData.value = response.data[0]
            } catch (_: HttpException) {

            }
        }
    }

    fun getDetailTourism(tourismId: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getDetailTourism(tourismId)
                _message.value = response.status
                _isLoading.value = false
                _isError.value = false
                _dataTourGuides.value = response.dataTourGuides
                _dataRides.value = response.dataRides
                _dataHotels.value = response.dataHotels
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                _message.value = errorMessage
                _isLoading.value = false
                _isError.value = true
            }
        }
    }

    fun addFavorite(userId: Int, tourismId: Int, hotelId: Int, rideId: Int, tourGuideId: Int, goDate: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.addFavouritePlan(userId, tourismId, hotelId, rideId, tourGuideId, goDate)
                _message.value = response.message
                _isLoading.value = false
                _isError.value = false
                _favResponse.value = response
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                _message.value = errorMessage
                _isLoading.value = false
                _isError.value = true
            }
        }
    }

    fun deleteFavorite(userId: Int, tourismId: Int, goDate: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.deleteFavouritePlan(userId, tourismId,goDate)
                _message.value = response.message
                _isLoading.value = false
                _isError.value = false
                _favResponse.value = response
                nullifyFavData()
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                _message.value = errorMessage
                _isLoading.value = false
                _isError.value = true
            }
        }
    }

    private fun nullifyFavData() {
        _favoriteData.value = null
    }
}