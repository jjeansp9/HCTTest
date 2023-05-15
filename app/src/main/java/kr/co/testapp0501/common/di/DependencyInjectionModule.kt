package kr.co.testapp0501.common.di

import kr.co.testapp0501.viewmodel.AlbumCommentViewModel
import kr.co.testapp0501.viewmodel.AlbumViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

var viewModelPart = module {
    viewModel {
        AlbumViewModel()
    }
    viewModel {
        AlbumCommentViewModel()
    }
}

var AppDiModule = listOf(
    viewModelPart
)