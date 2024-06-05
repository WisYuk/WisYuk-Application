package com.wisyuk.di

import android.content.Context
import com.wisyuk.data.api.ApiConfig
import com.wisyuk.data.pref.UserPreference
import com.wisyuk.data.pref.dataStore
import com.wisyuk.data.repository.UserRepository

object Injection {
    fun provideRepository(context: Context) : UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(pref, apiService)
    }
}