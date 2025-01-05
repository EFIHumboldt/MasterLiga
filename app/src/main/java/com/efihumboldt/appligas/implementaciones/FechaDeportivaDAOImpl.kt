package com.efihumboldt.appligas.implementaciones

import android.content.Context
import android.widget.Toast
import com.efihumboldt.appligas.entidades.FechaDeportiva
import com.efihumboldt.appligas.entidades.Partido
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

        return listOf(
            FechaDeportiva(1, "1", "01/01/2025","01/01/2025",true,
                    listOf(Partido("1","1","2","","","Equipo 1", "Equipo 2","#000000","#000000",null,null,null,null,"01/01/2025","08:00",null,null,null,null,null,null,null,"hola"),
                           Partido("1","1","2","","","Equipo 1", "Equipo 2","#000000","#000000",null,null,null,null,"01/01/2025","08:00",null,null,null,null,null,null,null,"hola"))),
            FechaDeportiva(2, "2", "01/01/2025","01/01/2025",true, emptyList()),
            FechaDeportiva(3, "3", "01/01/2025","01/01/2025",true, emptyList()),
            FechaDeportiva(4, "4", "01/01/2025","01/01/2025",true, emptyList()),
            )
        /* CAMBIAR DESPUES
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

         */
    }
}