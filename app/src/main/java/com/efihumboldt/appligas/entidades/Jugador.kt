package com.efihumboldt.appligas.entidades

import java.io.Serializable

data class Jugador (
    var id : Int,
    var nombreCompleto: String,
    var foto : String,
    var dorsal: String,
    var equipo : Int,
    var goles : Int,
    var tarjetasAmarillas : Int,
    var tarjetasRojas : Int
) : Serializable

