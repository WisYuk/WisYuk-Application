package com.wisyuk.ui.home.ui.plans

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlanViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is plan Fragment"
    }
    val text: LiveData<String> = _text
}