
package com.efihumboldt.appligas.ui.activities.DetalleEquipo

import androidx.lifecycle.ViewModel
import com.efihumboldt.appligas.entidades.EquipoSimple
import com.efihumboldt.appligas.entidades.Partido
import com.efihumboldt.appligas.entidades.Torneo
import com.efihumboldt.appligas.Varios.SharedDataHolder

class DetalleEquipoViewModel : ViewModel() {

    val bd: String? get() = SharedDataHolder.bd
    val selectedTournament : Torneo? get() = SharedDataHolder.selectedTournament
    val selectedTeam : EquipoSimple? get() = SharedDataHolder.selectedTeam

    fun setSelectedMatch (match : Partido)
    {
        SharedDataHolder.selectedMatch = match
    }
}