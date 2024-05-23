package com.weynard02.wisyuk.di

import android.content.Context
import com.weynard02.wisyuk.data.pref.UserPreference
import com.weynard02.wisyuk.data.pref.dataStore
import com.weynard02.wisyuk.data.repository.UserRepository

object Injection {
    fun provideRepository(context: Context) : UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}