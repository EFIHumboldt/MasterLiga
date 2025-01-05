package com.efihumboldt.appligas.implementaciones

import com.efihumboldt.appligas.interfaces.ApiService
import com.efihumboldt.appligas.interfaces.LigaApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Context
import com.efihumboldt.appligas.interfaces.BannerApiService
import com.efihumboldt.appligas.interfaces.CruceApiService
import com.efihumboldt.appligas.interfaces.EquipoSimpleApiService
import com.efihumboldt.appligas.interfaces.FechaDeportivaApiService
import com.efihumboldt.appligas.interfaces.NoticiaApiService
import com.efihumboldt.appligas.interfaces.PartidoApiService
import com.efihumboldt.appligas.interfaces.PosicionApiService
import com.efihumboldt.appligas.interfaces.TorneoApiService

class ApiServiceImpl(context: Context, bd: String?) : ApiService {


    private var path : String = if (!bd.isNullOrEmpty()) "$bd/" else ""

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(path) // Reemplaza esto con la URL base de tu API
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override val ligaApiService: LigaApiService by lazy {
        createService(LigaApiService::class.java)
    }

    override val torneoApiService: TorneoApiService by lazy {
        createService(TorneoApiService::class.java)
    }

    override val posicionApiService: PosicionApiService by lazy {
        createService(PosicionApiService::class.java)
    }

    override val equipoSimpleApiService: EquipoSimpleApiService by lazy {
        createService(EquipoSimpleApiService::class.java)
    }

    override val partidoApiService: PartidoApiService by lazy {
        createService(PartidoApiService::class.java)
    }

    override val noticiaApiService: NoticiaApiService by lazy {
        createService(NoticiaApiService::class.java)
    }

    override val fechaDeportivaApiService : FechaDeportivaApiService by lazy {
        createService(FechaDeportivaApiService::class.java)
    }

    override val cruceApiService : CruceApiService by lazy {
        createService(CruceApiService::class.java)
    }

    override val bannerApiService : BannerApiService by lazy {
        createService(BannerApiService::class.java)
    }



    private fun <T> createService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }
}