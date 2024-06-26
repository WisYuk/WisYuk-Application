package com.wisyuk.ui.home.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.wisyuk.data.pref.UserModel
import com.wisyuk.data.repository.UserRepository
import com.wisyuk.data.response.ErrorResponse
import com.wisyuk.data.response.ListTourismItem
import com.wisyuk.data.response.RecommendationsItem
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeViewModel(private val repository: UserRepository) : ViewModel() {

    private val _listTourism = MutableLiveData<List<ListTourismItem>>()
    val listTourism: LiveData<List<ListTourismItem>> = _listTourism

    private val _listRecs = MutableLiveData<List<RecommendationsItem>>()
    val listRecs: LiveData<List<RecommendationsItem>> = _listRecs

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _message = MutableLiveData<String?>()
    val message : LiveData<String?> = _message

    private val _noProfile = MutableLiveData<Boolean>()
    val noProfile : LiveData<Boolean> = _noProfile

//    init {
//        getTourism()
//    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun checkProfile(userId: Int) {
        viewModelScope.launch {
            try {
                repository.getProfile(userId)
                _noProfile.value = false
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                if (errorMessage == "preferences not found") {
                    _noProfile.value = true
                }
                _noProfile.value = false
            }

        }
    }

    fun getTourism() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getTourisms()
                _message.value = response.status
                _isLoading.value = false
                _isError.value = false
                _listTourism.value = response.data
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

    fun getTourism(query: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getTourisms(query)
                _message.value = response.status
                _isLoading.value = false
                _isError.value = false
                _listTourism.value = response.data
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

    fun getTourisms(goAt: String, userId: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getTourisms(goAt, userId)
                _message.value = response.status
                _isLoading.value = false
                _isError.value = false
                _listRecs.value = response.data.recommendations
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

    fun logout() {
        _isLoading.value = true
        viewModelScope.launch {
            repository.logout()
            _isLoading.value = false
        }
    }
}