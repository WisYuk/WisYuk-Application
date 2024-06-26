package com.wisyuk.ui

import android.content.Context
import android.provider.ContactsContract.Profile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wisyuk.data.repository.UserRepository
import com.wisyuk.di.Injection
import com.wisyuk.ui.home.detail_home.DetailViewModel
import com.wisyuk.ui.home.ui.favorite.FavoriteViewModel
import com.wisyuk.ui.home.ui.home.HomeViewModel
import com.wisyuk.ui.home.ui.plans.PlanViewModel
import com.wisyuk.ui.home.ui.profile.ProfileViewModel
import com.wisyuk.ui.login.LoginViewModel
import com.wisyuk.ui.payment.PaymentViewModel
import com.wisyuk.ui.paymentmethod.PaymentMethodViewModel
import com.wisyuk.ui.paymentreceipt.PaymentReceiptViewModel
import com.wisyuk.ui.preference.PreferenceViewModel
import com.wisyuk.ui.signup.SignUpViewModel
import com.wisyuk.ui.userdatemenu.DateViewModel
import com.wisyuk.ui.yourplan.detail_plan.DetailPlanViewModel

class ViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }
            modelClass.isAssignableFrom(PaymentViewModel::class.java) -> {
                PaymentViewModel(repository) as T
            }
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
                FavoriteViewModel(repository) as T
            }
            modelClass.isAssignableFrom(PlanViewModel::class.java) -> {
                PlanViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailPlanViewModel::class.java) -> {
                DetailPlanViewModel(repository) as T
            }
            modelClass.isAssignableFrom(PreferenceViewModel::class.java) -> {
                PreferenceViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DateViewModel::class.java) -> {
                DateViewModel(repository) as T
            }
            modelClass.isAssignableFrom(PaymentMethodViewModel::class.java) -> {
                PaymentMethodViewModel(repository) as T
            }
            modelClass.isAssignableFrom(PaymentReceiptViewModel::class.java) -> {
                PaymentReceiptViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}