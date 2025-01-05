package com.efihumboldt.appligas.services

import com.efihumboldt.appligas.entidades.EquipoSimple
import com.efihumboldt.appligas.interfaces.EquipoSimpleDAO

class EquipoSimpleService (private val equipoDAO: EquipoSimpleDAO) {

    suspend fun getAllEquipos(): List<EquipoSimple> {
        return equipoDAO.getAllTeams()
    }

    suspend fun getTeamByID(teamID : Int): List<EquipoSimple> {
        return equipoDAO.getTeamByTeamID(teamID)
    }
}