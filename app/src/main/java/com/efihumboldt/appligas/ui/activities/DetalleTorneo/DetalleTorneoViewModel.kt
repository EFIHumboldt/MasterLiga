package com.efihumboldt.appligas.ui.activities.DetalleTorneo

import androidx.lifecycle.ViewModel
import com.efihumboldt.appligas.entidades.EquipoSimple
import com.efihumboldt.appligas.entidades.Liga
import com.efihumboldt.appligas.entidades.Noticia
import com.efihumboldt.appligas.entidades.PosicionesPorZona
import com.efihumboldt.appligas.entidades.Torneo
import com.efihumboldt.appligas.Varios.SharedDataHolder

class DetalleTorneoViewModel : ViewModel() {

    var torneoSeleccionado: Torneo? = null
    var bd: String? = null
    var tablaPosiciones : List<PosicionesPorZona>? = emptyList()

    fun setBD(data : String)
    {
        SharedDataHolder.bd = data
    }

    fun setSelectedTournament(data : Torneo)
    {
        SharedDataHolder.selectedTournament = data
    }

    fun setSelectedTeam(data : EquipoSimple)
    {
        SharedDataHolder.selectedTeam = data
    }

    fun setSelectedNew(data : Noticia)
    {
        SharedDataHolder.selectedNew = data
    }
    fun setSelectedLeague(data : Liga)
    {
        SharedDataHolder.selectedLeague = data
    }
}