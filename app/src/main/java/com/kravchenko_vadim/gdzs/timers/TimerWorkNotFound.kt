package com.kravchenko_vadim.gdzs.timers

import android.media.MediaPlayer
import android.os.CountDownTimer
import android.util.Log
import android.widget.TextView
import com.kravchenko_vadim.gdzs.R
import com.kravchenko_vadim.gdzs.databinding.ActivityTimerCalculatorWorkBinding
import java.util.Locale

class TimerWorkNotFound(val binding: ActivityTimerCalculatorWorkBinding, val timeProtect: Int) {
    private var timerWorkNotFound: CountDownTimer? = null
    fun initialize() {
        val textTimerNotFind: TextView = binding.timerNotFind //таймер якщо не знайдено осередку
        timerWorkNotFound = object : CountDownTimer(timeProtect.toLong() / 2 * 60 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / (60 * 1000)
                val seconds = (millisUntilFinished % (60 * 1000)) / 1000
                val timeLeftFormatted =
                    String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
                Log.i("mylog", timeLeftFormatted)
                textTimerNotFind.text = timeLeftFormatted
            }

            override fun onFinish() {
                var mediaPlayer = MediaPlayer.create(
                    binding.root.context,
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
        timerWorkNotFound?.start()
    }

    fun stopTimer() {
        timerWorkNotFound?.cancel()
    }

}