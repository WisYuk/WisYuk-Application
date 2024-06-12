package com.wisyuk.ui.home.ui.plans

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.wisyuk.data.pref.UserModel
import com.wisyuk.data.repository.UserRepository
import com.wisyuk.data.response.ErrorResponse
import com.wisyuk.data.response.PlanTourismItem
import kotlinx.coroutines.launch
import retrofit2.HttpException

class PlanViewModel(private val repository: UserRepository) : ViewModel() {

    private val _listPaid = MutableLiveData<List<PlanTourismItem>>()
    val listPaid: LiveData<List<PlanTourismItem>> = _listPaid

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _message = MutableLiveData<String?>()
    val message : LiveData<String?> = _message


    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }


    fun getTourism(userId: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getPaidPlans(userId)
                _message.value = response.status
                _isLoading.value = false
                _isError.value = false
                _listPaid.value = response.data
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