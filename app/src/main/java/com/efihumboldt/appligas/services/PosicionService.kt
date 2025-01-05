package com.efihumboldt.appligas.services

import com.efihumboldt.appligas.entidades.Posicion
import com.efihumboldt.appligas.entidades.PosicionAndColorResponse
import com.efihumboldt.appligas.interfaces.PosicionDAO

class PosicionService(private val posicionDAO: PosicionDAO) {
    suspend fun getAllPosicion(): PosicionAndColorResponse {
        return posicionDAO.getAllPosicion()
    }

    suspend fun getAllPosicionClasificacion() : PosicionAndColorResponse {
        return posicionDAO.getAllPosicionClasificacion()
    }
    suspend fun getPosicionReducido(): List<Posicion> {
        return posicionDAO.getPosicionReducido()
    }
}