package com.kravchenko_vadim.gdzs

import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kravchenko_vadim.gdzs.databinding.ActivityLogBinding
import java.util.*

class LogActivity : AppCompatActivity() {
    lateinit var binding: ActivityLogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textTimeActual.setOnClickListener {
            showTimePickerDialog { selectedTime ->
                binding.textTimeActual.text = selectedTime
                binding.textTimeActual.background = ColorDrawable(Color.TRANSPARENT)
            }
        }
        val protectTime = intent.getStringExtra("333")
        binding.textProtection.text = protectTime
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