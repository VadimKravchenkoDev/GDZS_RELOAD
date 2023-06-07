package com.zmei.gdzs

import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.opengl.Visibility
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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.google.android.material.internal.ViewUtils.dpToPx
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.zmei.gdzs.constant.Constant
import com.zmei.gdzs.databinding.ActivityMain3Binding
import java.text.SimpleDateFormat
import java.util.*
class MainActivity3 : AppCompatActivity() {
    lateinit var binding: ActivityMain3Binding
    private var isActivityPaused = false
    private var timeWork : Int = 0
    override fun onPause() {
        super.onPause()
        isActivityPaused = true
    }
    override fun onResume() {
        super.onResume()
        isActivityPaused = false
    }
    override fun onCreate(savedInstanceState: Bundle?) {
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
        //час знаходження осередку пожежі
        binding.textTimeFire.setOnClickListener {
            showTimePickerDialog { selectedTime ->
                binding.textTimeFire.text = selectedTime
                //binding.textTimeFire.background = ColorDrawable(Color.TRANSPARENT)
            }
        }
        //поле для вводу мінімального тиску при знаходжені осередку пожежі
        val editText: EditText = binding.edMinPressure
        editText.setOnClickListener {
            editText.isFocusableInTouchMode = true
            editText.requestFocus()
        }
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) { val newText = s.toString() }
        })
        //при натисканні кнопки проводиться розрахунок тиску та часу
        binding.buttonCalcFire.setOnClickListener {
            val parentLayout = binding.buttonCalcFire.parent as ConstraintLayout
            parentLayout.post {
                val layoutParamsButton = binding.buttonCalcFire.layoutParams as ConstraintLayout.LayoutParams
                val layoutParamsConstraint = binding.constraintFindFire.layoutParams as ConstraintLayout.LayoutParams
                val layoutParamsBalon = binding.imageBalon.layoutParams as ConstraintLayout.LayoutParams
                val layoutParamsSpace = binding.spaceSave.layoutParams as ConstraintLayout.LayoutParams
                layoutParamsButton.startToStart = R.id.constraintFindFire
                layoutParamsConstraint.startToStart = R.id.spaceSave
                layoutParamsBalon.endToEnd = R.id.spaceSave
                layoutParamsConstraint.endToEnd = R.id.constrainMaster
                layoutParamsButton.endToEnd = R.id.constraintFindFire
                binding.buttonCalcFire.layoutParams = layoutParamsButton
                binding.constraintFindFire.layoutParams = layoutParamsConstraint
            }
            binding.imageBalon.visibility = View.VISIBLE
            binding.constraintFindFire.visibility = View.VISIBLE
            binding.buttonFire.visibility = View.VISIBLE
            binding.textViewReturn1.visibility = View.INVISIBLE
            binding.textViewReturn2.visibility = View.INVISIBLE
            binding.outlinedTextFieldReturn.visibility = View.INVISIBLE
        }
        binding.buttonFire.setOnClickListener {
            var textTimeFire = binding.textTimeFire.text.toString()
            var minPressureNearFire: Int = binding.edMinPressure.text.toString().toIntOrNull() ?: 0
            when {
            textTimeFire == "Обрати час" -> Toast.makeText(this, "Введіть час", Toast.LENGTH_SHORT).show()
            minPressureNearFire == 0 -> Toast.makeText(this, "Введіть тиск ", Toast.LENGTH_SHORT).show()
            minPressureNearFire > minPressure -> Toast.makeText(this, "Некоректний тиск ", Toast.LENGTH_SHORT).show()
            minPressureNearFire < (minPressure/2+Constant.reservDrager) -> Toast.makeText(this, "Некоректний тиск ", Toast.LENGTH_SHORT).show()
                else -> {
                    binding.constraintStartWork.visibility = View.VISIBLE
                    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(editText.windowToken, 0)
                        if (typeAparat == 1 || typeAparat == 3) {//при виборі типу апаратів з расходом 7 атмосфер
                            var pressureGo = minPressure - minPressureNearFire //  час захисної дії апарату
                            if (action == 1) {  //при виборі середнього навантаження роботи
                                var pressureExit: Int = pressureGo + Constant.reservDrager
                                binding.textPressureGo.text = pressureExit.toString() + "атм."//тиск використанний на прямування до осередку
                                timeWork = (((minPressure - (pressureGo * 2)) - Constant.reservDrager) / 7)
                                var minutesExit: Int = (pressureGo / 7) + timeWork
                                val exitTime = addMinutesToTime(timeAction.toString(), minutesExit)//час на годиннику коли потрібно виходити
                                binding.textExit.text = exitTime
                            }      else if (action == 2) {  //при виборі важкого навантаження роботи
                                        var pressureExit: Int = (2 * pressureGo) + Constant.reservDrager
                                        binding.textPressureGo.text = pressureExit.toString() + "атм."
                                        timeWork = (((minPressure - (pressureGo * 3)) - Constant.reservDrager) / 7)
                                        var minutesExit: Int = (pressureGo / 7) * 2 + timeWork
                                        val exitTime = addMinutesToTime(timeAction.toString(), minutesExit)//час на годиннику коли потрібно виходити
                                        binding.textExit.text = exitTime
                                    }
                        } else if (typeAparat == 2) {//при виборі типу апаратів з расходом 5 атмосфер
                                    var pressureGo = minPressure - minPressureNearFire
                                    if (action == 1) {
                                        var pressureExit: Int = pressureGo + Constant.reservASV
                                        binding.textPressureGo.text = pressureExit.toString() + "атм."
                                        timeWork = (((minPressure - (pressureGo * 2)) - Constant.reservASV) / 5)
                                        var minutesExit: Int = (pressureGo / 5) + timeWork
                                        val exitTime = addMinutesToTime(timeAction.toString(), minutesExit)
                                        binding.textExit.text = exitTime
                                    }       else if (action == 2) {
                                                var pressureExit: Int = (2 * pressureGo) + Constant.reservASV
                                                binding.textPressureGo.text = pressureExit.toString() + "атм."
                                                timeWork = (((minPressure - (pressureGo * 3)) - Constant.reservASV) / 5)
                                                var minutesExit: Int = (pressureGo / 5) * 2 + timeWork
                                                val exitTime = addMinutesToTime(timeAction.toString(), minutesExit)
                                                binding.textExit.text = exitTime
                                            }
                                }
                    val textTimerWork: TextView = binding.timerWork //таймер
                    val timerWork = object : CountDownTimer(timeWork.toLong() * 60 * 1000, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                        val minutes = millisUntilFinished / (60 * 1000)
                        val seconds = (millisUntilFinished % (60 * 1000)) / 1000
                        val timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
                        textTimerWork.text = timeLeftFormatted
                        }
                        override fun onFinish() {
                            var mediaPlayer = MediaPlayer.create(applicationContext, R.raw.gudok) // звуковий сигнал що оповіщуе про закінчення часу
                            mediaPlayer.start()
                            mediaPlayer.setOnCompletionListener {
                                mediaPlayer?.stop()
                                textTimerWork.text = "00.00"
                                cancel()
                            }
                        }
                    }
                    timerWork.start()
                    binding.buttonFire.isEnabled = false
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
        val textTimerNotFind: TextView = binding.timerNotFind //таймер
        val timerWorkNotFind = object : CountDownTimer(timeProtect.toLong()/2 * 60 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / (60 * 1000)
                val seconds = (millisUntilFinished % (60 * 1000)) / 1000
                val timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
                textTimerNotFind.text = timeLeftFormatted
            }
            override fun onFinish() {
                var mediaPlayer = MediaPlayer.create(applicationContext, R.raw.gudok) // звуковий сигнал що оповіщуе про закінчення часу
                mediaPlayer.start()
                mediaPlayer.setOnCompletionListener {
                    mediaPlayer?.stop()
                    textTimerNotFind.text = "00.00"
                    cancel()
                }
            }
        }
        timerWorkNotFind.start()
        val timerTextView: TextView = binding.tvTimer //таймер
        val timer = object : CountDownTimer(timeProtect.toLong() * 60 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / (60 * 1000)
                val seconds = (millisUntilFinished % (60 * 1000)) / 1000
                val timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
                timerTextView.text = timeLeftFormatted
            }
            override fun onFinish() {
                var mediaPlayer = MediaPlayer.create(applicationContext, R.raw.gudok) // звуковий сигнал що оповіщуе про закінчення часу
                mediaPlayer.start()
                mediaPlayer.setOnCompletionListener {
                mediaPlayer?.stop()
                timerTextView.text = "00.00"
                cancel()
                }
            }
        }
        timer.start()
        val timerFireText: TextView = binding.tvTimerFire //таймер зв'зку
        val timerFire = object : CountDownTimer(10 * 60 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / (60 * 1000)
                val seconds = (millisUntilFinished % (60 * 1000)) / 1000
                val timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
                timerFireText.text = timeLeftFormatted
            }
            override fun onFinish() {
                var mediaPlayer = MediaPlayer.create(applicationContext, R.raw.perevirka) // звуковий сигнал що оповіщуе про час перевірки зв'язку
                mediaPlayer.start()
                mediaPlayer.setOnCompletionListener {
                mediaPlayer?.stop()
                  if (isActivityPaused) cancel()
                  else start()
                }
            }
        }
        if (isActivityPaused==false) timerFire.start()
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
    private fun hideKeyboard() {//фунція для приховування клавіатури при натисканні на єкран
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusView = currentFocus
        if (currentFocusView != null)
        {
            inputMethodManager.hideSoftInputFromWindow(currentFocusView.windowToken, 0)
        }
    }
    override fun onBackPressed() {
        val dialogFragment = MyDialogFragment()
        dialogFragment.show(supportFragmentManager, "MyDialogFragment")
    }
    class MyDialogFragment : DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val builder = AlertDialog.Builder(requireActivity())
            builder.setTitle(R.string.warning)
            builder.setMessage(R.string.warningMassage)
            builder.setPositiveButton("OK") { dialog, which ->

                requireActivity().finish()
            }
            builder.setNegativeButton("Cancel") { dialog, which ->
            }
            return builder.create()
        }
    }
}

