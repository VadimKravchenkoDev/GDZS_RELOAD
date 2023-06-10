package com.zmei.gdzs

import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.zmei.gdzs.constant.Constant
import com.zmei.gdzs.databinding.ActivityMain4Binding
import java.util.*

class MainActivity4 : AppCompatActivity() {
    lateinit var binding : ActivityMain4Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain4Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textTimeActual.setOnClickListener {
            showTimePickerDialog { selectedTime ->
                binding.textTimeActual.text = selectedTime
                binding.textTimeActual.background = ColorDrawable(Color.TRANSPARENT)
            }
        }
        val protectTime = intent.getStringExtra("333")
        binding.textProtection.text = protectTime
    /*    binding.buttonFindFire.setOnClickListener {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(editText.windowToken, 0)
            //при виборі типу апаратів з расходом 7 атмосфер
            if (typeAparat == 1 || typeAparat == 3) {
                var minPressureNearFire: Int = binding.edMinPressure.text.toString().toIntOrNull() ?: 0
                if (minPressureNearFire == 0) Toast.makeText(this, "Введіть тиск ", Toast.LENGTH_SHORT).show()
                else {
                    var pressureGo = minPressure - minPressureNearFire //  час захисної дії апарату
                    var timeProtection = (minPressure / 7).toString() + "хв."
                    binding.textProtection.text = timeProtection
                    //рахуеємо тиск який витратили на шлях до осередку пожежі
                    binding.constraintPressure.visibility = View.VISIBLE
                    binding.constraintTime.visibility = View.VISIBLE
                    binding.textPressureGo.text = pressureGo.toString() + "атм."
                    if (action == 1) {//при виборі середнього навантаження роботи
                        var pressureExit: Int = pressureGo + Constant.reservDrager
                        var timeWork: Int = (((minPressure - (pressureGo * 2)) - Constant.reservDrager) / 7)
                        binding.textWork.text = timeWork.toString() + "хв." //  час роботи біля осередку пожежі
                        binding.textPressureExit.text = pressureExit.toString() + "атм." //тиск при котрому потрібно виходити
                        var minutesExit: Int = (pressureGo / 7) + timeWork
                        val exitTime = addMinutesToTime(timeAction.toString(), minutesExit)//час на годиннику коли потрібно виходити
                        binding.textExit.text = exitTime

                    } else if (action == 2) {//при виборі важкого навантаження роботи
                        var pressureExit: Int = (2 * pressureGo) + Constant.reservDrager
                        var timeWork: Int = (((minPressure - (pressureGo * 3)) - Constant.reservDrager) / 7)
                        binding.textWork.text = timeWork.toString() + "хв." //  час роботи біля осередку пожежі
                        binding.textPressureExit.text = pressureExit.toString() + "атм." //тиск при якому потрібно виходити
                        var minutesExit: Int = (pressureGo / 7) * 2 + timeWork
                        val exitTime = addMinutesToTime(timeAction.toString(), minutesExit)//час на годиннику коли потрібно виходити
                        binding.textExit.text = exitTime
                    }
                }
            } else if (typeAparat ==2) {//при виборі типу апаратів з расходом 5 атмосфер
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
                        var pressureExit: Int = pressureGo + Constant.reservASV
                        var timeWork: Int = (((minPressure - (pressureGo * 2)) - Constant.reservASV) / 5)
                        binding.textWork.text = timeWork.toString() + "хв." //  час роботи біля осередку пожежі
                        binding.textPressureExit.text = pressureExit.toString() + "атм." //тиск при якому потрібно виходити
                        var minutesExit: Int = (pressureGo / 5) + timeWork
                        val exitTime = addMinutesToTime(timeAction.toString(), minutesExit)//час на годиннику коли потрібно виходити
                        binding.textExit.text = exitTime
                    } else if (action == 2)
                    {
                        var pressureExit: Int = (2 * pressureGo) + Constant.reservASV
                        var timeWork: Int = (((minPressure - (pressureGo * 3)) - Constant.reservASV) / 5)
                        binding.textWork.text = timeWork.toString() + "хв." //  час роботи біля осередку пожежі
                        binding.textPressureExit.text = pressureExit.toString() + "атм." //тиск при котрому потрібно виходити
                        var minutesExit: Int = (pressureGo / 5) * 2 + timeWork
                        val exitTime = addMinutesToTime(timeAction.toString(), minutesExit)//час на годиннику коли потрібно виходити
                        binding.textExit.text = exitTime
                    }
                }
            }
        }*/

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