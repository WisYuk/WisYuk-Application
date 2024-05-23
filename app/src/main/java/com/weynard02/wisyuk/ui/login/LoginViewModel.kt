package com.weynard02.wisyuk.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weynard02.wisyuk.data.pref.UserModel
import com.weynard02.wisyuk.data.repository.UserRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _message = MutableLiveData<String?>()
    val message : MutableLiveData<String?> = _message

    fun login(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            // handle response belum
            val saveSessionJob = saveSession(
                UserModel(
                    email,
                    "token",
                    true
                )
            )
            saveSessionJob.join()
            _message.value = "success"
            _isLoading.value = false
            _isError.value = false
        }

    }

    private fun saveSession(user: UserModel) : Job {
        return viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}