package com.efihumboldt.appligas.services

import com.efihumboldt.appligas.entidades.Liga
import com.efihumboldt.appligas.interfaces.LigaDAO

class LigaService(private val ligaDAO: LigaDAO) {
    fun getAllLigas(): List<Liga> {
        return ligaDAO.getAllLigas()
    }

}
