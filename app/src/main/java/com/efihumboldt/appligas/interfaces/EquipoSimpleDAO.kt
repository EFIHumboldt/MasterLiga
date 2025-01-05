package com.efihumboldt.appligas.interfaces

import com.efihumboldt.appligas.entidades.EquipoSimple

interface EquipoSimpleDAO {

    suspend fun getAllTeams(): List<EquipoSimple>
    suspend fun getTeamByTeamID(teamID: Int): List<EquipoSimple>
}