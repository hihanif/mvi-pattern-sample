package com.kkamaraj.n26.bitcoinstats.dagger

import com.kkamaraj.n26.bitcoinstats.view.BitcoinTrackingActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): BitcoinTrackingActivity
}
