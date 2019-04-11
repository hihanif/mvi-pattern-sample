package com.kkamaraj.n26.bitcoinstats

import android.app.Activity
import android.app.Application
import com.kkamaraj.n26.bitcoinstats.dagger.DaggerBitcoinStatsComponent
import dagger.android.HasActivityInjector
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject



class BitcoinApplication: Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): DispatchingAndroidInjector<Activity>? {
        return dispatchingAndroidInjector
    }

    override fun onCreate() {
        super.onCreate()
        DaggerBitcoinStatsComponent.builder()
            .application(this)
            .build()
            .inject(this)
    }
}
