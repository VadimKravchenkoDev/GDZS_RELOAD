package com.kravchenko_vadim.gdzs.timers

import android.media.MediaPlayer
import android.os.CountDownTimer
import android.util.Log
import android.widget.TextView
import com.kravchenko_vadim.gdzs.R
import com.kravchenko_vadim.gdzs.databinding.ActivityTimerCalculatorWorkBinding
import java.util.Locale

class TimerBond(private val activityBinding: ActivityTimerCalculatorWorkBinding) {
    private var timerBond: CountDownTimer? = null
    fun initialize() {
        val timerBondText: TextView = activityBinding.tvTimerBond //таймер зв'зку
        timerBond = object : CountDownTimer(10 * 60 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / (60 * 1000)
                val seconds = (millisUntilFinished % (60 * 1000)) / 1000
                val timeLeftFormatted =
                    String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
                Log.w("mylog", timeLeftFormatted)
                timerBondText.text = timeLeftFormatted
            }

            override fun onFinish() {
                val mediaPlayer = MediaPlayer.create(
                    activityBinding.root.context,
                    R.raw.perevirka
                ) // звуковий сигнал що оповіщуе про час перевірки зв'язку
                mediaPlayer.start()
                mediaPlayer.setOnCompletionListener {
                    mediaPlayer?.stop()
                    start()
                }
            }
        }
        timerBond?.start()

    }

    fun stopTimer() {
        timerBond?.cancel()
    }
}
