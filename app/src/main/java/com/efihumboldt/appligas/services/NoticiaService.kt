package com.efihumboldt.appligas.services

import com.efihumboldt.appligas.entidades.Noticia
import com.efihumboldt.appligas.interfaces.NoticiaDAO

class NoticiaService (private val noticiaDAO: NoticiaDAO) {
    suspend fun getAllNoticias(tournamentID: Int?): List<Noticia> {
        return noticiaDAO.getAllNoticias(tournamentID)
    }
}