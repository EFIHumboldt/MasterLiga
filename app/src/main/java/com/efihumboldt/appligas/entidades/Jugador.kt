package com.efihumboldt.appligas.entidades

import java.io.Serializable

data class Jugador (
    var id : Int,
    var nombre: String,
    var foto : String,
    var dorsal: String,
    var equipo : Int
) : Serializable
