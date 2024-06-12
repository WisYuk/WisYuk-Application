package com.wisyuk.ui.yourplan.detail_plan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.wisyuk.data.pref.UserModel
import com.wisyuk.data.repository.UserRepository
import com.wisyuk.data.response.ErrorResponse
import com.wisyuk.data.response.PlanDataItem
import kotlinx.coroutines.launch
import retrofit2.HttpException

class DetailPlanViewModel(private val repository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _message = MutableLiveData<String?>()
    val message : LiveData<String?> = _message

    private val _data = MutableLiveData<PlanDataItem>()
    val data : LiveData<PlanDataItem> = _data

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getDetailTourism(userId: Int, tourismId: Int, goAt: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getDetailPaidPlan(userId, tourismId, goAt)
                _message.value = response.status
                _isLoading.value = false
                _isError.value = false
                _data.value = response.data[0]
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