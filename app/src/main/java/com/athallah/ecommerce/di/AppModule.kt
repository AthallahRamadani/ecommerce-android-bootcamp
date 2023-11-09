package com.athallah.ecommerce.di

import com.athallah.ecommerce.fragment.detail.DetailViewModel
import com.athallah.ecommerce.fragment.main.home.HomeViewModel
import com.athallah.ecommerce.fragment.main.MainViewModel
import com.athallah.ecommerce.fragment.main.profile.ProfileViewModel
import com.athallah.ecommerce.fragment.main.store.StoreViewModel
import com.athallah.ecommerce.fragment.main.store.search.SearchViewModel
import com.athallah.ecommerce.fragment.prelogin.LoginViewModel
import com.athallah.ecommerce.fragment.prelogin.OnboardingViewModel
import com.athallah.ecommerce.fragment.prelogin.RegisterViewModel
import com.athallah.ecommerce.fragment.review.ReviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val vmModule = module {
    viewModel { OnboardingViewModel(get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { RegisterViewModel(get(), get()) }
    viewModel { MainViewModel(get()) }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { StoreViewModel(get(), get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { DetailViewModel(get(),get()) }
    viewModel { ReviewViewModel(get()) }


}
