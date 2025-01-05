package com.efihumboldt.appligas.ui.activities.Ligas

import androidx.lifecycle.ViewModel
import com.efihumboldt.appligas.entidades.Liga
import com.efihumboldt.appligas.Varios.SharedDataHolder

class LigaViewModel : ViewModel(){
    val selectedLeague : Liga? = null
    fun setLeague(liga: Liga){
        SharedDataHolder.selectedLeague = liga
    }
}