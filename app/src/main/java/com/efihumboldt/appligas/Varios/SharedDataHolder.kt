package com.efihumboldt.appligas.Varios

import com.efihumboldt.appligas.entidades.EquipoSimple
import com.efihumboldt.appligas.entidades.Liga
import com.efihumboldt.appligas.entidades.Noticia
import com.efihumboldt.appligas.entidades.Partido
import com.efihumboldt.appligas.entidades.Torneo

object SharedDataHolder {
    var bd: String? = null
    var selectedTournament : Torneo? = null
    var selectedMatch : Partido? = null
    var selectedTeam : EquipoSimple? = null
    var selectedLeague : Liga? = null
    var selectedNew : Noticia? = null
}