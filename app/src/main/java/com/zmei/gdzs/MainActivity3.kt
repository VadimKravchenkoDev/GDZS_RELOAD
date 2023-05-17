package com.zmei.gdzs

import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.zmei.gdzs.databinding.ActivityMain3Binding
import java.text.SimpleDateFormat
import java.util.*
/*1. добавать поле с расчетом давления
    2. добавить поле с расчетом времени
    3. добавить поле где нужно выбрать минимальное давление при подходе к очагу
    4. добавить поле где нужно выбрать время когда звено подошло к очагу*/

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
            showTimePickerDialog { selectedTime ->
                binding.textTimeActual.text = selectedTime
                binding.textTimeActual.background = ColorDrawable(Color.TRANSPARENT)
            }
        }
        binding.textTimeFire.setOnClickListener {
            showTimePickerDialog { selectedTime ->
                binding.textTimeFire.text = selectedTime
                binding.textTimeFire.background = ColorDrawable(Color.TRANSPARENT)
            }
        }

        val editText: EditText = binding.edMinPressure
        editText.setOnClickListener {
            editText.isFocusableInTouchMode = true // Разрешить редактирование текста
            editText.requestFocus() // Передать фокус на EditText
        }
// Добавьте слушатель изменений текста, если вам нужно отслеживать изменения
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Не используется
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Не используется
            }
            override fun afterTextChanged(s: Editable?) {
                // Выполните действия после изменения текста
                val newText = s.toString()
                // Действия с новым текстом
            }
        })



        binding.buttonCalcExit.setOnClickListener {
            var minPressureFire: Int = binding.edMinPressure.text.toString().toIntOrNull() ?: 0
            if(minPressureFire == 0 ) Toast.makeText(this, "Введіть тиск ", Toast.LENGTH_SHORT).show()
            else {
                //вычисляем давление которое потратили на проход к очагу
                binding.constraintPressure.visibility = View.VISIBLE
                var pressureGo = minPressure - minPressureFire
                binding.textPressureGo.text = pressureGo.toString()

                var pressureExit : Int = pressureGo + 60
                binding.textPressureExit.text = pressureExit.toString()
            }
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

    private fun showTimePickerDialog(callback: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, hour, minute ->
            val selectedTime = String.format("%02d:%02d", hour, minute)
            callback(selectedTime)
        }, hourOfDay, minute, true)

        timePickerDialog.show()
    }


}

