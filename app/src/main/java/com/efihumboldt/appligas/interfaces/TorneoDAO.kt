package com.efihumboldt.appligas.interfaces

import com.efihumboldt.appligas.entidades.Torneo

interface TorneoDAO {
    suspend fun getAllTorneos(): List<Torneo>

    suspend fun getTorneosByIDLiga() : List<Torneo>

}