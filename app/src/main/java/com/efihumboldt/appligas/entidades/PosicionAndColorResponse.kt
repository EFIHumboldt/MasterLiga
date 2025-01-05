package com.efihumboldt.appligas.entidades

data class PosicionAndColorResponse(
    var posicionesPorZona: List<PosicionesPorZona>,
    val coloresDescripcion: List<ColorDescripcion>
)