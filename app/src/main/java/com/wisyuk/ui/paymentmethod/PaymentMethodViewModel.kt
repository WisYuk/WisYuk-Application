package com.wisyuk.ui.paymentmethod

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.wisyuk.data.pref.UserModel
import com.wisyuk.data.repository.UserRepository
import com.wisyuk.data.response.AddPaidPlanResponse
import com.wisyuk.data.response.ErrorResponse
import com.wisyuk.data.response.PaymentMethodItem
import com.wisyuk.data.response.PaymentMethodResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class PaymentMethodViewModel(private val repository: UserRepository): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _message = MutableLiveData<String?>()
    val message : LiveData<String?> = _message

    private val _paymentMethod = MutableLiveData<List<PaymentMethodItem>>()
    val paymentMethod: LiveData<List<PaymentMethodItem>> = _paymentMethod

    fun fetchPaymentMethod(){
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = repository.getPaymentMethod()
                _paymentMethod.value = response.data ?: emptyList()
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
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}