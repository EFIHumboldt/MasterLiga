package com.efihumboldt.appligas.interfaces

interface ApiService {

    val ligaApiService: LigaApiService
    val torneoApiService: TorneoApiService
    val posicionApiService : PosicionApiService
    val equipoSimpleApiService : EquipoSimpleApiService
    val partidoApiService: PartidoApiService
    val noticiaApiService : NoticiaApiService
    val fechaDeportivaApiService: FechaDeportivaApiService
    val cruceApiService: CruceApiService
    val jugadorApiService : JugadorApiService
    val bannerApiService: BannerApiService

}