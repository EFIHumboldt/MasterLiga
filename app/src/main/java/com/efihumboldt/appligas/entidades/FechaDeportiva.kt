package com.efihumboldt.appligas.entidades

data class FechaDeportiva(
    val id: Int,
    val valor: String,
    val fechaInicio: String?,
    val fechaFin: String?,
    val mostrar: Boolean,
    val listaPartido: List<Partido>
)