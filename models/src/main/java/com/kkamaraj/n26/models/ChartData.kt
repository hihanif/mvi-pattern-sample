package com.kkamaraj.n26.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class ChartData (@SerializedName("status") val status: Status,
                      @SerializedName("error") val errorMessage: String? = null,
                      @SerializedName("name") val chartName: String? = null,
                      @SerializedName("unit") val chartUnit: String? = null,
                      @SerializedName("period") val chartPeriod: String? = null,
                      @SerializedName("description") val chartDesc: String? = null,
                      @SerializedName("values") val chartValues: List<ChartValue>? = null)


data class ChartValue (@SerializedName("x") val xValue: Long,
                       @SerializedName("y") val yValue: Float)

enum class Status {
    OK,
    NOT_FOUND;

    companion object {
        @JvmStatic
        fun deserialize(status: String?): Status {
            return when (status?.toUpperCase(Locale.US)) {
                "OK" -> OK
                "NOT_FOUND" -> NOT_FOUND
                else -> throw IllegalArgumentException("Unknown Status [$status]")
            }
        }
    }
}
