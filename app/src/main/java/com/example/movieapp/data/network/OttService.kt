package com.example.movieapp.data.network


import com.example.movieapp.BuildConfig
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient

import retrofit2.Call
import retrofit2.Retrofit

import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

const val BASE_URL = "https://ott-details.p.rapidapi.com/"


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

val interceptor = Interceptor{chain->
    val request = chain.request().newBuilder()
        .header("X-RapidAPI-Host"," ott-details.p.rapidapi.com")
        .header("X-RapidAPI-Key", "92b08c98c4mshea2736dbe93ee35p1e934djsn3f8ffa7fd7ac")
    chain.proceed(request.build())
}

val httpClient: OkHttpClient = OkHttpClient().newBuilder().addInterceptor(interceptor).build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .client(httpClient)
    .baseUrl(BASE_URL)
    .build()
interface OttApiService {

//    @Headers(
//        "ott-details.p.rapidapi.com",
//        "92b08c98c4mshea2736dbe93ee35p1e934djsn3f8ffa7fd7ac"
//    )
    @GET("gettitleDetails")
    fun getMovieOttDetails(@Query("imdbid") imdbid : String) :
            Deferred<OttInfo>
}



object OttApi {
    val retrofitService : OttApiService by lazy {
        retrofit.create(OttApiService::class.java)
    }
}