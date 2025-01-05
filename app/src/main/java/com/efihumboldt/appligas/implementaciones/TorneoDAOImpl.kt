package com.efihumboldt.appligas.implementaciones

import android.content.Context
import android.widget.Toast
import com.efihumboldt.appligas.entidades.Torneo
import com.efihumboldt.appligas.interfaces.TorneoApiService
import com.efihumboldt.appligas.interfaces.TorneoDAO
import java.io.IOException

class TorneoDAOImpl(private val apiService: TorneoApiService,
                    bd : String, id : String,
                    private val context: Context
)  : TorneoDAO {

    private val bdName : String = bd
    private val id_liga : String = id

    /*
    override suspend fun getAllTorneos(): List<Torneo> {

        return try {
            val response = apiService.getAllTorneos(bdName)
            response.map { apiResponse ->
                Torneo(
                    torneoID = apiResponse.torneoID,
                    divisionID = apiResponse.divisionID,
                    nombreTorneoDivision = apiResponse.nombreTorneoDivision,
                    imgTorneo = 0,
                    reglamento = apiResponse.reglamento,
                    color = apiResponse.color
                )
            }
        } catch (e: IOException) {

            Toast.makeText(context, "No se pudo conectar con el servidor", Toast.LENGTH_SHORT).show()
            emptyList()
        } catch (e: Exception) {

            Toast.makeText(context, "No hay datos de esta liga", Toast.LENGTH_SHORT).show()
            emptyList()
        }
    }

    override suspend fun getTorneosByIDLiga(): List<Torneo> {
        return try {
            val response = apiService.getTorneosByIDLiga(bdName, id_liga)

            response.map { apiResponse ->
                Torneo(
                    torneoID = apiResponse.torneoID,
                    divisionID = apiResponse.divisionID,
                    nombreTorneoDivision = apiResponse.nombreTorneoDivision,
                    imgTorneo = 0,
                    reglamento = apiResponse.reglamento,
                    color = apiResponse.color
                )
            }

        } catch (e: IOException) {

            Toast.makeText(context, "No se pudo conectar con el servidor", Toast.LENGTH_SHORT).show()
            emptyList()
        } catch (e: Exception) {

            Toast.makeText(context, "No hay datos de esta liga", Toast.LENGTH_SHORT).show()
            emptyList()
        }
    }
     */

    override suspend fun getAllTorneos(): List<Torneo> {
        // Datos hardcodeados para pruebas
        return listOf(
            Torneo(
                torneoID = 1,
                divisionID = 101,
                nombreTorneoDivision = "Torneo Default 1",
                imgTorneo = 0,
                reglamento = "Reglamento del Torneo 1",
                color = "#000000"
            ),
            Torneo(
                torneoID = 2,
                divisionID = 102,
                nombreTorneoDivision = "Torneo Default 2",
                imgTorneo = 0,
                reglamento = "Reglamento del Torneo 2",
                color = "#00FF00"
            )
        )
    }

    override suspend fun getTorneosByIDLiga(): List<Torneo> {
        // Solo devuelve datos hardcodeados si el ID de la liga es "1"
        return if (id_liga == "1") {
            listOf(
                Torneo(
                    torneoID = 1,
                    divisionID = 201,
                    nombreTorneoDivision = "Torneo Liga 1",
                    imgTorneo = 0,
                    reglamento = "Reglamento Liga 1",
                    color = "#0000FF"
                ),
                Torneo(
                    torneoID = 2,
                    divisionID = 202,
                    nombreTorneoDivision = "Torneo Liga 1 División 2",
                    imgTorneo = 0,
                    reglamento = "Reglamento Liga 1 División 2",
                    color = "#FFFF00"
                )
            )
        } else {
            // Devuelve una lista vacía si el ID no es "1"
            emptyList()
        }
    }

}