package com.kkamaraj.n26.bitcoinstats.presenter

import androidx.annotation.VisibleForTesting
import com.kkamaraj.n26.models.ChartData
import com.kkamaraj.n26.models.Status
import com.kkamaraj.n26.service.ChartServiceClient
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class BitcoinStatsPresenter @Inject constructor(private val scheduler: Scheduler,
                                                private val chartServiceClient: ChartServiceClient) {

    private lateinit var uiEvents: UiEvents
    var screenData = BehaviorSubject.create<UiModel>()
    private var disposable: Disposable = CompositeDisposable()

    fun attach(uiEvents: UiEvents) {
        this.uiEvents = uiEvents

        val bitcoinChartDataObservable = uiEvents.getChartData().flatMap { loadScreenData() }

        disposable = bitcoinChartDataObservable
            .observeOn(scheduler)
            .onErrorReturn { UiModel(showProgress = false, errorMessage = it.message) }
            .subscribe { uiModel ->
                screenData.onNext(uiModel)
            }
    }

    private fun loadScreenData() : Observable<UiModel>  = chartServiceClient.getChart(
            chartName = "market-price",
            timeSpan = "1year",
            format = "json")
        .onErrorReturn { ChartData(status = Status.NOT_FOUND, errorMessage = it.message) }
        .observeOn(scheduler)
        .map<UiModel> {
            if (it.status == Status.OK) {
                it.toViewModel()
            } else {
                UiModel(showProgress = false, errorMessage = it.errorMessage)
            }
        }
        .startWith(UiModel(showProgress = true))

    fun detach() = disposable.dispose()

    interface UiEvents {
        fun getChartData() : Observable<Unit>
    }
}
