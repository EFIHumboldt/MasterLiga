package com.efihumboldt.appligas.interfaces

import com.efihumboldt.appligas.entidades.EquipoSimple
import retrofit2.http.GET
import retrofit2.http.Query

interface EquipoSimpleApiService {

    @GET("apis/get_all_teams.php")
    suspend fun getAllTeams(@Query("bd") bd: String?, @Query("divisionID") divisionID: Int?): List<EquipoSimple>

    @GET("apis/get_team_by_team_id.php")
    suspend fun getTeamByTeamID(@Query("bd") bd: String?, @Query("divisionID") divisionID: Int?, @Query("teamID") teamID : Int): List<EquipoSimple>

}