package com.kravchenko_vadim.gdzs.timers

import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.LayerDrawable
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.util.Log
import android.widget.TextView
import com.kravchenko_vadim.gdzs.R
import com.kravchenko_vadim.gdzs.databinding.ActivityTimerCalculatorWorkBinding
import java.util.Locale

class TimerProtection(
    private val binding: ActivityTimerCalculatorWorkBinding,
    val timeProtect: Int
) {
    private var timerProtection: CountDownTimer? = null
    fun initialize() {
        val timerTextView: TextView = binding.tvTimer //таймер загальної захисної дії апарату
        timerProtection = object : CountDownTimer(timeProtect.toLong() * 60 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / (60 * 1000)
                val seconds = (millisUntilFinished % (60 * 1000)) / 1000
                val timeLeftFormatted =
                    String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
                Log.d("mylog", timeLeftFormatted)
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
                    binding.root.context,
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
        timerProtection?.start()
    }

    fun stopTimer() {
        timerProtection?.cancel()
    }
}