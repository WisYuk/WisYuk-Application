package com.wisyuk.di

import android.content.Context
import com.wisyuk.data.pref.UserPreference
import com.wisyuk.data.pref.dataStore
import com.wisyuk.data.repository.UserRepository

object Injection {
    fun provideRepository(context: Context) : UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}