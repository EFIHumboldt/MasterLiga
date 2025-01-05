package com.efihumboldt.appligas.ui.activities.Favoritos

import androidx.lifecycle.ViewModel
import com.efihumboldt.appligas.entidades.EquipoSimple
import com.efihumboldt.appligas.entidades.Liga
import com.efihumboldt.appligas.entidades.Torneo
import com.efihumboldt.appligas.Varios.SharedDataHolder

class FavoritosViewModel : ViewModel(){

    val bd: String? get() = SharedDataHolder.bd
    val selectedTournament : Torneo? get() = SharedDataHolder.selectedTournament

    val selectedLeague : Liga? get() = SharedDataHolder.selectedLeague

    fun setSelectedTeam(data : EquipoSimple)
    {
        SharedDataHolder.selectedTeam = data
    }
}