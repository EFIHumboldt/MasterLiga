package com.efihumboldt.appligas.implementaciones

import android.content.Context
import android.widget.Toast
import com.efihumboldt.appligas.entidades.FechaDeportiva
import com.efihumboldt.appligas.interfaces.FechaDeportivaApiService
import com.efihumboldt.appligas.interfaces.FechaDeportivaDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class FechaDeportivaDAOImpl(private val apiService: FechaDeportivaApiService,
                            private val bd: String?,
                            private val id_torneo : Int?,
                            private val context : Context,
                            ) : FechaDeportivaDAO {





    override suspend fun getAllMatchdays(): List<FechaDeportiva> {
        return try {
            withContext(Dispatchers.IO) {
                val response = apiService.getAllMatchdays(bd, id_torneo)
                response.map { apiResponse ->

                    FechaDeportiva(
                        id = apiResponse.id,
                        valor = apiResponse.valor,
                        fechaInicio = apiResponse.fechaInicio,
                        fechaFin = apiResponse.fechaFin,
                        mostrar = apiResponse.mostrar,
                        listaPartido = listOf()
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