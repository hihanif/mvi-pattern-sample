package com.kkamaraj.n26.bitcoinstats.dagger

import com.kkamaraj.n26.service.ChartServiceClient
import com.kkamaraj.n26.service.ChartServiceClientImpl
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers

@Module
class BitcoinStatsModule {

    @Provides fun provideScheduler() : Scheduler = AndroidSchedulers.mainThread()

    @Provides fun provideChartService(): ChartServiceClient = ChartServiceClientImpl()
}
