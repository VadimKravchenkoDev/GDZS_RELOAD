package com.zmei.gdzs

import AdapterClass
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.zmei.gdzs.databinding.ActivityMain2Binding
import drawable.ItemOffsetDecoration
import java.text.SimpleDateFormat
import java.util.*

class MainActivity2 : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    lateinit var binding : ActivityMain2Binding

    private lateinit var adapter: AdapterClass
    private lateinit var timeTextView: TextView

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
            if(adapter.zvenoList.size < 5)  {
                adapter.addZveno()
            }
            else if (adapter.zvenoList.size>=5) Toast.makeText(this, "Максимум 5 чол. у звені", Toast.LENGTH_SHORT).show()
        }
        binding.btRemove.setOnClickListener {
            removeLastItem()
        }
        binding.recyclerView.addItemDecoration(ItemOffsetDecoration(16))
/*обираємо час входу ланки*/
        binding.btnSelectTime.setOnClickListener {
            showTimePickerDialog()
        }
    }


    fun onClick(view: View) {
        /*перевіряємо чи введені данні*/
        val spinner1: Spinner = binding.spinnerAction
        val spinner2: Spinner = binding.spinnerAction2
        val spinner3: Spinner = binding.spinnerAction3
        val selected1 = spinner1.selectedItemPosition
        val selected2 = spinner2.selectedItemPosition
        val selected3 = spinner3.selectedItemPosition
        var sizeList = adapter.zvenoList.size

        var timeBt = binding.btnSelectTime.text.toString()
        when {
            selected1 == 0 -> showErrorMessage("Оберіть вид роботи!")
            selected2 == 0 -> showErrorMessage("Оберіть вид апаратів")
            selected3 == 0 -> showErrorMessage("Оберіть вид навантаження")
            timeBt == "-Обрати час-" -> showErrorMessage("Оберіть час входу ланки")
            sizeList == 1 ->showErrorMessage("Мінімум 2 чол. в ланці")
            checkAllFieldsFilled() == false -> showErrorMessage("Введіть призвища та тиск")

                        /* якщо все заповнено то при тисненні кнопки запускається екран розрахунків*/
            else -> {
                binding.textAction.visibility = View.GONE
                val intent = Intent(this, MainActivity3::class.java)
                val minPressure = adapter.zvenoList.minByOrNull { it.pressure.toInt() }?.pressure?.toInt() ?: 400
                /*пошук мінімального тиску*/
                intent.putExtra("minPressure", minPressure)
                intent.putExtra("aparat", selected2)
                intent.putExtra("action", selected3)
                intent.putExtra("timeAction", timeBt)
                startActivity(intent)
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }
        }
            }
    private fun showErrorMessage(errorMessage: String) {
        binding.textAction.visibility = View.VISIBLE
        binding.textAction.text = errorMessage
        /*функция которую мы используем для вывода ошибки*/
    }
    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
    }
    override fun onNothingSelected(parent: AdapterView<*>) {
    }
    private fun showTimePickerDialog() {
        // Создаем диалог выбора времени
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(this, { _, hour, minute ->
            // Вызывается, когда пользователь выбирает время
            var selectedTime = String.format("%02d:%02d", hour, minute)
            binding.btnSelectTime.text = selectedTime
        }, hourOfDay, minute, true)
        // Отображаем диалог выбора времени
        timePickerDialog.show()
    }
    private fun checkAllFieldsFilled(): Boolean {
        for (zveno in adapter.zvenoList) {
            if (zveno.sername.isEmpty() || zveno.pressure.isEmpty()) {
                return false
            }
        }
        return true
    }

    private fun removeLastItem() {
        val lastIndex = adapter.zvenoList.lastIndex
        if (lastIndex > 0) {
            adapter.zvenoList.removeAt(lastIndex)
            adapter.notifyItemRemoved(lastIndex)
        }
    }


}

