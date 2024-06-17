package com.wisyuk.ui.home.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.wisyuk.data.pref.UserModel
import com.wisyuk.data.repository.UserRepository
import com.wisyuk.data.response.Data
import com.wisyuk.data.response.ErrorResponse
import com.wisyuk.data.response.PreferencesItem
import com.wisyuk.data.response.UpdateProfileResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.File

class ProfileViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _message = MutableLiveData<String?>()
    val message : LiveData<String?> = _message

    private val _editMode = MutableLiveData<Boolean>()
    val editMode: LiveData<Boolean> = _editMode

    private val _profileResponse = MutableLiveData<Data>()
    val profileResponse: LiveData<Data> = _profileResponse

    private val _updateResponse = MutableLiveData<UpdateProfileResponse>()
    val updateResponse: LiveData<UpdateProfileResponse> = _updateResponse

    private val _preferencesResponse = MutableLiveData<List<PreferencesItem>>()
    val preferencesResponse: LiveData<List<PreferencesItem>> = _preferencesResponse

    init {
        _editMode.value = false
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    private fun saveSession(user: UserModel) : Job {
        return viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun toggleEditMode() {
        _editMode.value = _editMode.value == false
    }

    fun getPreferences() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getPreferences()
                _message.value = response.status
                _isLoading.value = false
                _isError.value = false
                _preferencesResponse.value = response.data
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

    fun getProfile(userId: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getProfile(userId)

                _message.value = response.message
                _isLoading.value = false
                _isError.value = false
                _profileResponse.value = response.data
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

    fun updateProfile(userId: Int, image: File? = null, name: String, preferences: List<Int>) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.updateProfile(userId, image, name, preferences)
                _message.value = response.message
                _isLoading.value = false
                _isError.value = false
                _updateResponse.value = response
                val email = getSession().value?.email ?: ""
                val saveSessionJob = saveSession(
                    UserModel(
                        email,
                        name,
                        userId,
                        true
                    )
                )
                saveSessionJob.join()
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