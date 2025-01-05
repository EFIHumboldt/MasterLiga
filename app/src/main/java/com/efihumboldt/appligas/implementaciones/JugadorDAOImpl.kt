package com.efihumboldt.appligas.implementaciones

import android.content.Context
import com.efihumboldt.appligas.entidades.Jugador
import com.efihumboldt.appligas.entidades.Partido
import com.efihumboldt.appligas.interfaces.JugadorApiService
import com.efihumboldt.appligas.interfaces.JugadorDAO
import com.efihumboldt.appligas.interfaces.PartidoApiService
import com.efihumboldt.appligas.interfaces.PartidoDAO

class JugadorDAOImpl(private val apiService: JugadorApiService,
                     private val bd: String?,
                     private val context : Context
): JugadorDAO {

    override suspend fun getJugadoresByTeamID(teamID: Int): List<Jugador> {
        return listOf(
            Jugador(1,"Perez, J.","null","#22",1),
            Jugador(2,"Perez, J.","null","#22",1),
            Jugador(3,"Perez, J.","null","#22",1),)

    }
}