package com.wisyuk.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.wisyuk.data.pref.UserModel
import com.wisyuk.data.repository.UserRepository
import com.wisyuk.data.response.ErrorResponse
import com.wisyuk.data.response.LoginResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _message = MutableLiveData<String?>()
    val message : MutableLiveData<String?> = _message

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    fun login(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.login(email, password)
                val result = response.user
                val saveSessionJob = saveSession(
                    UserModel(
                        email,
                        result.name,
                        result.id,
                        true
                    )
                )
                saveSessionJob.join()
                _message.value = response.message
                _isLoading.value = false
                _isError.value = false
                _loginResponse.value = response
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

    private fun saveSession(user: UserModel) : Job {
        return viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}