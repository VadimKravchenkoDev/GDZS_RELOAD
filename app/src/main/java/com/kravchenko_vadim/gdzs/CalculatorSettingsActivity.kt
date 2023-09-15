package com.kravchenko_vadim.gdzs

import AdapterClass
import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.*
import com.kravchenko_vadim.gdzs.constant.Constant
import com.kravchenko_vadim.gdzs.databinding.ActivityCalculatorSettingsBinding
import drawable.ItemOffsetDecoration
import java.util.*

class CalculatorSettingsActivity : AppCompatActivity() {
    lateinit var binding: ActivityCalculatorSettingsBinding
    private val adapter: AdapterClass by lazy { AdapterClass() } //ініціалізація списку ланки ГДЗС
    private val onItemSelectedListener: AdapterView.OnItemSelectedListener =
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                Log.d("mylog", "onItemSelected")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.d("mylog", "onNothingSelected")
            }
        }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCalculatorSettingsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        spinners()  // функція для роботи спінерів
        binding.recyclerView.adapter = adapter
        repeat(2) { adapter.addZveno() }
        binding.btAdd.setOnClickListener {// при тиснені на кнопку додается поле для додаткового газодимозахисника
            if (adapter.zvenoList.size < 5) {
                adapter.addZveno()
            } else if (adapter.zvenoList.size >= 5) Toast.makeText(
                this, "Максимум 5 чол. у звені", Toast.LENGTH_SHORT
            ).show()
        }
        binding.btRemove.setOnClickListener {// при тиснені на кнопку прибирается 1 поле для газодимозахисника
            removeLastItem()
        }
        binding.recyclerView.addItemDecoration(ItemOffsetDecoration(16))
        binding.btnSelectTime.setOnClickListener {/*обираємо час входу ланки*/
            showTimePickerDialog()
        }
        val rootView = findViewById<View>(android.R.id.content)
        // Додаємо обробник подій натискання на кореневе представлення
        rootView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                // Скриваемо клавиатуру
                hideKeyboard()
            }
            false
        }
        binding.buttonCalc.setOnClickListener {
            onClick()
        }
    }

    // функція для роботи спінерів
    private fun spinners() {
        val spinner1: Spinner = binding.spinnerAction
        val spinnerAdapter1 =
            ArrayAdapter.createFromResource(this, R.array.action, R.layout.spinner_prompt_item)
        spinnerAdapter1.setDropDownViewResource(R.layout.spinner_item)
        spinner1.adapter = spinnerAdapter1
        spinner1.onItemSelectedListener =
            onItemSelectedListener/* додаемо спинер з вибором активности */
        val spinner2: Spinner = binding.spinnerAction2
        val spinnerAdapter2 =
            ArrayAdapter.createFromResource(this, R.array.apparatus, R.layout.spinner_prompt_item)
        spinnerAdapter2.setDropDownViewResource(R.layout.spinner_item)
        spinner2.adapter = spinnerAdapter2
        spinner2.onItemSelectedListener =
            onItemSelectedListener/* додаемо спинер з вибором типу апаратів */
        val spinner3: Spinner = binding.spinnerAction3
        val spinnerAdapter3 =
            ArrayAdapter.createFromResource(this, R.array.workload, R.layout.spinner_prompt_item)
        spinnerAdapter3.setDropDownViewResource(R.layout.spinner_item)
        spinner3.adapter = spinnerAdapter3
        spinner3.onItemSelectedListener =
            onItemSelectedListener/* додаемо спинер з вибором типу навантаження */
    }

    private fun onClick() { //функція яка перевіряє введені данні, відправляє їх на слудуючу сторінку, відкриває новий єкран
        val spinner1: Spinner = binding.spinnerAction
        val spinner2: Spinner = binding.spinnerAction2
        val spinner3: Spinner = binding.spinnerAction3
        val selected1 = spinner1.selectedItemPosition
        val selected2 = spinner2.selectedItemPosition
        val selected3 = spinner3.selectedItemPosition
        val sizeList = adapter.zvenoList.size
        val timeBt = binding.btnSelectTime.text.toString()
        when { /*перевіряємо чи вірно введені данні*/
            selected1 == 0 -> showErrorMessage("Оберіть вид роботи!")
            selected2 == 0 -> showErrorMessage("Оберіть вид апаратів")
            selected3 == 0 -> showErrorMessage("Оберіть вид навантаження")
            timeBt == "Обрати час" -> showErrorMessage("Оберіть час входу ланки")
            sizeList == 1 -> showErrorMessage("Мінімум 2 чол. в ланці")
            checkAllFieldsFilled().not() -> showErrorMessage("Введіть призвища та тиск")/* якщо все заповнено вірно то при тисненні кнопки запускається екран розрахунків*/
            else -> {
                binding.textAction.visibility = View.GONE
                val minPressure =
                    adapter.zvenoList.minByOrNull { it.pressure.toInt() }?.pressure?.toInt()
                        ?: 0 /*пошук мінімального тиску*/
                val maxPressure =
                    adapter.zvenoList.maxByOrNull { it.pressure.toInt() }?.pressure?.toInt()
                        ?: 0 /*пошук максимального тиску*/
                val dragerMsiPresuureOk =
                    (binding.accessToWork.isChecked  && (selected2 == 1 || selected2 == 3) || (binding.accessToWork.isChecked && selected2 == 2))
                val minPressureDragerError =
                    (minPressure <= Constant.minPressureDrager && (selected2 == 1 || selected2 == 3))
                val minPressureAsvErorr = (minPressure <= Constant.minPressureASP && selected2 == 2)
                val maxPressureDragerError =
                    (maxPressure >= Constant.maxPressureDrager && (selected2 == 1 || selected2 == 3))
                val maxPressureAsvErorr = (maxPressure >= Constant.maxPressureASP && selected2 == 2)
                when {
                    dragerMsiPresuureOk -> {
                        //відправка отриманих данних на слідуючий єкран
                        val intent = Intent(this, TimerCalculatorWorkActivity::class.java)
                        intent.putExtra("minPressure", minPressure)
                        intent.putExtra("aparat", selected2)
                        intent.putExtra("action", selected3)
                        intent.putExtra("timeAction", timeBt)
                        ActivityAnimation.startActivityWithAnimation(this, intent)
                    }

                    minPressureDragerError -> showErrorMessage("Мінімальний тиск 270 атм.!")
                    minPressureAsvErorr -> showErrorMessage("Мінімальний тиск 180 атм.!")
                    maxPressureDragerError -> showErrorMessage("Максимальний тиск 330 атм.!")
                    maxPressureAsvErorr -> showErrorMessage("Максимальний тиск 230 атм.!")
                    else -> {
                        val intent = Intent(this, TimerCalculatorWorkActivity::class.java)
                        intent.putExtra("minPressure", minPressure)
                        intent.putExtra("aparat", selected2)
                        intent.putExtra("action", selected3)
                        intent.putExtra("timeAction", timeBt)
                        ActivityAnimation.startActivityWithAnimation(this, intent)
                    }
                }
            }
        }
    }

    private fun showErrorMessage(errorMessage: String) {
        binding.textAction.visibility = View.VISIBLE
        binding.textAction.text = errorMessage
        binding.textAction.setTextColor(Color.RED)/*функція яка використовується для виводу повідомлень*/
    }

    private fun showTimePickerDialog() {// функція для вибору часу
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val minutes = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(this, { _, hour, minute ->
            val selectedTime = String.format("%02d:%02d", hour, minute)
            binding.btnSelectTime.text = selectedTime
        }, hourOfDay, minutes, true)
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

    private fun removeLastItem() { //фунція для видалення поля у звені ГДЗС
        val lastIndex = adapter.zvenoList.lastIndex
        if (lastIndex > 0) {
            adapter.zvenoList.removeAt(lastIndex)
            adapter.notifyItemRemoved(lastIndex)
        }
    }
}

