package com.efihumboldt.appligas.entidades

import java.io.Serializable

data class EquipoSimple(
    var id : Int,
    var zona: String,
    var nombrecompleto: String,
    var nombrecorto: String,
    var escudo : String,
    var color : String) : Serializable