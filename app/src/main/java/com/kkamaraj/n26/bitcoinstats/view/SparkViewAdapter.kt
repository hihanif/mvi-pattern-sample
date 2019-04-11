package com.kkamaraj.n26.bitcoinstats.view

import com.robinhood.spark.SparkAdapter

class SparkViewAdapter (private var yData: List<Float>) : SparkAdapter() {

    fun updateData(data: List<Float>) {
        yData = data
        notifyDataSetChanged()
    }

    override fun getY(index: Int): Float = yData[index]

    override fun getItem(index: Int): Any = yData[index]

    override fun getCount(): Int = yData.size

}
