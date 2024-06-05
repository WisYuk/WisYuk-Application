package com.wisyuk.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.wisyuk.data.repository.UserRepository
import com.wisyuk.data.response.ErrorResponse
import com.wisyuk.data.response.SignUpResponse
import com.wisyuk.utils.Utils.isEmailValid
import com.wisyuk.utils.Utils.isPasswordValid
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SignUpViewModel(private val repository: UserRepository) : ViewModel(){
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError : LiveData<Boolean> = _isError

    private val _message = MutableLiveData<String?>()
    val message : LiveData<String?> = _message

    private val _signUpResponse = MutableLiveData<SignUpResponse>()
    val signUpResponse: LiveData<SignUpResponse> = _signUpResponse

    fun postData(name: String, email: String, password: String) {
        _isLoading.value = true

        if (!isEmailValid(email) || !isPasswordValid(password)) {
            _message.value = "Please check the format"
            _isLoading.value = false
            _isError.value = true
            return
        }

        viewModelScope.launch {
            try {
                val response = repository.signUp(name, email, password)
                _signUpResponse.value = response
                _message.value = response.message
                _isLoading.value = false
                _isError.value = false
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