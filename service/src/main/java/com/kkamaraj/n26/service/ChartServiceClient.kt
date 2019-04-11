package com.kkamaraj.n26.service

import com.kkamaraj.n26.models.ChartData
import io.reactivex.Observable


interface ChartServiceClient {
    fun getChart(chartName: String, timeSpan: String, format: String): Observable<ChartData>
}

class ChartServiceClientImpl: ChartServiceClient {
    override fun getChart(chartName: String, timeSpan: String, format: String): Observable<ChartData> {
        return ServiceAdapter.getRetrofit().create(ChartService::class.java).getChart(chartName, timeSpan, format)
    }
}
