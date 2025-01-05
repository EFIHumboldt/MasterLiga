package com.efihumboldt.appligas.implementaciones


import android.content.Context
import android.widget.Toast
import com.efihumboldt.appligas.entidades.Noticia
import com.efihumboldt.appligas.interfaces.NoticiaApiService
import com.efihumboldt.appligas.interfaces.NoticiaDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class NoticiaDAOImpl(private val apiService: NoticiaApiService,
                     private val bd: String?,
                     private val tournamentID : Int?,
                     private val context : Context): NoticiaDAO {


    override suspend fun getAllNoticias(tournamentID: Int?): List<Noticia> {
        return try {
            withContext(Dispatchers.IO) {
                val response = apiService.getAllNoticias(bd, tournamentID)
                response.map { apiResponse ->
                    Noticia(
                        id = apiResponse.id,
                        imagen = apiResponse.imagen,
                        titulo = apiResponse.titulo,
                        cuerpo = apiResponse.cuerpo
                    )
                }
            }
        } catch (e: IOException) {

            Toast.makeText(context, "No se pudo conectar con el servidor", Toast.LENGTH_SHORT).show()
            emptyList()
        } catch (e: Exception) {

            Toast.makeText(context, "Error en la solicitud: ${e.message}", Toast.LENGTH_SHORT).show()
            throw e
        }
    }

}
