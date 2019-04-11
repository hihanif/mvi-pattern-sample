package com.kkamaraj.n26.bitcoinstats.dagger

import android.app.Application
import com.kkamaraj.n26.bitcoinstats.BitcoinApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Component(
    modules = [
        BitcoinStatsModule::class,
        ActivityModule::class,
        AndroidSupportInjectionModule::class]
)

@Singleton
interface BitcoinStatsComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): BitcoinStatsComponent
    }

    fun inject(bitcoinApp: BitcoinApplication)
}
