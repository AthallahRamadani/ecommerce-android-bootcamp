package com.athallah.ecommerce.di

import com.athallah.ecommerce.data.AppRepository
import com.athallah.ecommerce.data.ImpAppRepository
import com.athallah.ecommerce.fragment.prelogin.LoginViewModel
import com.athallah.ecommerce.fragment.prelogin.OnboardingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repoModule = module {
    factory<ImpAppRepository> { AppRepository(get()) }
}

val vmModule = module {
    viewModel { OnboardingViewModel(get()) }
    viewModel { LoginViewModel(get()) }
}
