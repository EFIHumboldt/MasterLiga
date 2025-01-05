package com.efihumboldt.appligas.entidades

import java.io.Serializable

data class Noticia(

    var id : Int,
    var imagen : String,
    var titulo : String,
    var cuerpo : String
) : Serializable
