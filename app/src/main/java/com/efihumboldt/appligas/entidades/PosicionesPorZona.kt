package com.efihumboldt.appligas.entidades

data class PosicionesPorZona(
    val zona: String,
    val posiciones: List<Posicion>
)