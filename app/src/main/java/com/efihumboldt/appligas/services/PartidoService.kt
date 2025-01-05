package com.efihumboldt.appligas.services

import com.efihumboldt.appligas.entidades.FechaDeportiva
import com.efihumboldt.appligas.entidades.Gol
import com.efihumboldt.appligas.interfaces.PartidoDAO
import com.efihumboldt.appligas.entidades.Partido
import com.efihumboldt.appligas.entidades.StatsGoal

class PartidoService(private val partidoDAO: PartidoDAO) {
    suspend fun getAllMatchs(): List<Partido> {
        return partidoDAO.getAllMatchs()
    }
    suspend fun getMatchsByMatchdayID(matchdayID : Int): List<FechaDeportiva> {
        return partidoDAO.getMatchsByMatchdayID(matchdayID)
    }
    suspend fun getLastMatchsByTeamID(teamID: Int): List<Partido> {
        return partidoDAO.getLastMatchsByTeamID(teamID)
    }

    suspend fun getStatsGoalsByTeamID(teamID: Int) : StatsGoal {
        return partidoDAO.getStatsGoalsByTeamID(teamID)
    }

    suspend fun getGoalsByMatchID(matchID : Int) : List<Gol> {
        return partidoDAO.getGoalsByMatchID(matchID)
    }

    suspend fun getMatchByMatchID(matchID : Int) : Partido {
        return partidoDAO.getMatchByMatchID(matchID)
    }
    suspend fun getAllMatchsByTeamID(matchID : Int) : List<Partido> {
        return partidoDAO.getAllMatchsByTeamID(matchID)
    }


}