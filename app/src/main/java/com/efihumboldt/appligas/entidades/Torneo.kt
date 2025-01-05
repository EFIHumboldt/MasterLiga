package com.efihumboldt.appligas.entidades

import java.io.Serializable

data class Torneo(
    val torneoID : Int,
    val divisionID : Int,
    var imgTorneo : Int,
    var nombreTorneoDivision: String,
    var reglamento : String,
    var color: String) : Serializable