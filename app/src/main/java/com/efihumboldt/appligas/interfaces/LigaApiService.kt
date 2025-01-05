package com.efihumboldt.appligas.interfaces
import com.efihumboldt.appligas.entidades.Liga
import retrofit2.http.GET

interface LigaApiService {
    @GET("/general/get_all_ligas.php")
    suspend fun getAllLigas(): List<Liga>



    // Otras operaciones relacionadas con Ligas (insert, update, delete, etc.)
}
