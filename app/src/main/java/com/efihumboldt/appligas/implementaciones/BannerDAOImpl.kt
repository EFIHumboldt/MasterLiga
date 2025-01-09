package com.efihumboldt.appligas.implementaciones

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.efihumboldt.appligas.entidades.Banner
import com.efihumboldt.appligas.interfaces.BannerApiService
import com.efihumboldt.appligas.interfaces.BannerDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class BannerDAOImpl (private val apiService: BannerApiService,
                     private val bd: String?,
                     private val divisionID: Int?,
                     private val context : Context
): BannerDAO {

    override suspend fun getBanners1ByDivisionID(divisionID: Int?): List<Banner> {
        return try {
            withContext(Dispatchers.IO) {
                val response = apiService.getBanners1ByDivisionID(bd, divisionID)
                response.map { apiResponse ->
                    Banner(
                        link = apiResponse.link
                    )
                }
            }
        } catch (e: IOException) {
            Toast.makeText(context, "No se pudo conectar con el servidor", Toast.LENGTH_SHORT).show()
            emptyList()
        } catch (e: HttpException) {
            e.printStackTrace()
            Log.e("HTTP 500 Error 1", "Response Body: ${e.response()?.errorBody()?.string()}")
            emptyList()
        }
    }

    override suspend fun getBanners2ByDivisionID(divisionID: Int?): List<Banner> {
        return try {
            withContext(Dispatchers.IO) {
                val response = apiService.getBanners2ByDivisionID(bd, divisionID)
                response.map { apiResponse ->
                    Banner(
                        link = apiResponse.link
                    )
                }
            }
        } catch (e: IOException) {
            Toast.makeText(context, "No se pudo conectar con el servidor", Toast.LENGTH_SHORT).show()
            emptyList()
        } catch (e: HttpException) {
            e.printStackTrace()
            Log.e("HTTP 500 Error", "Response Body: ${e.response()?.errorBody()?.string()}")
            emptyList()
        }
    }

    override suspend fun getBanners3ByDivisionID(divisionID: Int?): List<Banner> {
        return try {
            withContext(Dispatchers.IO) {
                val response = apiService.getBanners3ByDivisionID(bd, divisionID)
                response.map { apiResponse ->
                    Banner(
                        link = apiResponse.link
                    )
                }
            }
        } catch (e: IOException) {
            Toast.makeText(context, "No se pudo conectar con el servidor", Toast.LENGTH_SHORT).show()
            emptyList()
        } catch (e: HttpException) {
            e.printStackTrace()
            Log.e("HTTP 500 Error", "Response Body: ${e.response()?.errorBody()?.string()}")
            emptyList()
        }
    }

    override suspend fun getBanners4ByDivisionID(divisionID: Int?): List<Banner> {
        return try {
            withContext(Dispatchers.IO) {
                val response = apiService.getBanners4ByDivisionID(bd, divisionID)
                response.map { apiResponse ->
                    Banner(
                        link = apiResponse.link
                    )
                }
            }
        } catch (e: IOException) {
            Toast.makeText(context, "No se pudo conectar con el servidor", Toast.LENGTH_SHORT).show()
            emptyList()
        } catch (e: HttpException) {
            e.printStackTrace()
            Log.e("HTTP 500 Error", "Response Body: ${e.response()?.errorBody()?.string()}")
            emptyList()
        }
    }

    override suspend fun getBanners5ByDivisionID(divisionID: Int?, teamID : Int): List<Banner> {
        return try {
            withContext(Dispatchers.IO) {
                val response = apiService.getBanners5ByDivisionID(bd, divisionID, teamID)
                response.map { apiResponse ->
                    Banner(
                        link = apiResponse.link
                    )
                }
            }
        } catch (e: IOException) {
            Toast.makeText(context, "No se pudo conectar con el servidor", Toast.LENGTH_SHORT).show()
            emptyList()
        } catch (e: HttpException) {
            e.printStackTrace()
            Log.e("HTTP 500 Error", "Response Body: ${e.response()?.errorBody()?.string()}")
            emptyList()
        }
    }

    override suspend fun getBanners6ByDivisionID(divisionID: Int?, localID : Int, visitID : Int): List<Banner> {
        return try {
            withContext(Dispatchers.IO) {
                val response = apiService.getBanners6ByDivisionID(bd, divisionID, localID, visitID)
                response.map { apiResponse ->
                    Banner(
                        link = apiResponse.link
                    )
                }
            }
        } catch (e: IOException) {
            Toast.makeText(context, "No se pudo conectar con el servidor", Toast.LENGTH_SHORT).show()
            emptyList()
        } catch (e: HttpException) {
            e.printStackTrace()
            Log.e("HTTP 500 Error", "Response Body: ${e.response()?.errorBody()?.string()}")
            emptyList()
        }
    }

    override suspend fun getBanners7ByDivisionID(divisionID: Int?): List<Banner> {
        return try {
            withContext(Dispatchers.IO) {
                val response = apiService.getBanners7ByDivisionID(bd, divisionID)
                response.map { apiResponse ->
                    Banner(
                        link = apiResponse.link
                    )
                }
            }
        } catch (e: IOException) {
            Toast.makeText(context, "No se pudo conectar con el servidor", Toast.LENGTH_SHORT).show()
            emptyList()
        } catch (e: HttpException) {
            e.printStackTrace()
            Log.e("HTTP 500 Error", "Response Body: ${e.response()?.errorBody()?.string()}")
            emptyList()
        }
    }

    override suspend fun getBanners8ByDivisionID(divisionID: Int?, teamID: Int): List<Banner> {
        return try {
            withContext(Dispatchers.IO) {
                val response = apiService.getBanners8ByDivisionID(bd, divisionID, teamID)
                response.map { apiResponse ->
                    Banner(
                        link = apiResponse.link
                    )
                }
            }
        } catch (e: IOException) {
            Toast.makeText(context, "No se pudo conectar con el servidor", Toast.LENGTH_SHORT).show()
            emptyList()
        } catch (e: HttpException) {
            e.printStackTrace()
            Log.e("HTTP 500 Error", "Response Body: ${e.response()?.errorBody()?.string()}")
            emptyList()
        }
    }
}