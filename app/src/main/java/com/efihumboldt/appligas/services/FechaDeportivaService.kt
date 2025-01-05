package com.efihumboldt.appligas.services

import com.efihumboldt.appligas.entidades.FechaDeportiva
import com.efihumboldt.appligas.interfaces.FechaDeportivaDAO

class FechaDeportivaService(private val fechaDAO: FechaDeportivaDAO) {
    suspend fun getAllMatchdays(): List<FechaDeportiva> {
        return fechaDAO.getAllMatchdays()
    }

}
