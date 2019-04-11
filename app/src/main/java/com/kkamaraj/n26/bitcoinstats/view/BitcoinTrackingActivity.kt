package com.kkamaraj.n26.bitcoinstats.view

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatTextView
import com.jakewharton.rxbinding3.view.clicks
import com.kkamaraj.n26.bitcoinstats.R
import com.kkamaraj.n26.bitcoinstats.presenter.BitcoinStatsPresenter
import com.kkamaraj.n26.bitcoinstats.presenter.UiModel
import com.robinhood.spark.SparkView
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class BitcoinTrackingActivity : AppCompatActivity(), BitcoinStatsPresenter.UiEvents {

    @Inject lateinit var presenter: BitcoinStatsPresenter

    private lateinit var getDataCTA: Button
    private lateinit var sparkView: SparkView
    private lateinit var chartTitle: AppCompatTextView
    private lateinit var chartDesc: AppCompatTextView
    private lateinit var scrubPriceView: AppCompatTextView

    private var disposable: Disposable = CompositeDisposable()

    private lateinit var sparkViewAdapter: SparkViewAdapter

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getDataCTA = findViewById(R.id.bitcoin_price_data_cta)
        scrubPriceView = findViewById(R.id.bitcoin_chart_scrub)

        sparkView = findViewById(R.id.spark_view)
        sparkViewAdapter = SparkViewAdapter(emptyList())
        sparkView.adapter = sparkViewAdapter
        sparkView.setScrubListener { value ->
            when (value) {
                null -> scrubPriceView.text = getString(R.string.scrub_empty)
                else -> scrubPriceView.text = getString(R.string.scrub_format, value)
            }
        }

        chartTitle = findViewById(R.id.bitcoin_chart_title)
        chartDesc = findViewById(R.id.bitcoin_chart_description)
        progressBar = findViewById(R.id.progressBar)
    }

    override fun onResume() {
        super.onResume()
        presenter.attach(this)
        disposable = subscribeForData()
    }

    override fun getChartData(): Observable<Unit> = getDataCTA.clicks().debounce(300, TimeUnit.MILLISECONDS)

    private fun subscribeForData()  = presenter.screenData
        .subscribe {
            processScreenData(it)
        }

    private fun processScreenData(viewModel: UiModel?) {
        viewModel?. let {
            with(viewModel) {
                when {
                    this.showProgress -> showLoading()
                    else -> cancelLoading()
                }
                this.chartName?. let { showChartTitle(it) }
                this.chartDesc?. let { showChartDescription(it) }
                this.yData?. let { loadChartData(it) }
                this.errorMessage?. let { showErrorDialog(it)}
            }
        }

    }

    private fun showErrorDialog(message: String?) {
        val errorMessage = message ?: getString(R.string.alert_message)
        val alertDialog = AlertDialog.Builder(this)
            .setTitle(R.string.alert_title)
            .setMessage(errorMessage)
            .setPositiveButton(R.string.ok_button) { _: DialogInterface, _: Int -> DialogInterface.OnClickListener { dialog, _ -> dialog.dismiss()} }
            .create()
        alertDialog.show()

    }

    private fun loadChartData(yData: List<Float>) {
        scrubPriceView.visibility = View.VISIBLE
        sparkViewAdapter.updateData(yData)
    }

    private fun showChartDescription(desc: String) {
        chartDesc.text = desc
    }

    private fun showChartTitle(title: String) {
        chartTitle.text = title
    }

    private fun cancelLoading() {
        progressBar.visibility = View.GONE
    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    override fun onPause() {
        super.onPause()
        presenter.detach()
        disposable.dispose()
    }

}
