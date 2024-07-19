package app.bambushain.viewModels

import app.bambushain.viewModels.authentication.LoginViewModel
import app.bambushain.viewModels.user.PandaViewModel
import app.bambushain.viewModels.user.UserCardViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        LoginViewModel(get(), get())
    }
    viewModelOf(::UserCardViewModel)
    viewModel {
        PandaViewModel(get(), get())
    }
}