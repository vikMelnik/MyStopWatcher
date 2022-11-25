package com.example.mystopwatcher.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mystopwatcher.databinding.ActivityMainBinding
import com.example.mystopwatcher.viewmodel.MainViewModel
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.liveData.observe(this){
            renderData()
        }

        binding.buttonStart.setOnClickListener {
            viewModel.loadData(0)
        }
        binding.buttonPause.setOnClickListener {
            viewModel.loadData(1)
        }
        binding.buttonStop.setOnClickListener {
            viewModel.loadData(2)
        }
    }

    private fun renderData() {

        binding.textTime.text = viewModel.setTime().toString()

        CoroutineScope(
            Dispatchers.Main
                    + SupervisorJob()
        ).launch {
            viewModel.setTime()
                .collect {
                    binding.textTime.text = it
                }
        }
    }
}




