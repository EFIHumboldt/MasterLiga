package com.efihumboldt.appligas.interfaces

import com.efihumboldt.appligas.entidades.Banner
import retrofit2.http.GET
import retrofit2.http.Query

interface BannerApiService {

    @GET("apis/banners/get_banner_1.php")
    suspend fun getBanners1ByDivisionID(@Query("bd") bd: String?, @Query("divisionID") divisionID: Int?): List<Banner>
    @GET("apis/banners/get_banner_2.php")
    suspend fun getBanners2ByDivisionID(@Query("bd") bd: String?, @Query("divisionID") divisionID: Int?): List<Banner>
    @GET("apis/banners/get_banner_3.php")
    suspend fun getBanners3ByDivisionID(@Query("bd") bd: String?, @Query("divisionID") divisionID: Int?): List<Banner>
    @GET("apis/banners/get_banner_4.php")
    suspend fun getBanners4ByDivisionID(@Query("bd") bd: String?, @Query("divisionID") divisionID: Int?): List<Banner>
    @GET("apis/banners/get_banner_5.php")
    suspend fun getBanners5ByDivisionID(@Query("bd") bd: String?, @Query("divisionID") divisionID: Int?,@Query("teamID")  teamID : Int): List<Banner>
    @GET("apis/banners/get_banner_6.php")
    suspend fun getBanners6ByDivisionID(@Query("bd") bd: String?, @Query("divisionID") divisionID: Int?, @Query("localID") localID : Int, @Query("visitID")  visitID : Int): List<Banner>
    @GET("apis/banners/get_banner_7.php")
    suspend fun getBanners7ByDivisionID(@Query("bd") bd: String?, @Query("divisionID") divisionID: Int?): List<Banner>
    @GET("apis/banners/get_banner_8.php")
    suspend fun getBanners8ByDivisionID(@Query("bd") bd: String?, @Query("divisionID") divisionID: Int?, @Query("teamID")  teamID : Int): List<Banner>
}