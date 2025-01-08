package com.efihumboldt.appligas.implementaciones

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.efihumboldt.appligas.entidades.EquipoSimple
import com.efihumboldt.appligas.interfaces.EquipoSimpleApiService
import com.efihumboldt.appligas.interfaces.EquipoSimpleDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import kotlin.math.log

class EquipoSimpleDAOImpl(private val apiService: EquipoSimpleApiService,
                          private val bd: String?,
                          private val divisionID: Int?,
                          private val context : Context): EquipoSimpleDAO{

    override suspend fun getAllTeams(): List<EquipoSimple> {

        return try {
            withContext(Dispatchers.IO) {

                val response = apiService.getAllTeams(bd, divisionID)

                response.map { apiResponse ->
                    EquipoSimple(
                        id = apiResponse.id,
                        zona = apiResponse.zona,
                        nombrecompleto = apiResponse.nombrecompleto,
                        nombrecorto = apiResponse.nombrecorto,
                        escudo = apiResponse.escudo,
                        color = apiResponse.color
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
         /*
        return listOf(
            EquipoSimple(1,"A","Equipo 1","Equipo 1", "0.png", "#000000"),
            EquipoSimple(2,"A","Equipo 2","Equipo 2", "0.png", "#000000"),
            EquipoSimple(3,"A","Equipo 3","Equipo 3", "0.png", "#000000"),
            EquipoSimple(4,"A","Equipo 4","Equipo 4", "0.png", "#000000"),
            EquipoSimple(5,"A","Equipo 5","Equipo 5", "0.png", "#000000"),
            EquipoSimple(6,"B","Equipo 6","Equipo 6", "0.png", "#000000"),
            EquipoSimple(7,"B","Equipo 7","Equipo 7", "0.png", "#000000"),
            EquipoSimple(8,"B","Equipo 8","Equipo 8", "0.png", "#000000"),
            EquipoSimple(9,"B","Equipo 9","Equipo 9", "0.png", "#000000"),
            EquipoSimple(10,"B","Equipo 10","Equipo 10", "0.png", "#000000"))
        */
    }


    override suspend fun getTeamByTeamID(teamID : Int): List<EquipoSimple> {
        return try {
            withContext(Dispatchers.IO) {
                val response = apiService.getTeamByTeamID(bd, divisionID, teamID)
                response.map { apiResponse ->
                    EquipoSimple(
                        id = apiResponse.id,
                        zona = apiResponse.zona,
                        nombrecompleto = apiResponse.nombrecompleto,
                        nombrecorto = apiResponse.nombrecorto,
                        escudo = apiResponse.escudo,
                        color = apiResponse.color
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