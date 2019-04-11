package com.kkamaraj.n26.bitcoinstats.presenter

import com.kkamaraj.n26.models.ChartData

data class UiModel(val showProgress: Boolean = false,
                   val errorMessage: String? = null,
                   val chartName: String? = null,
                   val chartDesc: String? = null,
                   val xData: List<Long>? = null,
                   val yData: List<Float>? = null
)

fun ChartData.toViewModel() : UiModel = UiModel(showProgress = false,
    chartName = this.chartName,
    chartDesc = this.chartDesc,
    xData = this.chartValues?.flatMap { value -> mutableListOf(value.xValue) },
    yData = this.chartValues?.flatMap { value -> mutableListOf(value.yValue) })



