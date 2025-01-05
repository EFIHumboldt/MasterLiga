package com.efihumboldt.appligas.ui.activities.DaetallesNoticia

import androidx.lifecycle.ViewModel
import com.efihumboldt.appligas.entidades.Liga
import com.efihumboldt.appligas.entidades.Noticia
import com.efihumboldt.appligas.Varios.SharedDataHolder

class DetalleNoticiaViewModel : ViewModel() {

    val bd: String? get() = SharedDataHolder.bd
    val selectedNew : Noticia? get() = SharedDataHolder.selectedNew
    val selectedLeague : Liga? get() = SharedDataHolder.selectedLeague
}