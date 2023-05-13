package com.zmei.gdzs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zmei.gdzs.databinding.ActivityMain3Binding

class MainActivity3 : AppCompatActivity() {
    lateinit var binding: ActivityMain3Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain3Binding.inflate(layoutInflater)
        setContentView(binding.root)
/* задачи:  0. Чтобы при начале ввода исчезал хинт в виде нуля
            1. сделать так чтобы нельзя было вводить давление больше 400 и меньше 60
            2. сделать так чтобы при вводе не сьезжала картинка и сообщение об ошибке

 */
       val action = intent.getIntExtra("action", 0)
        binding.textView3.text = action.toString()
        val myAdapter = intent.getIntArrayExtra("myAdapter")
        binding.textView4.text = myAdapter.press.toString()
            }
}

