package com.example.mystopwatcher.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mystopwatcher.model.*
import com.example.mystopwatcher.util.TimestampMillisecondsFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.StateFlow

class MainViewModel() : ViewModel() {

    val liveData: MutableLiveData<Any> = MutableLiveData()

    private val timestampProvider = object : TimestampProvider {
        override fun getMilliseconds(): Long {
            return System.currentTimeMillis()
        }
    }
    private val stopwatchListOrchestrator = StopwatchListOrchestrator(
        StopwatchStateHolder(
            StopwatchStateCalculator(
                timestampProvider,
                ElapsedTimeCalculator(timestampProvider)
            ),
            ElapsedTimeCalculator(timestampProvider),
            TimestampMillisecondsFormatter()
        ),
        CoroutineScope(
            Dispatchers.Main
                    + SupervisorJob()
        )
    )

    fun setTime(): StateFlow<String> {
        return stopwatchListOrchestrator.ticker
    }

    fun loadData(i: Int) {
        when (i) {
            0 -> liveData.postValue(stopwatchListOrchestrator.start())
            1 -> liveData.postValue(stopwatchListOrchestrator.pause())
            2 -> liveData.postValue(stopwatchListOrchestrator.stop())
        }
    }
}

