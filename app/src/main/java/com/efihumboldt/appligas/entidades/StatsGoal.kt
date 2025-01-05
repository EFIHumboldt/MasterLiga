package com.efihumboldt.appligas.entidades

data class StatsGoal(
    val golesHechosLocalTotal: Int,
    val promedioGolesHechosLocal: Double,
    val golesHechosVisitaTotal: Int,
    val promedioGolesHechosVisita: Double,
    val golesRecibidosLocalTotal: Int,
    val promedioGolesRecibidosLocal: Double,
    val golesRecibidosVisitaTotal: Int,
    val promedioGolesRecibidosVisita: Double
)