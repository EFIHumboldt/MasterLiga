package com.efihumboldt.appligas.interfaces

import com.efihumboldt.appligas.entidades.Noticia
import retrofit2.http.GET
import retrofit2.http.Query

interface NoticiaApiService {

    @GET("apis/get_all_news.php")
    suspend fun getAllNoticias(@Query("bd") bd: String?, @Query("divisionID") divisionID: Int?): List<Noticia>
}