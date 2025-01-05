package com.efihumboldt.appligas.interfaces

import com.efihumboldt.appligas.entidades.Liga

interface LigaDAO {
    fun getAllLigas(): List<Liga>

}