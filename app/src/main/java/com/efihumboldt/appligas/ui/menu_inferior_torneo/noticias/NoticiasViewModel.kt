package com.efihumboldt.appligas.ui.menu_inferior_torneo.noticias

import androidx.lifecycle.ViewModel
import com.efihumboldt.appligas.entidades.Liga
import com.efihumboldt.appligas.Varios.SharedDataHolder

class NoticiasViewModel : ViewModel() {
    val bd: String? get() = SharedDataHolder.bd

    val liga : Liga? get() = SharedDataHolder.selectedLeague
}