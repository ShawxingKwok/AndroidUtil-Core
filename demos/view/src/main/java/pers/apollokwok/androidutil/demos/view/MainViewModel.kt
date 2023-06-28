package pers.apollokwok.androidutil.demos.view

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel : ViewModel(){
    val items = (1..50).map(::User).let(::MutableStateFlow)
}