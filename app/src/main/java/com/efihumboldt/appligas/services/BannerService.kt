package com.efihumboldt.appligas.services

import com.efihumboldt.appligas.entidades.Banner
import com.efihumboldt.appligas.interfaces.BannerDAO

class BannerService(private val bannerDAO : BannerDAO) {

    suspend fun getBanners1ByDivisionID(divisionID : Int?): List<Banner> {
        return bannerDAO.getBanners1ByDivisionID(divisionID)
    }
    suspend fun getBanners2ByDivisionID(divisionID : Int?): List<Banner> {
        return bannerDAO.getBanners2ByDivisionID(divisionID)
    }
    suspend fun getBanners3ByDivisionID(divisionID : Int?): List<Banner> {
        return bannerDAO.getBanners3ByDivisionID(divisionID)
    }
    suspend fun getBanners4ByDivisionID(divisionID : Int?): List<Banner> {
        return bannerDAO.getBanners4ByDivisionID(divisionID)
    }
    suspend fun getBanners5ByDivisionID(divisionID : Int?, teamID : Int): List<Banner> {
        return bannerDAO.getBanners5ByDivisionID(divisionID, teamID)
    }
    suspend fun getBanners6ByDivisionID(divisionID : Int?, localID : Int, visitID : Int): List<Banner> { // POR SI ALGUNO DE LOS EQUIPOS EN ESPECIAL QUIERE PROMOCIONARLO
        return bannerDAO.getBanners6ByDivisionID(divisionID, localID, visitID)
    }
    suspend fun getBanners7ByDivisionID(divisionID : Int?): List<Banner> {
        return bannerDAO.getBanners7ByDivisionID(divisionID)
    }
    suspend fun getBanners8ByDivisionID(divisionID : Int?): List<Banner> {
        return bannerDAO.getBanners8ByDivisionID(divisionID)
    }
}