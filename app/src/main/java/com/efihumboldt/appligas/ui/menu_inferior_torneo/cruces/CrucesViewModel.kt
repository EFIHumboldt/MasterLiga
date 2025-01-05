package com.efihumboldt.appligas.ui.menu_inferior_torneo.cruces

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CrucesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Pantalla de cruces (definir)"
    }
    val text: LiveData<String> = _text
}