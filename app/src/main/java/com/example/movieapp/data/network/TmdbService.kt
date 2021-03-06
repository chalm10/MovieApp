package com.example.movieapp.data.network

import com.example.movieapp.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface TmdbService {
    companion object{
        private const val BASE_URL = "https://api.themoviedb.org/3/"
        const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w185"
        const val BACKDROP_BASE_URL = "https://image.tmdb.org/t/p/w300"

        private val retrofitService by lazy {
            val interceptor = Interceptor{chain->
                val request = chain.request()
                val url = request.url().newBuilder()
                    .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY).build()
                val newRequest = request.newBuilder().url(url).build()
                chain.proceed(newRequest)
            }

            val httpClient = OkHttpClient().newBuilder().addInterceptor(interceptor).build()
            val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(TmdbService::class.java)

        }
        fun getInstance() : TmdbService {
            return retrofitService
        }
    }

    @GET("movie/popular" +
            "?page=1")
    suspend fun getMovies() : retrofit2.Response<TmdbMovieList>

}