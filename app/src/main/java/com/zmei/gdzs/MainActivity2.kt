package com.zmei.gdzs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zmei.gdzs.databinding.ActivityMain2Binding
import drawable.ItemOffsetDecoration

class MainActivity2 : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    lateinit var binding : ActivityMain2Binding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterClass


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMain2Binding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val spinner1: Spinner = binding.spinnerAction
        val spinnerAdapter1 = ArrayAdapter.createFromResource(this, R.array.action, R.layout.spinner_prompt_item)
        spinnerAdapter1.setDropDownViewResource(R.layout.spinner_item)
        spinner1.adapter = spinnerAdapter1
        spinner1.onItemSelectedListener = this
        /* додаемо спинер з вибором активности */
        val spinner2: Spinner = binding.spinnerAction2
        val spinnerAdapter2 = ArrayAdapter.createFromResource(this, R.array.apparatus, R.layout.spinner_prompt_item)
        spinnerAdapter2.setDropDownViewResource(R.layout.spinner_item)
        spinner2.adapter = spinnerAdapter2
        spinner2.onItemSelectedListener = this
        /* додаемо спинер з вибором типу апаратів */
        val spinner3: Spinner = binding.spinnerAction3
        val spinnerAdapter3 = ArrayAdapter.createFromResource(this, R.array.workload, R.layout.spinner_prompt_item)
        spinnerAdapter3.setDropDownViewResource(R.layout.spinner_item)
        spinner3.adapter = spinnerAdapter3
        spinner3.onItemSelectedListener = this
        /* додаемо спинер з вибором типу навантаження */


        adapter = AdapterClass()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // добавляем первый элемент
        adapter.addZveno()

        // при нажатии на кнопку добавляем новый элемент
        binding.btAdd.setOnClickListener {
            adapter.addZveno()
        }
        binding.recyclerView.addItemDecoration(ItemOffsetDecoration(16))
    }
    fun onClick(view: View) {
        val spinner1: Spinner = binding.spinnerAction
        val spinner2: Spinner = binding.spinnerAction2
        val spinner3: Spinner = binding.spinnerAction3
        val selected1 = spinner1.selectedItemPosition
        val selected2 = spinner2.selectedItemPosition
        val selected3 = spinner3.selectedItemPosition
        /*перевіряємо чи зроблен вибір на спінерах*/
        when {
            selected1 == 0 -> showErrorMessage("Оберіть вид роботи!")
            selected2 == 0 -> showErrorMessage("Оберіть вид апаратів")
            selected3 == 0 -> showErrorMessage("Оберіть вид навантаження")
            /* якщо на усіх спінерах зроблено вибір то при тисненні кнопки запускається екран розрахунків*/
            else -> {
                val intent = Intent(this, MainActivity3::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }
        }
    }

    private fun showErrorMessage(errorMessage: String) {
        binding.textAction.visibility = View.VISIBLE
        binding.textAction.text = errorMessage
    }


    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        val selectedItem = parent.getItemAtPosition(position).toString()
        Toast.makeText(this, "Ви обрали: $selectedItem", Toast.LENGTH_SHORT).show()
    }
    override fun onNothingSelected(parent: AdapterView<*>) {

    }

}

