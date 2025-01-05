package com.efihumboldt.appligas.entidades

data class Posicion(
    var  numeroPosicion: Int,
    var  equipo : Int,
    var  escudo: String?,
    var  nombre: String,
    var  partidosJugados: Int,
    var  partidosGanados: Int,
    var  partidosEmpatados: Int,
    var  partidosPerdidos: Int,
    var  golesAFavor: Int,
    var  golesEnContra: Int,
    var  diferenciaDeGoles: Int,
    var  puntos: Int,
    var  colorBarra: String
)