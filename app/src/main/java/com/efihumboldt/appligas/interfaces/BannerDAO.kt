package com.efihumboldt.appligas.interfaces

import com.efihumboldt.appligas.entidades.Banner

interface BannerDAO {

    suspend fun getBanners1ByDivisionID(divisionID : Int?): List<Banner>
    suspend fun getBanners2ByDivisionID(divisionID : Int?): List<Banner>
    suspend fun getBanners3ByDivisionID(divisionID : Int?): List<Banner>
    suspend fun getBanners4ByDivisionID(divisionID : Int?): List<Banner>
    suspend fun getBanners5ByDivisionID(divisionID : Int?, teamID: Int): List<Banner> //Por si alguno de los equipos quiere patrocinarlo en especial
    suspend fun getBanners6ByDivisionID(divisionID : Int?, localID: Int, visitID : Int): List<Banner> //Por si alguno de los equipos quiere patrocinarlo en especial
    suspend fun getBanners7ByDivisionID(divisionID : Int?): List<Banner>
    suspend fun getBanners8ByDivisionID(divisionID : Int?): List<Banner>

}