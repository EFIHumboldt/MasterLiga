package com.efihumboldt.appligas.interfaces

import com.efihumboldt.appligas.entidades.FechaDeportiva
import com.efihumboldt.appligas.entidades.Gol
import com.efihumboldt.appligas.entidades.Partido
import com.efihumboldt.appligas.entidades.StatsGoal

interface PartidoDAO {
    suspend fun getAllMatchs(): List<Partido>
    suspend fun getMatchsByMatchdayID(matchdayID: Int): List<FechaDeportiva>
    suspend fun getLastMatchsByTeamID(teamID: Int?) : List<Partido>
    suspend fun getStatsGoalsByTeamID(teamID: Int?) : StatsGoal
    suspend fun getGoalsByMatchID(matchID: Int?) : List<Gol>
    suspend fun getMatchByMatchID(matchdayID: Int): Partido
    suspend fun getAllMatchsByTeamID(matchdayID: Int): List<Partido>

}