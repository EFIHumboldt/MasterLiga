package com.efihumboldt.appligas.interfaces

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

interface CruceApiService {

    @GET("apis/get_cruces_xml.php")
    fun getXmlData(): Call<ResponseBody>
}