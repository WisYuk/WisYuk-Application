package com.wisyuk.ui.home.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.wisyuk.data.pref.UserModel
import com.wisyuk.data.repository.UserRepository
import com.wisyuk.data.response.ErrorResponse
import com.wisyuk.data.response.ListTourismItem
import kotlinx.coroutines.launch
import retrofit2.HttpException

class FavoriteViewModel(private val repository: UserRepository) : ViewModel() {

    private val _listFavorite = MutableLiveData<List<ListTourismItem>>()
    val listFavorite: LiveData<List<ListTourismItem>> = _listFavorite

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _message = MutableLiveData<String?>()
    val message : LiveData<String?> = _message

    init {
        getTourism()
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getTourism() {
        //...
    }

    fun getTourism(userId: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getFavorite(userId)

                _message.value = response.message
                _isLoading.value = false
                _isError.value = false
                _listFavorite.value = response.listTourism
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