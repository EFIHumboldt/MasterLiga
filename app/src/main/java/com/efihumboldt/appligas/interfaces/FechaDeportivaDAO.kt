package com.efihumboldt.appligas.interfaces

import com.efihumboldt.appligas.entidades.FechaDeportiva

interface FechaDeportivaDAO {
    suspend fun getAllMatchdays(): List<FechaDeportiva>

}