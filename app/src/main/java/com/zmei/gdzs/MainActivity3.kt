package com.zmei.gdzs

import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextClock
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.zmei.gdzs.constant.Constant
import com.zmei.gdzs.databinding.ActivityMain3Binding
import java.text.SimpleDateFormat
import java.util.*
class MainActivity3 : AppCompatActivity()
{
    lateinit var binding: ActivityMain3Binding
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMain3Binding.inflate(layoutInflater)
        setContentView(binding.root)
        val textInputEditText = binding.watch //годинник
        val handler = Handler()
        val updateRunnable = object : Runnable {
            override fun run() {
                val currentTime = SimpleDateFormat("HH:mm:ss").format(Date())
                textInputEditText.setText(currentTime)
                handler.postDelayed(this, 1000) // оновлення тексту годинника кожну секунду
            }
        }
        handler.postDelayed(updateRunnable, 0)
        val action = intent.getIntExtra("action", 0)
        val minPressure = intent.getIntExtra("minPressure",0)
        val timeAction = intent.getStringExtra("timeAction")
        val typeAparat = intent.getIntExtra("aparat",0)
        binding.textTimeEnter.text = timeAction
        var timeProtect :Int =0 //очікуваний час на годиннику коли ланка повинна вийти із зони непридатної до дихання
        if (typeAparat==1||typeAparat==3) timeProtect = ((minPressure-Constant.reservDrager)/7)
        else if (typeAparat==2) timeProtect = ((minPressure-Constant.reservASV)/5)
        val newTime = addMinutesToTime(timeAction.toString(), timeProtect)
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
            if (typeAparat == 1 || typeAparat == 3)
            {
                var minPressureNearFire: Int = binding.edMinPressure.text.toString().toIntOrNull() ?: 0
                if (minPressureNearFire == 0) Toast.makeText(this, "Введіть тиск ", Toast.LENGTH_SHORT).show()
                else
                {
                    var pressureGo = minPressure - minPressureNearFire //  час захисної дії апарату
                    var timeProtection = (minPressure / 7).toString() + "хв."
                    binding.textProtection.text = timeProtection
                    //рахуеємо тиск який витратили на шлях до осередку пожежі
                    binding.constraintPressure.visibility = View.VISIBLE
                    binding.constraintTime.visibility = View.VISIBLE
                    binding.textPressureGo.text = pressureGo.toString() + "атм."
                    if (action == 1)
                        {//при виборі середнього навантаження роботи
                            var pressureExit: Int = pressureGo + Constant.reservDrager
                            var timeWork: Int = (((minPressure - (pressureGo * 2)) - Constant.reservDrager) / 7)
                            binding.textWork.text = timeWork.toString() + "хв." //  час роботи біля осередку пожежі
                            binding.textPressureExit.text = pressureExit.toString() + "атм." //тиск при котрому потрібно виходити
                            var minutesExit: Int = (pressureGo / 7) + timeWork
                            val exitTime = addMinutesToTime(timeAction.toString(), minutesExit)//час на годиннику коли потрібно виходити
                            binding.textExit.text = exitTime

                        } else if (action == 2)
                            {//при виборі важкого навантаження роботи
                                var pressureExit: Int = (2 * pressureGo) + Constant.reservDrager
                                var timeWork: Int = (((minPressure - (pressureGo * 3)) - Constant.reservDrager) / 7)
                                binding.textWork.text = timeWork.toString() + "хв." //  час роботи біля осередку пожежі
                                binding.textPressureExit.text = pressureExit.toString() + "атм." //тиск при якому потрібно виходити
                                var minutesExit: Int = (pressureGo / 7) * 2 + timeWork
                                val exitTime = addMinutesToTime(timeAction.toString(), minutesExit)//час на годиннику коли потрібно виходити
                                binding.textExit.text = exitTime
                            }
                }
            } else if (typeAparat ==2)
            {//при виборі типу апаратів з расходом 5 атмосфер
                var minPressureNearFire: Int = binding.edMinPressure.text.toString().toIntOrNull() ?: 0
                if (minPressureNearFire == 0) Toast.makeText(this, "Введіть тиск ", Toast.LENGTH_SHORT).show()
                else
                {
                    var pressureGo = minPressure - minPressureNearFire
                    binding.textProtection.text = (minPressure / 5).toString() + "хв." //  час захисної дії апарату
                    //рахуеємо тиск який витратили на шлях до осередку пожежі
                    binding.constraintPressure.visibility = View.VISIBLE
                    binding.constraintTime.visibility = View.VISIBLE
                    binding.textPressureGo.text = pressureGo.toString() + "атм." //тиск використанний на прямування до осередку
                        if (action == 1)
                        {
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
        }
        val rootView = findViewById<View>(android.R.id.content)
        // Додаємо обробник подій натискання на кореневе представлення
        rootView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                // Скрываем клавиатуру
                hideKeyboard()
            }
            false
        }
        val timerTextView: TextView = findViewById(R.id.tvTimer) //таймер
        val timer = object : CountDownTimer(timeProtect.toLong() * 60 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / (60 * 1000)
                val seconds = (millisUntilFinished % (60 * 1000)) / 1000
                val timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
                timerTextView.text = timeLeftFormatted
            }
            override fun onFinish() {
                timerTextView.text = "00.00"
            }
        }
        timer.start()
    }
    private fun addMinutesToTime(time: String, minutes: Int): String
    {
        //Функція для додавання хвилин до поточного часу
        val calendar = Calendar.getInstance()
        val timeFormat = SimpleDateFormat("HH:mm")
        calendar.time = timeFormat.parse(time)
        calendar.add(Calendar.MINUTE, minutes)
        return timeFormat.format(calendar.time)
    }
    private fun showTimePickerDialog(callback: (String) -> Unit)
    {
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, hour, minute ->
            val selectedTime = String.format("%02d:%02d", hour, minute)
            callback(selectedTime)
        }, hourOfDay, minute, true)
        timePickerDialog.show()
    }
    private fun hideKeyboard() //фунція для приховування клавіатури при натисканні на єкран
    {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusView = currentFocus
        if (currentFocusView != null)
        {
            inputMethodManager.hideSoftInputFromWindow(currentFocusView.windowToken, 0)
        }
    }
}

