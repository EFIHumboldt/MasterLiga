package com.efihumboldt.appligas.entidades

data class Partido(
    //No se si van las fotos
    val idPartido: String,
    val idLocal: String,
    val idVisita : String,
    val imgLocal: String,
    val imgVisita: String,
    val nombreLocal: String,
    val nombreVisita: String,
    val colorLocal : String,
    val colorVisita : String,
    val resultadoLocal: String?,
    val resultadoVisita: String?,
    val penalesLocal: String?,
    val penalesVisita : String?,
    val fecha : String?,
    val hora: String?,
    val inicioPrimerTiempo : String?,
    val finPrimerTiempo : String?,
    val inicioSegundoTiempo : String?,
    val finSegundoTiempo : String?,
    val estadioPartido : String?,
    val latitudEstadio : String?,
    val longitudEstadio : String?,
    val infoPartido : String
)