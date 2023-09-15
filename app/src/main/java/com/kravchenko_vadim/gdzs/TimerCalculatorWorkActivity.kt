package com.kravchenko_vadim.gdzs

import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.LayerDrawable
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.kravchenko_vadim.gdzs.constant.Constant
import com.kravchenko_vadim.gdzs.databinding.ActivityTimerCalculatorWorkBinding
import com.kravchenko_vadim.gdzs.timers.TimerWork
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class TimerCalculatorWorkActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    lateinit var binding: ActivityTimerCalculatorWorkBinding
    private var isActivityPaused = false
    private var timeWork: Int = 0
    private var minPressureNearFire: Int = 0
    private lateinit var timer: CountDownTimer
    private lateinit var timerWorkNotFind: CountDownTimer
    private lateinit var timerFire: CountDownTimer
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
        binding = ActivityTimerCalculatorWorkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val textInputEditText = binding.watch //годинник
        launch {
            while (true) {
                val currentTime = SimpleDateFormat("HH:mm:ss").format(Date())
                textInputEditText.setText(currentTime)
                delay(1000) // 1 секунда оновлення часу
            }
        }
        val action = intent.getIntExtra("action", 0)
        val minPressure = intent.getIntExtra("minPressure", 0)
        val timeAction = intent.getStringExtra("timeAction")
        val typeAparat = intent.getIntExtra("aparat", 0)
        binding.textTimeEnter.text = timeAction
        var timeProtect: Int =
            0 //очікуваний час на годиннику коли ланка повинна вийти із зони непридатної до дихання
        if (typeAparat == 1 || typeAparat == 3) timeProtect =
            ((minPressure - Constant.reservDrager) / 7)
        else if (typeAparat == 2) timeProtect = ((minPressure - Constant.reservASV) / 5)
        val newTime = addMinutesToTime(timeAction.toString(), timeProtect)
        //час виходу ланки
        binding.textTimeExpected.text = newTime
        //час знаходження осередку пожежі
        binding.textTimeFire.setOnClickListener {
            showTimePickerDialog { selectedTime ->
                binding.textTimeFire.text = selectedTime
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
            override fun afterTextChanged(s: Editable?) {
                val newText = s.toString()
            }
        })
        //при натисканні кнопки проводиться розрахунок тиску та часу
        binding.buttonCalcFire.setOnClickListener {
            val parentLayout = binding.buttonCalcFire.parent as ConstraintLayout
            parentLayout.post {
                val layoutParamsButton =
                    binding.buttonCalcFire.layoutParams as ConstraintLayout.LayoutParams
                val layoutParamsConstraint =
                    binding.constraintFindFire.layoutParams as ConstraintLayout.LayoutParams
                val layoutParamsBalon =
                    binding.imageBalon.layoutParams as ConstraintLayout.LayoutParams
                val layoutParamsSpace =
                    binding.spaceSave.layoutParams as ConstraintLayout.LayoutParams
                layoutParamsButton.startToStart = R.id.constraintFindFire
                layoutParamsConstraint.startToStart = R.id.spaceSave
                layoutParamsBalon.endToEnd = R.id.spaceSave
                layoutParamsConstraint.endToEnd = R.id.constrainMaster
                layoutParamsButton.endToEnd = R.id.constraintFindFire
                binding.buttonCalcFire.layoutParams = layoutParamsButton
                binding.constraintFindFire.layoutParams = layoutParamsConstraint
            }
            binding.imageBalon.visibility = View.VISIBLE
            binding.progressBarBalon.visibility = View.VISIBLE
            binding.constraintFindFire.visibility = View.VISIBLE
            binding.buttonFire.visibility = View.VISIBLE
            binding.textViewReturn1.visibility = View.INVISIBLE
            binding.textViewReturn2.visibility = View.INVISIBLE
            binding.outlinedTextFieldReturn.visibility = View.INVISIBLE
            binding.buttonCalcFire.isEnabled = false
        }
        binding.buttonFire.setOnClickListener {
            var textTimeFire = binding.textTimeFire.text.toString()
            minPressureNearFire = binding.edMinPressure.text.toString().toIntOrNull() ?: 0
            when {
                textTimeFire == "Обрати час" -> Toast.makeText(
                    this,
                    "Введіть час",
                    Toast.LENGTH_SHORT
                ).show()

                minPressureNearFire == 0 -> Toast.makeText(
                    this,
                    "Введіть тиск ",
                    Toast.LENGTH_SHORT
                ).show()

                minPressureNearFire > minPressure -> Toast.makeText(
                    this,
                    "Некоректний тиск ",
                    Toast.LENGTH_SHORT
                ).show()

                minPressureNearFire < (minPressure / 2 + Constant.reservDrager) -> Toast.makeText(
                    this,
                    "Некоректний тиск ",
                    Toast.LENGTH_SHORT
                ).show()

                else -> {
                    binding.buttonExit.visibility = View.VISIBLE
                    binding.constraintStartWork.visibility = View.VISIBLE
                    val inputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(editText.windowToken, 0)
                    if (typeAparat == 1 || typeAparat == 3) {//при виборі типу апаратів з расходом 7 атмосфер
                        var pressureGo =
                            (minPressure - minPressureNearFire) //  час захисної дії апарату
                        if (action == 1) {  //при виборі середнього навантаження роботи
                            var pressureExit: Int = pressureGo + Constant.reservDrager
                            binding.textPressureGo.text =
                                pressureExit.toString() + "атм."//тиск використанний на прямування до осередку
                            timeWork =
                                (((minPressure - (pressureGo * 2)) - Constant.reservDrager) / 7)
                            var minutesExit: Int = (pressureGo / 7) + timeWork
                            val exitTime = addMinutesToTime(
                                timeAction.toString(),
                                minutesExit
                            )//час на годиннику коли потрібно виходити
                            binding.textExit.text = exitTime
                        } else if (action == 2) {  //при виборі важкого навантаження роботи
                            var pressureExit: Int = (2 * pressureGo) + Constant.reservDrager
                            binding.textPressureGo.text = pressureExit.toString() + "атм."
                            timeWork =
                                (((minPressure - (pressureGo * 3)) - Constant.reservDrager) / 7)
                            var minutesExit: Int = (pressureGo / 7) * 2 + timeWork
                            val exitTime = addMinutesToTime(
                                timeAction.toString(),
                                minutesExit
                            )//час на годиннику коли потрібно виходити
                            binding.textExit.text = exitTime
                        }
                    } else if (typeAparat == 2) {//при виборі типу апаратів з расходом 5 атмосфер
                        var pressureGo = (minPressure - minPressureNearFire)
                        if (action == 1) {
                            var pressureExit: Int = pressureGo + Constant.reservASV
                            binding.textPressureGo.text = pressureExit.toString() + "атм."
                            timeWork = (((minPressure - (pressureGo * 2)) - Constant.reservASV) / 5)
                            var minutesExit: Int = (pressureGo / 5) + timeWork
                            val exitTime = addMinutesToTime(timeAction.toString(), minutesExit)
                            binding.textExit.text = exitTime
                        } else if (action == 2) {
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
                            val timeLeftFormatted =
                                String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
                            textTimerWork.text = timeLeftFormatted
                        }

                        override fun onFinish() {
                            var mediaPlayer = MediaPlayer.create(
                                applicationContext,
                                R.raw.gudok
                            ) // звуковий сигнал що оповіщуе про закінчення часу
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
        binding.buttonExit.setOnClickListener {//при натисканні прибираеться таймер та автоматично вводиться поточний час та тиск
            binding.outlinedTextField4.visibility = View.GONE
            val currentTimeString = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            binding.textExit.text = currentTimeString
            binding.textExit.setTextColor(Color.BLUE)
            binding.textView.setTextColor(Color.BLUE)
            binding.textView15.setTextColor(Color.BLUE)
            binding.textPressureGo.visibility = View.INVISIBLE
            binding.editPressureExit.visibility = View.VISIBLE
            //нижче через різницю поточного часу та часу початку роботу розраховуємо час та тиск використаний на гасінні пожежі
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val timeFire = timeFormat.parse(binding.textTimeFire.text.toString())
            val timeExit = timeFormat.parse(binding.textExit.text.toString())
            val differenceMillis = timeExit.time - timeFire.time
            val differenceMinutes = (differenceMillis / (1000 * 60)).toInt()
            val pressureSpendWork = differenceMinutes * 7
            val pressureOnExit = minPressureNearFire - pressureSpendWork
            binding.editPressureExit.hint = pressureOnExit.toString()
            binding.buttonSecurityLog.visibility = View.VISIBLE
        }
        val rootView = findViewById<View>(android.R.id.content)
        // Додаємо обробник подій натискання на кореневе представлення
        rootView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) { // Скрываем клавиатуру
                hideKeyboard()
            }
            false
        }
        val textTimerNotFind: TextView = binding.timerNotFind //таймер якщо не знайдено осередку
        val timerWorkNotFind = object : CountDownTimer(timeProtect.toLong() / 2 * 60 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / (60 * 1000)
                val seconds = (millisUntilFinished % (60 * 1000)) / 1000
                val timeLeftFormatted =
                    String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
                textTimerNotFind.text = timeLeftFormatted
            }

            override fun onFinish() {
                var mediaPlayer = MediaPlayer.create(
                    applicationContext,
                    R.raw.gudok
                ) // звуковий сигнал що оповіщуе про закінчення часу
                mediaPlayer.start()
                mediaPlayer.setOnCompletionListener {
                    mediaPlayer?.stop()
                    textTimerNotFind.text = "00.00"
                    cancel()
                }
            }
        }
        timerWorkNotFind.start()
        val timerTextView: TextView = binding.tvTimer //таймер загальної захисної дії апарату
        val timer = object : CountDownTimer(timeProtect.toLong() * 60 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / (60 * 1000)
                val seconds = (millisUntilFinished % (60 * 1000)) / 1000
                val timeLeftFormatted =
                    String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
                timerTextView.text = timeLeftFormatted
                //нижче код для встановлення рівня прогресс бара та зміна кольору в залежності від часу на таймері
                val progress = (millisUntilFinished / 1000).toInt() // Прогресс в секундах
                val maxProgress =
                    (timeProtect * 60).toInt() // Максимальне значение прогресса в секундах
                binding.progressBarBalon.progress = progress
                binding.progressBarBalon.max = maxProgress
                val progressDrawable = binding.progressBarBalon.progressDrawable as LayerDrawable
                val clipDrawable =
                    progressDrawable.findDrawableByLayerId(android.R.id.progress) as ClipDrawable
                val progressLayer = clipDrawable.drawable as LayerDrawable
                val progressOrange = progressLayer.findDrawableByLayerId(R.id.progressOrange)
                val progressRed = progressLayer.findDrawableByLayerId(R.id.progressRed)
                val progressBlue = progressLayer.findDrawableByLayerId(R.id.progressBlue)
                if (progress >= maxProgress * 2 / 3) {
                    progressOrange.alpha = 0
                    progressRed.alpha = 0
                    progressBlue.alpha = 255
                } else if (progress >= maxProgress / 3) {
                    progressOrange.alpha = 255
                    progressRed.alpha = 0
                    progressBlue.alpha = 0
                } else {
                    progressOrange.alpha = 0
                    progressBlue.alpha = 0
                    progressRed.alpha = 255
                }
            }

            override fun onFinish() {
                val mediaPlayer = MediaPlayer.create(
                    applicationContext,
                    R.raw.gudok
                ) // звуковий сигнал що оповіщуе про закінчення часу
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
                val timeLeftFormatted =
                    String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
                timerFireText.text = timeLeftFormatted
            }

            override fun onFinish() {
                val mediaPlayer = MediaPlayer.create(
                    applicationContext,
                    R.raw.perevirka
                ) // звуковий сигнал що оповіщуе про час перевірки зв'язку
                mediaPlayer.start()
                mediaPlayer.setOnCompletionListener {
                    mediaPlayer?.stop()
                    if (isActivityPaused) cancel()
                    else start()
                }
            }
        }
        if (!isActivityPaused) timerFire.start()
        binding.buttonSecurityLog.setOnClickListener {


            val intent = Intent(this, LogActivity::class.java)
            intent.putExtra("333", "333")
            ActivityAnimation.startActivityWithAnimation(this, intent)
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

    override fun onStop() {
        super.onStop()
        if (::timer.isInitialized) {
            timer.cancel()
        }
        if (::timerWorkNotFind.isInitialized) {
            timerWorkNotFind.cancel()
        }
        if (::timerFire.isInitialized) {
            timerFire.cancel()
        }
    }

    override fun onDestroy() {
        cancel()
        super.onDestroy()
    }
}

