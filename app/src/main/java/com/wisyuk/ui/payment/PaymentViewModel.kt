package com.wisyuk.ui.payment

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
import kotlinx.coroutines.launch
import retrofit2.HttpException

class PaymentViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _message = MutableLiveData<String?>()
    val message : LiveData<String?> = _message

    private val _paymentResponse = MutableLiveData<AddPaidPlanResponse>()
    val paymentResponse: LiveData<AddPaidPlanResponse> = _paymentResponse

    fun addPaidPlan(userID: Int, tourismID: Int, hotelID: Int, rideID: Int, tourGuideID: Int, goDate: String, status: Int, paymentMethodID: Int) {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = repository.addPaidPlan(userID, tourismID, hotelID, rideID, tourGuideID, goDate, status, paymentMethodID)
                _paymentResponse.value = response
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

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}