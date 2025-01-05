package com.efihumboldt.appligas.implementaciones

import android.content.Context
import android.widget.Toast
import com.efihumboldt.appligas.entidades.FechaDeportiva
import com.efihumboldt.appligas.entidades.Gol
import com.efihumboldt.appligas.entidades.Partido
import com.efihumboldt.appligas.entidades.StatsGoal
import com.efihumboldt.appligas.interfaces.PartidoApiService
import com.efihumboldt.appligas.interfaces.PartidoDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class PartidoDAOImpl(private val apiService: PartidoApiService,
                     private val bd: String?,
                     private val context : Context
): PartidoDAO {

    override suspend fun getLastMatchsByTeamID(teamID: Int?): List<Partido> {
        return listOf( Partido("1","1","2","","","Equipo 1", "Equipo 2","#000000","#000000","3","3",null,null,"01/01/2025","08:00",null,null,null,null,null,null,null,"Finalizado"),
            Partido("1","1","2","","","Equipo 1", "Equipo 2","#000000","#000000","2","3",null,null,"01/01/2025","08:00",null,null,null,null,null,null,null,"Finalizado"),
            Partido("1","1","2","","","Equipo 1", "Equipo 2","#000000","#000000","2","0",null,null,"01/01/2025","08:00",null,null,null,null,null,null,null,"Finalizado"),
            )

        /* CAMBIAR DESPUES
        return try {
            val response = apiService.getLastMatchsByTeamID(bd, teamID)

            response.map { apiResponse ->
                Partido(
                    idPartido = apiResponse.idPartido,
                    idLocal  = apiResponse.idLocal,
                    idVisita = apiResponse.idVisita,
                    imgLocal = apiResponse.imgLocal,
                    imgVisita = apiResponse.imgVisita,
                    nombreLocal = apiResponse.nombreLocal,
                    nombreVisita = apiResponse.nombreVisita,
                    colorLocal = apiResponse.colorLocal,
                    colorVisita = apiResponse.colorVisita,
                    resultadoLocal = apiResponse.resultadoLocal,
                    resultadoVisita = apiResponse.resultadoVisita,
                    penalesLocal = apiResponse.penalesLocal,
                    penalesVisita = apiResponse.penalesVisita,
                    fecha = apiResponse.fecha,
                    hora = apiResponse.hora,
                    inicioPrimerTiempo = apiResponse.inicioPrimerTiempo,
                    finPrimerTiempo = apiResponse.finPrimerTiempo,
                    inicioSegundoTiempo = apiResponse.inicioSegundoTiempo,
                    finSegundoTiempo = apiResponse.finSegundoTiempo,
                    estadioPartido = apiResponse.estadioPartido,
                    latitudEstadio = apiResponse.latitudEstadio,
                    longitudEstadio = apiResponse.longitudEstadio,
                    infoPartido = apiResponse.infoPartido
                )
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


    override suspend fun getMatchsByMatchdayID(matchdayID: Int): List<FechaDeportiva> {
        return listOf(FechaDeportiva(0,"Miercoles 01 de Enero","01/01", "03/01",false,
                        listOf( Partido("1","1","2","","","Equipo 1", "Equipo 2","#000000","#000000","3","3",null,null,"01/01/2025","08:00",null,null,null,null,null,null,null,"Finalizado"),
                                Partido("1","1","2","","","Equipo 1", "Equipo 2","#000000","#000000","2","2",null,null,"01/01/2025","08:00",null,null,null,null,null,null,null,"Finalizado"))),
                    FechaDeportiva(1,"Jueves 02 de Enero","01/01", "03/01",false,
                        listOf( Partido("1","3","4","","","Equipo 3", "Equipo 4","#000000","#000000",null,null,null,null,"01/01/2025","08:00",null,null,null,null,null,null,null,"08:00"),
                                Partido("1","3","4","","","Equipo 3", "Equipo 4","#000000","#000000",null,null,null,null,"01/01/2025","08:00",null,null,null,null,null,null,null,"08:00"))),)
        /*
        return try {
            withContext(Dispatchers.IO) {
                val response = apiService.getMatchsByMatchdayID(bd, matchdayID)
                response.map { apiResponse ->
                    FechaDeportiva(
                        id = 0,
                        valor = apiResponse.valor,
                        fechaInicio = "01/01",
                        fechaFin = "03/01",
                        mostrar = false,
                        /*
                        listaPartido = apiResponse.listaPartido.map { partidoResponse ->
                            Partido(
                                idPartido = partidoResponse.idPartido,
                                idLocal  = partidoResponse.idLocal,
                                idVisita = partidoResponse.idVisita,
                                imgLocal = partidoResponse.imgLocal,
                                imgVisita = partidoResponse.imgVisita,
                                nombreLocal = partidoResponse.nombreLocal,
                                nombreVisita = partidoResponse.nombreVisita,
                                colorLocal = partidoResponse.colorLocal,
                                colorVisita = partidoResponse.colorVisita,
                                resultadoLocal = partidoResponse.resultadoLocal,
                                resultadoVisita = partidoResponse.resultadoVisita,
                                penalesLocal = partidoResponse.penalesLocal,
                                penalesVisita = partidoResponse.penalesVisita,
                                fecha = partidoResponse.fecha,
                                inicioPrimerTiempo = partidoResponse.inicioPrimerTiempo,
                                finPrimerTiempo = partidoResponse.finPrimerTiempo,
                                hora = partidoResponse.hora,
                                inicioSegundoTiempo = partidoResponse.inicioSegundoTiempo,
                                finSegundoTiempo = partidoResponse.finSegundoTiempo,
                                estadioPartido = partidoResponse.estadioPartido,
                                latitudEstadio = partidoResponse.latitudEstadio,
                                longitudEstadio = partidoResponse.longitudEstadio,
                                infoPartido = partidoResponse.infoPartido
                            )
                        }*/
                        listaPartido =  listOf(Partido("1","1","2","","","Equipo 1", "Equipo 2","#000000","#000000",null,null,null,null,"01/01/2025","08:00",null,null,null,null,null,null,null,"hola"),
                            Partido("1","1","2","","","Equipo 1", "Equipo 2","#000000","#000000",null,null,null,null,"01/01/2025","08:00",null,null,null,null,null,null,null,"hola")))
                }
            }
        }catch (e: IOException) {

            Toast.makeText(context, "No se pudo conectar con el servidor", Toast.LENGTH_SHORT).show()
            emptyList()
        } catch (e: Exception) {

            Toast.makeText(context, "Error en la solicitud: ${e.message}", Toast.LENGTH_SHORT).show()
            throw e
        }

         */
    }


    override suspend fun getStatsGoalsByTeamID(teamID: Int?): StatsGoal {

        return StatsGoal(1,
            0.5,
            1,
            0.5,
            1,
            0.5,
            1,
            0.5)
        /*CAMBIAR DESPUES
        return try {
            withContext(Dispatchers.IO) {
                val response = apiService.getStatsGoalsByTeamID(bd, teamID)
                StatsGoal(
                    golesHechosLocalTotal = response.golesHechosLocalTotal,
                    promedioGolesHechosLocal = response.promedioGolesHechosLocal,
                    golesHechosVisitaTotal = response.golesHechosVisitaTotal,
                    promedioGolesHechosVisita = response.promedioGolesHechosVisita,
                    golesRecibidosLocalTotal = response.golesRecibidosLocalTotal,
                    promedioGolesRecibidosLocal = response.promedioGolesRecibidosLocal,
                    golesRecibidosVisitaTotal = response.golesRecibidosVisitaTotal,
                    promedioGolesRecibidosVisita = response.promedioGolesRecibidosVisita
                )
            }
        } catch (e: IOException) {

            Toast.makeText(context, "No se pudo conectar con el servidor", Toast.LENGTH_SHORT).show()
            throw e
        } catch (e: Exception) {

            Toast.makeText(context, "Error en la solicitud: ${e.message}", Toast.LENGTH_SHORT).show()
            throw e
        }

         */
    }


    override suspend fun getGoalsByMatchID(matchID: Int?): List<Gol> {
        return try {
            withContext(Dispatchers.IO) {
                val response = apiService.getGoalsByMatchID(bd, matchID)
                response.map { apiResponse ->
                    Gol(
                        minuto = apiResponse.minuto,
                        lado  = apiResponse.lado,
                        tiempo = apiResponse.tiempo
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

    override suspend fun getAllMatchs(): List<Partido> {

        return listOf()
    }

    override suspend fun getMatchByMatchID(matchdayID: Int): Partido {
        return try {
            withContext(Dispatchers.IO) {
                val response = apiService.getMatchByMatchID(bd, matchdayID)
                Partido(
                    idPartido = response.idPartido,
                    idLocal  = response.idLocal,
                    idVisita = response.idVisita,
                    imgLocal = response.imgLocal,
                    imgVisita = response.imgVisita,
                    nombreLocal = response.nombreLocal,
                    nombreVisita = response.nombreVisita,
                    colorLocal = response.colorLocal,
                    colorVisita = response.colorVisita,
                    resultadoLocal = response.resultadoLocal,
                    resultadoVisita = response.resultadoVisita,
                    penalesLocal = response.penalesLocal,
                    penalesVisita = response.penalesVisita,
                    fecha = response.fecha,
                    inicioPrimerTiempo = response.inicioPrimerTiempo,
                    finPrimerTiempo = response.finPrimerTiempo,
                    hora = response.hora,
                    inicioSegundoTiempo = response.inicioSegundoTiempo,
                    finSegundoTiempo = response.finSegundoTiempo,
                    estadioPartido = response.estadioPartido,
                    latitudEstadio = response.latitudEstadio,
                    longitudEstadio = response.longitudEstadio,
                    infoPartido = response.infoPartido
                )
            }
        }catch (e: IOException) {

            Toast.makeText(context, "No se pudo conectar con el servidor", Toast.LENGTH_SHORT).show()
            throw e
        } catch (e: Exception) {

            Toast.makeText(context, "Error en la solicitud: ${e.message}", Toast.LENGTH_SHORT).show()
            throw e
        }
    }

    override suspend fun getAllMatchsByTeamID(matchdayID: Int): List<Partido> {
        return listOf( Partido("1","1","2","","","Equipo 1", "Equipo 2","#000000","#000000","3","3",null,null,"01/01/2025","08:00",null,null,null,null,null,null,null,"Finalizado"),
            Partido("1","1","2","","","Equipo 1", "Equipo 2","#000000","#000000","2","3",null,null,"01/01/2025","08:00",null,null,null,null,null,null,null,"Finalizado"),
            Partido("1","1","2","","","Equipo 1", "Equipo 2","#000000","#000000","2","0",null,null,"01/01/2025","08:00",null,null,null,null,null,null,null,"Finalizado"),
        )
        /*
        return try {
            withContext(Dispatchers.IO) {
                val response = apiService.getAllMatchsByTeamID(bd, matchdayID)
                response.map { apiResponse ->
                    Partido(
                        idPartido = apiResponse.idPartido,
                        idLocal  = apiResponse.idLocal,
                        idVisita = apiResponse.idVisita,
                        imgLocal = apiResponse.imgLocal,
                        imgVisita = apiResponse.imgVisita,
                        nombreLocal = apiResponse.nombreLocal,
                        nombreVisita = apiResponse.nombreVisita,
                        colorLocal = apiResponse.colorLocal,
                        colorVisita = apiResponse.colorVisita,
                        resultadoLocal = apiResponse.resultadoLocal,
                        resultadoVisita = apiResponse.resultadoVisita,
                        penalesLocal = apiResponse.penalesLocal,
                        penalesVisita = apiResponse.penalesVisita,
                        fecha = apiResponse.fecha,
                        hora = apiResponse.hora,
                        inicioPrimerTiempo = apiResponse.inicioPrimerTiempo,
                        finPrimerTiempo = apiResponse.finPrimerTiempo,
                        inicioSegundoTiempo = apiResponse.inicioSegundoTiempo,
                        finSegundoTiempo = apiResponse.finSegundoTiempo,
                        estadioPartido = apiResponse.estadioPartido,
                        latitudEstadio = apiResponse.latitudEstadio,
                        longitudEstadio = apiResponse.longitudEstadio,
                        infoPartido = apiResponse.infoPartido
                    )
                }
            }
        }catch (e: IOException) {

            Toast.makeText(context, "No se pudo conectar con el servidor", Toast.LENGTH_SHORT).show()
            emptyList()
        } catch (e: Exception) {

            Toast.makeText(context, "Error en la solicitud: ${e.message}", Toast.LENGTH_SHORT).show()
            emptyList()
        }

         */
    }
}