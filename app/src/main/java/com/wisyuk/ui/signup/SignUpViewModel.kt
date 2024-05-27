package com.wisyuk.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wisyuk.data.repository.UserRepository
import com.wisyuk.utils.Utils.isEmailValid
import com.wisyuk.utils.Utils.isPasswordValid

class SignUpViewModel(private val repository: UserRepository) : ViewModel(){
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError : LiveData<Boolean> = _isError

    private val _message = MutableLiveData<String?>()
    val message : LiveData<String?> = _message

    fun postData(name: String, email: String, password: String) {
        _isLoading.value = true

        if (!isEmailValid(email) || !isPasswordValid(password)) {
            _message.value = "Please check the format"
            _isLoading.value = false
            _isError.value = true
            return
        }

        // handle response
    }
}