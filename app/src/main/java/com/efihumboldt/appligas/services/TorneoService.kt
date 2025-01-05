package com.efihumboldt.appligas.services

import com.efihumboldt.appligas.entidades.Torneo
import com.efihumboldt.appligas.interfaces.TorneoDAO

class TorneoService (private val torneoDAO: TorneoDAO) {
    suspend fun getAllTorneos(): List<Torneo> {
        return torneoDAO.getAllTorneos()
    }
}