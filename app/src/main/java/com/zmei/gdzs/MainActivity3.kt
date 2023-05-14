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

       val action = intent.getIntExtra("action", 0)
        binding.textView3.text = action.toString()
        val myAdapter = intent.getIntExtra("myAdapter",0)
        binding.textView4.text = myAdapter.toString()
            }
}

