package com.efihumboldt.appligas.implementaciones

import android.content.Context
import android.widget.Toast
import com.efihumboldt.appligas.entidades.Jugador
import com.efihumboldt.appligas.entidades.Partido
import com.efihumboldt.appligas.interfaces.JugadorApiService
import com.efihumboldt.appligas.interfaces.JugadorDAO
import com.efihumboldt.appligas.interfaces.PartidoApiService
import com.efihumboldt.appligas.interfaces.PartidoDAO
import okio.IOException

class JugadorDAOImpl(private val apiService: JugadorApiService,
                     private val bd: String?,
                     private val context : Context
): JugadorDAO {

    override suspend fun getJugadoresByTeamID(teamID: Int): List<Jugador> {
        return try {
            val response = apiService.getJugadoresByTeamID(bd, teamID)
            response.map { apiResponse ->
                Jugador(
                    id = apiResponse.id,
                    nombreCompleto = apiResponse.nombreCompleto,
                    foto = apiResponse.foto,
                    dorsal = apiResponse.dorsal,
                    equipo = apiResponse.equipo,
                    goles = apiResponse.goles,
                    tarjetasAmarillas = apiResponse.tarjetasAmarillas,
                    tarjetasRojas = apiResponse.tarjetasRojas
                )
            }
        } catch (e: IOException) {
            Toast.makeText(context, "No se puede conectar con el servidor", Toast.LENGTH_SHORT)
                .show()
            emptyList()
        } catch (e: Exception) {
            Toast.makeText(context, "Error en la solicitud: ${e.message}", Toast.LENGTH_SHORT)
                .show()
            throw e
        }

    }

    override suspend fun getGoleadoresByTeamID(id: Int): List<Jugador> {
        return try {
            val response = apiService.getGoleadoresByTeamID(bd, id)
            response.map { apiResponse ->
                Jugador(
                    id = apiResponse.id,
                    nombreCompleto = apiResponse.nombreCompleto,
                    foto = apiResponse.foto,
                    dorsal = apiResponse.dorsal,
                    equipo = apiResponse.equipo,
                    goles = apiResponse.goles,
                    tarjetasAmarillas = apiResponse.goles,
                    tarjetasRojas = apiResponse.goles,
                )
            }
        } catch (e: IOException) {
            Toast.makeText(context, "No se puede conectar con el servidor", Toast.LENGTH_SHORT)
                .show()
            emptyList()
        } catch (e: Exception) {
            Toast.makeText(context, "Error en la solicitud: ${e.message}", Toast.LENGTH_SHORT)
                .show()
            throw e
        }

    }
}