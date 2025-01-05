package com.efihumboldt.appligas.menu_inferior.partidos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EquiposViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Estás en equipos fiera"
    }
    val text: LiveData<String> = _text
}