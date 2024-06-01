package com.wisyuk.ui.home.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.wisyuk.data.pref.UserModel
import com.wisyuk.data.repository.UserRepository
import com.wisyuk.data.response.ListTourismItem
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: UserRepository) : ViewModel() {

    private val _listTourism = MutableLiveData<List<ListTourismItem>>()
    val listTourism: LiveData<List<ListTourismItem>> = _listTourism

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _message = MutableLiveData<String?>()
    val message : MutableLiveData<String?> = _message

    init {
        getTourism()
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getTourism() {
        //...
    }

    fun getTourism(query: String) {
        //...
    }

    fun logout() {
        _isLoading.value = true
        viewModelScope.launch {
            repository.logout()
            _isLoading.value = false
        }
    }
}