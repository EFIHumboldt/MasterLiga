package com.efihumboldt.appligas.services

import com.efihumboldt.appligas.entidades.Jugador
import com.efihumboldt.appligas.entidades.Liga
import com.efihumboldt.appligas.entidades.Partido
import com.efihumboldt.appligas.interfaces.JugadorDAO
import com.efihumboldt.appligas.interfaces.LigaDAO

class JugadorService(private val jugadorDAO: JugadorDAO) {
    suspend fun getJugadoresByTeamID(teamID: Int): List<Jugador> {
        return jugadorDAO.getJugadoresByTeamID(teamID)
    }
}