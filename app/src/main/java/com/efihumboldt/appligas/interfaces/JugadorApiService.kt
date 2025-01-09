package com.efihumboldt.appligas.interfaces

import com.efihumboldt.appligas.entidades.FechaDeportiva
import com.efihumboldt.appligas.entidades.Jugador
import com.efihumboldt.appligas.entidades.Liga
import com.efihumboldt.appligas.entidades.Partido
import retrofit2.http.GET
import retrofit2.http.Query

interface JugadorApiService {
    @GET("/apis/get_jugadores_by_team_id.php")
    suspend fun getJugadoresByTeamID(@Query("bd") bd: String?, @Query("teamID") teamID: Int?): List<Jugador>

    @GET("/apis/get_goleadores_by_team_id.php")
    suspend fun getGoleadoresByTeamID(@Query("bd") bd: String?, @Query("teamID") teamID: Int?): List<Jugador>
}