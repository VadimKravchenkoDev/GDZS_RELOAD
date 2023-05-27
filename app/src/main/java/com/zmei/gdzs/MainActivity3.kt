package com.zmei.gdzs

import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.zmei.gdzs.databinding.ActivityMain3Binding
import java.text.SimpleDateFormat
import java.util.*


class MainActivity3 : AppCompatActivity() {
    lateinit var binding: ActivityMain3Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain3Binding.inflate(layoutInflater)
        setContentView(binding.root)
    //отримуємо введені данні
        val action = intent.getIntExtra("action", 0)
        val minPressure = intent.getIntExtra("minPressure",0)
        val timeAction = intent.getStringExtra("timeAction")
        val typeAparat = intent.getIntExtra("aparat",0)
        binding.textTimeEnter.text = timeAction
        var timeWork :Int = ((minPressure-60)/7)
        val newTime = addMinutesToTime(timeAction.toString(), timeWork)
    //час виходу ланки
        binding.textTimeExpected.text = newTime
        binding.textTimeActual.setOnClickListener {
            showTimePickerDialog { selectedTime ->
                binding.textTimeActual.text = selectedTime
                binding.textTimeActual.background = ColorDrawable(Color.TRANSPARENT)
            }
        }
    //час знаходження осередку пожежі
        binding.textTimeFire.setOnClickListener {
            showTimePickerDialog { selectedTime ->
                binding.textTimeFire.text = selectedTime
                binding.textTimeFire.background = ColorDrawable(Color.TRANSPARENT)
            }
        }
    //поле для вводу мінімального тиску при знаходжені осередку пожежі
        val editText: EditText = binding.edMinPressure
        editText.setOnClickListener {
            editText.isFocusableInTouchMode = true
            editText.requestFocus()
        }
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                            }
            override fun afterTextChanged(s: Editable?) {
                                val newText = s.toString()
                            }
        })
//при натисканні кнопки проводиться розрахунок тиску та часу
        binding.buttonCalcExit.setOnClickListener {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(editText.windowToken, 0)
        //при виборі типу апаратів з расходом 7 атмосфер
            if (typeAparat == 1 || typeAparat == 3) {
                var minPressureNearFire: Int = binding.edMinPressure.text.toString().toIntOrNull() ?: 0
                if (minPressureNearFire == 0) Toast.makeText(this, "Введіть тиск ", Toast.LENGTH_SHORT).show()
                else {
                    var pressureGo = minPressure - minPressureNearFire
                    binding.textProtection.text =
                        (minPressure / 7).toString() + "хв." //  час захисної дії апарату
                //рахуеємо тиск який витратили на шлях до осередку пожежі
                    binding.constraintPressure.visibility = View.VISIBLE
                    binding.constraintTime.visibility = View.VISIBLE
                    binding.textPressureGo.text = pressureGo.toString() + "атм."
                //при виборі середнього навантаження роботи
                    if (action == 1) {
                        var pressureExit: Int = pressureGo + 60
                        var timeWork: Int = (((minPressure - (pressureGo * 2)) - 60) / 7)
                        binding.textWork.text = timeWork.toString() + "хв." //  час роботи біля осередку пожежі
                        binding.textPressureExit.text = pressureExit.toString() + "атм." //тиск при котрому потрібно виходити
                        var minutesExit: Int = (pressureGo / 7) + timeWork
                        val exitTime = addMinutesToTime(timeAction.toString(), minutesExit)//час на годиннику коли потрібно виходити
                        binding.textExit.text = exitTime
                //при виборі важкого навантаження роботи
                    } else if (action == 2) {
                        var pressureExit: Int = (2 * pressureGo) + 60
                        var timeWork: Int = (((minPressure - (pressureGo * 3)) - 60) / 7)
                        binding.textWork.text = timeWork.toString() + "хв." //  час роботи біля осередку пожежі
                        binding.textPressureExit.text = pressureExit.toString() + "атм." //тиск при якому потрібно виходити
                        var minutesExit: Int = (pressureGo / 7) * 2 + timeWork
                        val exitTime = addMinutesToTime(timeAction.toString(), minutesExit)//час на годиннику коли потрібно виходити
                        binding.textExit.text = exitTime
                    }
                }
        //при виборі типу апаратів з расходом 5 атмосфер
            } else if (typeAparat ==2) {
                var minPressureNearFire: Int = binding.edMinPressure.text.toString().toIntOrNull() ?: 0
                if (minPressureNearFire == 0) Toast.makeText(this, "Введіть тиск ", Toast.LENGTH_SHORT).show()
                else {
                    var pressureGo = minPressure - minPressureNearFire
                    binding.textProtection.text = (minPressure / 5).toString() + "хв." //  час захисної дії апарату
                //рахуеємо тиск який витратили на шлях до осередку пожежі
                    binding.constraintPressure.visibility = View.VISIBLE
                    binding.constraintTime.visibility = View.VISIBLE
                    binding.textPressureGo.text = pressureGo.toString() + "атм." //тиск використанний на прямування до осередку
                    if (action == 1) {
                        var pressureExit: Int = pressureGo + 60
                        var timeWork: Int = (((minPressure - (pressureGo * 2)) - 60) / 5)
                        binding.textWork.text = timeWork.toString() + "хв." //  час роботи біля осередку пожежі
                        binding.textPressureExit.text = pressureExit.toString() + "атм." //тиск при якому потрібно виходити
                        var minutesExit: Int = (pressureGo / 5) + timeWork
                        val exitTime = addMinutesToTime(timeAction.toString(), minutesExit)//час на годиннику коли потрібно виходити
                        binding.textExit.text = exitTime
                    } else if (action == 2) {
                        var pressureExit: Int = (2 * pressureGo) + 60
                        var timeWork: Int = (((minPressure - (pressureGo * 3)) - 60) / 5)
                        binding.textWork.text = timeWork.toString() + "хв." //  час роботи біля осередку пожежі
                        binding.textPressureExit.text = pressureExit.toString() + "атм." //тиск при котрому потрібно виходити
                        var minutesExit: Int = (pressureGo / 5) * 2 + timeWork
                        val exitTime = addMinutesToTime(timeAction.toString(), minutesExit)//час на годиннику коли потрібно виходити
                        binding.textExit.text = exitTime
                    }
                }
            }
            }
    }
    private fun addMinutesToTime(time: String, minutes: Int): String {
        //Функція для додавання хвилин до поточного часу
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

