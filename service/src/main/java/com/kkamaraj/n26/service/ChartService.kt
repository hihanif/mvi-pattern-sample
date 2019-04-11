package com.kkamaraj.n26.service

import com.kkamaraj.n26.models.ChartData
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ChartService {
    @GET("charts/{chartName}")
    fun getChart(@Path("chartName") chartName: String,
                 @Query("timespan") timeSpan: String,
                 @Query("format") format: String) : Observable<ChartData>
}
