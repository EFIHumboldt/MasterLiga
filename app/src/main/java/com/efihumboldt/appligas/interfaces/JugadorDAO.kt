package com.efihumboldt.appligas.interfaces

import com.efihumboldt.appligas.entidades.Jugador

interface JugadorDAO {
    suspend fun getJugadoresByTeamID(teamID : Int) : List<Jugador>
}