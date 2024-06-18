package com.wisyuk.ui.paymentreceipt

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.wisyuk.data.pref.UserModel
import com.wisyuk.data.repository.UserRepository
import com.wisyuk.data.response.ErrorResponse
import com.wisyuk.data.response.PaymentMethodItem
import com.wisyuk.data.response.PlanDataItem
import com.wisyuk.data.response.Receipt
import kotlinx.coroutines.launch
import retrofit2.HttpException

class PaymentReceiptViewModel(private val repository: UserRepository): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _message = MutableLiveData<String?>()
    val message : LiveData<String?> = _message

    private val _data = MutableLiveData<Receipt>()
    val data : LiveData<Receipt> = _data

    private val _selectedPaymentMethod = MutableLiveData<PaymentMethodItem?>()
    val selectedPaymentMethod: LiveData<PaymentMethodItem?> = _selectedPaymentMethod

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getDetailTourism(receiptID: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getReceipt(receiptID)
                _message.value = response.status
                _isLoading.value = false
                _isError.value = false
                _data.value = response.data

//                if (response.data.paymentMethodsId != null) fetchPaymentMethod(response.data.paymentMethodsId)
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

    private val _paymentMethod = MutableLiveData<List<PaymentMethodItem>>()
    val paymentMethod: LiveData<List<PaymentMethodItem>> = _paymentMethod

    fun fetchPaymentMethod(paymentMethodID: Int){
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = repository.getPaymentMethod()
                _paymentMethod.value = response.data ?: emptyList()
                _isLoading.value = false
                _isError.value = false

                assignPaymentMethod(paymentMethodID)
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
    private fun assignPaymentMethod(paymentMethodID: Int) {
        _paymentMethod.value?.let { methods ->
            val method = methods.find { it.id == paymentMethodID }
            _selectedPaymentMethod.value = method
        }
    }
}