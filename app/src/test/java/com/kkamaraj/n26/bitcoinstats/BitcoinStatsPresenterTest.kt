package com.kkamaraj.n26.bitcoinstats

import com.kkamaraj.n26.bitcoinstats.presenter.BitcoinStatsPresenter
import com.kkamaraj.n26.bitcoinstats.presenter.UiModel
import com.kkamaraj.n26.models.ChartData
import com.kkamaraj.n26.models.ChartValue
import com.kkamaraj.n26.models.Status
import com.kkamaraj.n26.service.ChartServiceClient
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class BitcoinStatsPresenterTest {

    private lateinit var bitcoinStatsPresenter: BitcoinStatsPresenter
    private lateinit var chartServiceClient: ChartServiceClient
    private lateinit var testObserver: TestObserver<UiModel>
    private lateinit var view: MockView
    private lateinit var viewModel: UiModel
    private lateinit var chartData: ChartData

    @Before
    fun setUp() {
        chartServiceClient = mock()
        bitcoinStatsPresenter = BitcoinStatsPresenter(Schedulers.trampoline(), chartServiceClient)
        testObserver = TestObserver()
        bitcoinStatsPresenter.screenData.subscribe(testObserver)
        view = MockView()

        chartData = ChartData(status = Status.OK,
            chartName = "market-price", chartDesc = "SomeDesc",
            chartValues = mutableListOf(ChartValue(1234556789, 1000f)))

        viewModel = UiModel(showProgress = false, chartDesc = "SomeDesc",
            chartName = "market-price", xData = mutableListOf(1234556789), yData = mutableListOf(1000f))
    }

    @Test
    fun `when tap on Bitcoin price CTA fetches market price data`() {
        bitcoinStatsPresenter.attach(view)

        view.buttonEvent.onNext(Unit)
        verify(chartServiceClient).getChart(any(), any(), any())
    }

    @Test
    fun `when tap on Bitcoin price CTA verify that data is returned`() {
        val response = Observable.just(chartData)
        bitcoinStatsPresenter.attach(view)
        whenever(chartServiceClient.getChart(any(), any(), any())).thenReturn(response)

        view.buttonEvent.onNext(Unit)
        verify(chartServiceClient).getChart(any(), any(), any())

        with(testObserver.values().first()) {
            assertThat(this).isEqualTo(UiModel(showProgress = true))

        }

        with(testObserver.values().last()) {
            assertThat(this).isEqualTo(viewModel)

        }

    }

    class MockView: BitcoinStatsPresenter.UiEvents {
        val buttonEvent = PublishSubject.create<Unit>()
        override fun getChartData(): Observable<Unit> = buttonEvent.hide()

    }
}
