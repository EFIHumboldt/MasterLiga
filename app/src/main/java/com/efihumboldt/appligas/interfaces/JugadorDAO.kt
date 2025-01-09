package com.efihumboldt.appligas.interfaces

import com.efihumboldt.appligas.entidades.Jugador

interface JugadorDAO {
    suspend fun getJugadoresByTeamID(teamID : Int) : List<Jugador>
    suspend fun getGoleadoresByTeamID(teamID : Int) : List<Jugador>
}