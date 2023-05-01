package com.zmei.gdzs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.zmei.gdzs.databinding.ActivityMain2Binding
import com.zmei.gdzs.databinding.ActivityMainBinding

class MainActivity2 : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    lateinit var binding : ActivityMain2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMain2Binding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val spinner: Spinner = binding.spinnerAction
        val spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.action, R.layout.spinner_prompt_item)
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item)
        spinner.adapter = spinnerAdapter
        spinner.onItemSelectedListener = this
    }
    fun onClick(view: View){
        val spinner: Spinner = binding.spinnerAction
        val selected = spinner.selectedItemPosition
        if (selected == 0){
            binding.textAction.visibility = View.VISIBLE
            binding.textAction.text = "Оберіть вид роботи!"}
        else {val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)}

    }
    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        val selectedItem = parent.getItemAtPosition(position).toString()
        Toast.makeText(this, "Ви обрали: $selectedItem", Toast.LENGTH_SHORT).show()
    }
    override fun onNothingSelected(parent: AdapterView<*>) {

    }
}

