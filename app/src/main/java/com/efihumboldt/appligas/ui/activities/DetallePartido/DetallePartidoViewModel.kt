package com.efihumboldt.appligas.ui.activities.DetallePartido

import androidx.lifecycle.ViewModel
import com.efihumboldt.appligas.entidades.EquipoSimple
import com.efihumboldt.appligas.entidades.Liga
import com.efihumboldt.appligas.entidades.Partido
import com.efihumboldt.appligas.entidades.Torneo
import com.efihumboldt.appligas.Varios.SharedDataHolder

class DetallePartidoViewModel : ViewModel() {

    val selectedMatch: Partido? get() = SharedDataHolder.selectedMatch

    val bd: String? get() = SharedDataHolder.bd

    val selectedTournament : Torneo? get() = SharedDataHolder.selectedTournament

    val selectedLeague : Liga? get() = SharedDataHolder.selectedLeague

    fun setSelectedMatch(data : Partido)
    {
        SharedDataHolder.selectedMatch = data
    }

    fun setSelectedTeam(data : EquipoSimple)
    {
        SharedDataHolder.selectedTeam = data
    }
}