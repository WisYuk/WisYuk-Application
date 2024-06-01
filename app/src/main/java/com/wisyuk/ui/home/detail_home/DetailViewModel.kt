package com.wisyuk.ui.home.detail_home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wisyuk.data.repository.UserRepository
import com.wisyuk.data.response.ListTourismItem

class DetailViewModel(private val repository: UserRepository) : ViewModel() {
    private val _tourism = MutableLiveData<List<ListTourismItem>>()
    val listTourism: LiveData<List<ListTourismItem>> = _tourism

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _message = MutableLiveData<String?>()
    val message : MutableLiveData<String?> = _message

    fun getTourism() {

    }
}