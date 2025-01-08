package com.efihumboldt.appligas.implementaciones

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.efihumboldt.appligas.entidades.Posicion
import com.efihumboldt.appligas.entidades.PosicionAndColorResponse
import com.efihumboldt.appligas.entidades.PosicionesPorZona
import com.efihumboldt.appligas.interfaces.PosicionApiService
import com.efihumboldt.appligas.interfaces.PosicionDAO
import retrofit2.HttpException
import java.io.IOException

class PosicionDAOImpl(private val apiService: PosicionApiService,
                      bd: String?, private val divisionID : Int,
                      private val context : Context) : PosicionDAO {

    private val bdName : String = bd!!


    override suspend fun getAllPosicion(): PosicionAndColorResponse {
        /*
        val lista1 : List<PosicionesPorZona> = listOf(
            PosicionesPorZona("Zona A", listOf(
                Posicion(1,1,"0.png","Equipo 1",1,1,1,1,1,1,1,1,"#000000"),
                Posicion(2,2,"0.png","Equipo 2",1,1,1,1,1,1,1,1,"#000000"),
                Posicion(3,3,"0.png","Equipo 3",1,1,1,1,1,1,1,1,"#000000"),
                Posicion(4,4,"0.png","Equipo 4",1,1,1,1,1,1,1,1,"#000000"),
                Posicion(5,5,"0.png","Equipo 5",1,1,1,1,1,1,1,1,"#000000"))),
            PosicionesPorZona("Zona B", listOf(
                Posicion(1,6,"0.png","Equipo 6",1,1,1,1,1,1,1,1,"#000000"),
                Posicion(2,7,"0.png","Equipo 7",1,1,1,1,1,1,1,1,"#000000"),
                Posicion(3,8,"0.png","Equipo 8",1,1,1,1,1,1,1,1,"#000000"),
                Posicion(4,9,"0.png","Equipo 9",1,1,1,1,1,1,1,1,"#000000"),
                Posicion(5,10,"0.png","Equipo 10",1,1,1,1,1,1,1,1,"#000000")))
            )


        return PosicionAndColorResponse(posicionesPorZona = lista1, coloresDescripcion = emptyList())

        */
        return try {
            val resultadoApi = apiService.getAllPosicion(bdName, divisionID)


            val modifierPosition = resultadoApi.posicionesPorZona.map { posicion ->

                val i = posicion.posiciones.size

                for (x in 0..<i)
                {
                    posicion.posiciones[x].numeroPosicion = x+1;
                }
                posicion
            }
            resultadoApi.posicionesPorZona = modifierPosition
            resultadoApi
        } catch (e: IOException) {

            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
            return PosicionAndColorResponse(posicionesPorZona = emptyList(), coloresDescripcion = emptyList())
        } catch (e: HttpException) {
            Log.e("error", "HTTP ${e.code()} - ${e.message()}")
            e.printStackTrace()
            return PosicionAndColorResponse(posicionesPorZona = emptyList(), coloresDescripcion = emptyList())
        } catch (e: IOException) {
            Log.e("error", "No se pudo conectar con el servidor: ${e.message}")
            e.printStackTrace()
            return PosicionAndColorResponse(posicionesPorZona = emptyList(), coloresDescripcion = emptyList())
        } catch (e: Exception) {
            Log.e("error", "Excepción desconocida: ${e.message}")
            e.printStackTrace()
            return PosicionAndColorResponse(posicionesPorZona = emptyList(), coloresDescripcion = emptyList())
        }


    }

    override suspend fun getAllPosicionClasificacion(): PosicionAndColorResponse {
        return try {
            val resultadoApi = apiService.getAllPosicionClasificacion(bdName, divisionID)


            val modifierPosition = resultadoApi.posicionesPorZona.map { posicion ->

                val i = posicion.posiciones.size

                for (x in 0..<i)
                {
                    posicion.posiciones[x].numeroPosicion = x+1;
                }
                posicion
            }
            resultadoApi.posicionesPorZona = modifierPosition
            resultadoApi
        } catch (e: IOException) {

            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
            return PosicionAndColorResponse(posicionesPorZona = emptyList(), coloresDescripcion = emptyList())
        } catch (e: HttpException) {
            Log.e("error", "HTTP ${e.code()} - ${e.message()}")
            e.printStackTrace()
            return PosicionAndColorResponse(posicionesPorZona = emptyList(), coloresDescripcion = emptyList())
        } catch (e: IOException) {
            Log.e("error", "No se pudo conectar con el servidor: ${e.message}")
            e.printStackTrace()
            return PosicionAndColorResponse(posicionesPorZona = emptyList(), coloresDescripcion = emptyList())
        } catch (e: Exception) {
            Log.e("error", "Excepción desconocida: ${e.message}")
            e.printStackTrace()
            return PosicionAndColorResponse(posicionesPorZona = emptyList(), coloresDescripcion = emptyList())
        }
    }


    override suspend fun getPosicionReducido() : List<Posicion> {

        return listOf(
            Posicion(
                numeroPosicion = 1,
                equipo = 1,
                escudo = "escudo_equipo_1",
                nombre = "Equipo 1",
                partidosJugados = 10,
                partidosGanados = 5,
                partidosEmpatados = 3,
                partidosPerdidos = 2,
                golesAFavor = 20,
                golesEnContra = 15,
                diferenciaDeGoles = 5,
                puntos = 13,
                colorBarra = "#ffffff"
            ),
        )
    }
}