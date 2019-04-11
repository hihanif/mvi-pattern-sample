package com.kkamaraj.n26.service

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.kkamaraj.n26.models.Status
import com.kkamaraj.n26.models.StatusDeserializer
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceAdapter {

    private const val BASE_URL = "https://api.blockchain.info/"

    private val httpClient = OkHttpClient.Builder()
    private val logging = HttpLoggingInterceptor()
    val gson = GsonBuilder()
        .registerTypeAdapter(Status::class.java, StatusDeserializer())
        .create()

    fun getRetrofit() : Retrofit {

        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(logging)
        }

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
    }

}
