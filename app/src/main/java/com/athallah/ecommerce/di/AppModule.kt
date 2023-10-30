package com.athallah.ecommerce.di

import com.athallah.ecommerce.data.repo.AppRepository
import com.athallah.ecommerce.data.repo.AppRepositoryImpl
import com.athallah.ecommerce.fragment.prelogin.LoginViewModel
import com.athallah.ecommerce.fragment.prelogin.OnboardingViewModel
import com.athallah.ecommerce.fragment.prelogin.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repoModule = module {
    factory<AppRepository> { AppRepositoryImpl(get()) }
}

val vmModule = module {
    viewModel { OnboardingViewModel(get()) }
    viewModel { LoginViewModel(get(),get() ) }
    viewModel { RegisterViewModel(get()) }
}
