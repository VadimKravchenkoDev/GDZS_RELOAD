package com.zmei.gdzs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zmei.gdzs.databinding.ActivityMain3Binding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity3 : AppCompatActivity() {
    lateinit var binding: ActivityMain3Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain3Binding.inflate(layoutInflater)
        setContentView(binding.root)

       val action = intent.getIntExtra("action", 0)
       val minPressure = intent.getIntExtra("minPressure",0)
       val timeAction = intent.getStringExtra("timeAction")
       binding.textTimeEnter.text = timeAction
        var timeWork :Int = ((minPressure-60)/7)
        val newTime = addMinutesToTime(timeAction.toString(), timeWork)
        binding.textTimeExpected.text = newTime
            }

    private fun addMinutesToTime(time: String, minutes: Int): String {
        val calendar = Calendar.getInstance()
        val timeFormat = SimpleDateFormat("HH:mm")
        calendar.time = timeFormat.parse(time)
        calendar.add(Calendar.MINUTE, minutes)
        return timeFormat.format(calendar.time)
    }
}

