package com.kravchenko_vadim.gdzs.timers

import android.media.MediaPlayer
import android.os.CountDownTimer
import android.widget.TextView
import com.kravchenko_vadim.gdzs.R
import java.util.Locale

class TimerWork(private val textView: TextView, private val timeWork: Int) {
        private var timerWork: CountDownTimer? = null

        fun startTimer() {
            timerWork = object : CountDownTimer(timeWork.toLong() * 60 * 1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val minutes = millisUntilFinished / (60 * 1000)
                    val seconds = (millisUntilFinished % (60 * 1000)) / 1000
                    val timeLeftFormatted =
                        String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
                    textView.text = timeLeftFormatted
                }

                override fun onFinish() {
                    val mediaPlayer = MediaPlayer.create(
                        textView.context,
                        R.raw.gudok
                    ) // звуковой сигнал, оповещающий о завершении времени
                    mediaPlayer.start()
                    mediaPlayer.setOnCompletionListener {
                        mediaPlayer?.stop()
                        textView.text = "00:00"
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