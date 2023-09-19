package com.kravchenko_vadim.gdzs.timers

import android.media.MediaPlayer
import android.os.CountDownTimer
import android.util.Log
import android.widget.TextView
import com.kravchenko_vadim.gdzs.R
import com.kravchenko_vadim.gdzs.databinding.ActivityTimerCalculatorWorkBinding
import java.util.Locale

class TimerWork(val binding: ActivityTimerCalculatorWorkBinding, val timeWork: Int) {

private var timerWork: CountDownTimer? = null

    fun initialize() {
        val textTimerWork: TextView = binding.timerWork //таймер роботи
        timerWork = object : CountDownTimer(timeWork.toLong() * 60 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / (60 * 1000)
                val seconds = (millisUntilFinished % (60 * 1000)) / 1000
                val timeLeftFormatted =
                    String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
                Log.e("mylog", timeLeftFormatted)
                textTimerWork.text = timeLeftFormatted
            }

            override fun onFinish() {
                var mediaPlayer = MediaPlayer.create(
                    binding.root.context,
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
        timerWork?.start()
    }

    fun stopTimer() {
        timerWork?.cancel()
    }
}