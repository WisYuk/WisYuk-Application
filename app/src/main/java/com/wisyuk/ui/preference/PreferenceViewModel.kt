package com.wisyuk.ui.preference

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide.init
import com.google.gson.Gson
import com.wisyuk.data.pref.UserModel
import com.wisyuk.data.repository.UserRepository
import com.wisyuk.data.response.AddPreferencesResponse
import com.wisyuk.data.response.ErrorResponse
import com.wisyuk.data.response.PreferencesItem
import kotlinx.coroutines.launch
import retrofit2.HttpException

class PreferenceViewModel(private val repository: UserRepository): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _message = MutableLiveData<String?>()
    val message : LiveData<String?> = _message

    private val _preferences = MutableLiveData<List<PreferencesItem>>()
    val preferences: LiveData<List<PreferencesItem>> = _preferences

    private val _addResponse = MutableLiveData<AddPreferencesResponse>()
    val addResponse: LiveData<AddPreferencesResponse> = _addResponse

    init {
        getPreferences()
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }


    private fun getPreferences() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getPreferences()
                _message.value = response.status
                _isLoading.value = false
                _isError.value = false
                _preferences.value = response.data
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

    fun addPreferences(userId: Int, preferences: List<Int>) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.postPreferences(userId, preferences)
                _message.value = response.status
                _isLoading.value = false
                _isError.value = false
                _addResponse.value = response
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


}