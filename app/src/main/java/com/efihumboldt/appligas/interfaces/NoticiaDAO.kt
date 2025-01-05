package com.efihumboldt.appligas.interfaces

import com.efihumboldt.appligas.entidades.Noticia

interface NoticiaDAO {
    suspend fun getAllNoticias(divisionID: Int?): List<Noticia>
}