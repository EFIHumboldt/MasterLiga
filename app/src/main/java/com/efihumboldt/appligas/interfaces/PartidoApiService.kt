package com.efihumboldt.appligas.interfaces

import com.efihumboldt.appligas.entidades.FechaDeportiva
import com.efihumboldt.appligas.entidades.Gol
import com.efihumboldt.appligas.entidades.Partido
import com.efihumboldt.appligas.entidades.StatsGoal
import retrofit2.http.GET
import retrofit2.http.Query

interface PartidoApiService {

    @GET("apis/get_last_matchs_by_team_id.php")
    suspend fun getLastMatchsByTeamID(@Query("bd") bd: String?, @Query("teamID") teamID: Int?): List<Partido>

    @GET("apis/get_all_matchs.php")
     suspend fun getAllMatchs(@Query("bd") bd: String?, @Query("teamID") teamID: Int): List<Partido>

    @GET("apis/get_all_matchs_by_team_id.php")
    suspend fun getAllMatchsByTeamID(@Query("bd") bd: String?, @Query("teamID") teamID: Int): List<Partido>

    @GET("apis/get_stats_goals_by_team_id.php")
    suspend fun getStatsGoalsByTeamID(@Query("bd") bd: String?, @Query("teamID") teamID: Int?): StatsGoal

    @GET("apis/get_matchs_by_matchday_id.php")
    suspend fun getMatchsByMatchdayID(@Query("bd") bd: String?, @Query("matchdayID") matchdayID: Int?): List<FechaDeportiva>

    @GET("apis/get_goals_by_match_id.php")
    suspend fun getGoalsByMatchID(@Query("bd") bd: String?, @Query("matchID") matchID: Int?): List<Gol>

    @GET("apis/get_match_by_match_id.php")
    suspend fun getMatchByMatchID(@Query("bd") bd: String?, @Query("matchID") matchID: Int?): Partido
}