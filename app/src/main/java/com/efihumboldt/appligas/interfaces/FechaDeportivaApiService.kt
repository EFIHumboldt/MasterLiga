package com.efihumboldt.appligas.interfaces

import com.efihumboldt.appligas.entidades.FechaDeportiva
import retrofit2.http.GET
import retrofit2.http.Query

interface FechaDeportivaApiService {

    @GET("apis/get_all_matchdays.php")
    suspend fun getAllMatchdays(@Query("bd") bd: String?, @Query("divisionID") divisionID: Int?): List<FechaDeportiva>


}