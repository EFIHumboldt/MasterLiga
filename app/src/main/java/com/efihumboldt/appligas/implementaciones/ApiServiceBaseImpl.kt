package com.efihumboldt.appligas.implementaciones

import com.efihumboldt.appligas.interfaces.LigaApiService
import com.efihumboldt.appligas.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Context
import com.efihumboldt.appligas.interfaces.ApiBaseService

class ApiServiceBaseImpl(context: Context, bd: String?) : ApiBaseService {

    //private var path : String = context.getString(R.string.URL_base) + if (!bd.isNullOrEmpty()) "/$bd/" else ""
    private var path : String = "http://192.168.56.1/"
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(path) // Reemplaza esto con la URL base de tu API
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override val ligaApiService: LigaApiService by lazy {
        createService(LigaApiService::class.java)
    }

    private fun <T> createService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }
}