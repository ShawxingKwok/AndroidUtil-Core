package pers.apollokwok.androidutil.demos.view

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel : ViewModel(){
    val topItems = (1..10).map(::User).let(::MutableStateFlow)
    val bottomItems = (101..110).map(::User).let(::MutableStateFlow)
}