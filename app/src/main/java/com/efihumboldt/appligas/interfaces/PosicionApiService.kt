package com.efihumboldt.appligas.interfaces

import com.efihumboldt.appligas.entidades.Posicion
import com.efihumboldt.appligas.entidades.PosicionAndColorResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PosicionApiService {

    @GET("apis/get_all_posicion_updated.php")
    suspend fun getAllPosicion(@Query("bd") bd: String, @Query("divisionID") divisionID: Int): PosicionAndColorResponse

    @GET("apis/get_all_posicion_clasificacion.php")
    suspend fun getAllPosicionClasificacion(@Query("bd") bd: String, @Query("divisionID") divisionID: Int): PosicionAndColorResponse

    @GET("apis/get_posicion_reducido.php")
    suspend fun getPosicionReducido(@Query("bd") bd: String, @Query("id_equipo") id:Int): List<Posicion>

}