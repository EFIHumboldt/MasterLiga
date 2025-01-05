package com.efihumboldt.appligas.interfaces

import com.efihumboldt.appligas.entidades.Posicion
import com.efihumboldt.appligas.entidades.PosicionAndColorResponse

interface PosicionDAO {
    suspend fun getAllPosicion(): PosicionAndColorResponse
    suspend fun getAllPosicionClasificacion(): PosicionAndColorResponse
    suspend fun getPosicionReducido(): List<Posicion>
}