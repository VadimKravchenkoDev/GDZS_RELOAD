package com.zmei.gdzs

import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zmei.gdzs.databinding.ActivityMain3Binding
import java.text.SimpleDateFormat
import java.util.*
/*1. добавать поле с расчетом давления
    2. добавить поле с расчетом времени
    3. добавить поле где нужно выбрать минимальное давление при подходе к очагу
    4. добавить поле где нужно выбрать время когда звено подошло к очагу
 
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
        binding.textTimeActual.setOnClickListener {
            showTimePickerDialog()
            binding.textTimeActual.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
            }

    private fun addMinutesToTime(time: String, minutes: Int): String {
        //функция для добавления минут к реальному времени
        val calendar = Calendar.getInstance()
        val timeFormat = SimpleDateFormat("HH:mm")
        calendar.time = timeFormat.parse(time)
        calendar.add(Calendar.MINUTE, minutes)
        return timeFormat.format(calendar.time)
    }

    private fun showTimePickerDialog() {
        // Создаем диалог выбора времени
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(this, { _, hour, minute ->
            // Вызывается, когда пользователь выбирает время
            var selectedTime = String.format("%02d:%02d", hour, minute)
            binding.textTimeActual.text = selectedTime
        }, hourOfDay, minute, true)
        // Отображаем диалог выбора времени
        timePickerDialog.show()
    }

}

