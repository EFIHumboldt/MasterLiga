package com.efihumboldt.appligas.ui.menu_inferior_torneo.partidos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PartidosViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Estas en partidos pa"
    }
    val text: LiveData<String> = _text
}