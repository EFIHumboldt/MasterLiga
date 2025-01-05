package com.efihumboldt.appligas.implementaciones

import android.content.Context
import android.widget.Toast
import com.efihumboldt.appligas.entidades.Liga
import com.efihumboldt.appligas.interfaces.LigaApiService
import com.efihumboldt.appligas.interfaces.LigaDAO
import kotlinx.coroutines.runBlocking
import java.io.IOException

class LigaDAOImpl(private val apiService: LigaApiService,
                  private val context: Context) : LigaDAO {

    override fun getAllLigas(): List<Liga> {
        // return runBlocking {
        //    try {
        //        val response = apiService.getAllLigas()
        //        response.map { apiResponse ->
        //            Liga(
        //                ID = apiResponse.ID,
        //                nombre = apiResponse.nombre,
        //                link = apiResponse.link,
        //                logo = apiResponse.logo,
        //                region = apiResponse.region,
        //                reglamento = apiResponse.reglamento,
        //                color = apiResponse.color
        //            )
        //        }
        //    } catch (e: IOException) {
        //        Toast.makeText(context, "No se pudo conectar con el servidor", Toast.LENGTH_SHORT).show()
        //        emptyList()
        //    } catch (e: Exception) {
        //        Toast.makeText(context, "No hay datos de esta liga", Toast.LENGTH_SHORT).show()
        //        emptyList()
        //    }
        // }

        //
        return listOf(
            Liga(
                ID = 1,
                nombre = "Liga 1",
                link = "http://liga1.com",
                logo = "http://liga1.com/logo.png",
                region = "Región 1",
                reglamento = "Reglamento 1",
                color = "#FF0000"
            ),
            Liga(
                ID = 2,
                nombre = "Liga 2",
                link = "http://liga2.com",
                logo = "http://liga2.com/logo.png",
                region = "Región 2",
                reglamento = "Reglamento 2",
                color = "#00FF00"
            )
        )
    }

}