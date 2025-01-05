package com.efihumboldt.appligas.interfaces

import com.efihumboldt.appligas.entidades.Torneo
import retrofit2.http.GET
import retrofit2.http.Query

interface TorneoApiService {

    @GET("apis/get_all_torneos.php")
    suspend fun getAllTorneos(@Query("bd") bd: String): List<Torneo>

    @GET("apis/get_torneos_by_id_liga.php")
    suspend fun getTorneosByIDLiga(@Query("bd") bd: String, @Query("id_liga") id:String): List<Torneo>

}