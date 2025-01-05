package com.efihumboldt.appligas.entidades

import java.io.Serializable

data class Liga (

    val ID: Int,
    val nombre : String,
    val link : String,
    val logo : String,
    val region : String,
    val reglamento : String,
    val color : String

) : Serializable