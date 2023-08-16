package com.kravchenko_vadim.gdzs

import AdapterClass
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.kravchenko_vadim.gdzs.constant.Constant
import com.kravchenko_vadim.gdzs.databinding.ActivityMain2Binding
import drawable.ItemOffsetDecoration
import java.util.*

class MainActivity2 : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    lateinit var binding : ActivityMain2Binding
    private lateinit var adapter: AdapterClass
    private lateinit var timeTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMain2Binding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        spinners()  // функція для роботи спінерів
        adapter = AdapterClass() //ініціалізація списку ланки ГДЗС
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        repeat(2) { adapter.addZveno() }
            binding.btAdd.setOnClickListener {// при тиснені на кнопку додается поле для додаткового газодимозахисника
                if(adapter.zvenoList.size < 5)  { adapter.addZveno() }
                else if (adapter.zvenoList.size>=5) Toast.makeText(this, "Максимум 5 чол. у звені", Toast.LENGTH_SHORT).show()
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
                // Скрываем клавиатуру
                hideKeyboard()
            }
            false
        }
    }
// функція для роботи спінерів
    fun spinners() {
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
    }
    fun onClick(view: View) { //функція яка перевіряє введені данні, відправляє їх на слудуючу сторінку, відкриває новий єкран
        val spinner1: Spinner = binding.spinnerAction
        val spinner2: Spinner = binding.spinnerAction2
        val spinner3: Spinner = binding.spinnerAction3
        val selected1 = spinner1.selectedItemPosition
        val selected2 = spinner2.selectedItemPosition
        val selected3 = spinner3.selectedItemPosition
        var sizeList = adapter.zvenoList.size
        var timeBt = binding.btnSelectTime.text.toString()
        when { /*перевіряємо чи вірно введені данні*/
            selected1 == 0 -> showErrorMessage("Оберіть вид роботи!")
            selected2 == 0 -> showErrorMessage("Оберіть вид апаратів")
            selected3 == 0 -> showErrorMessage("Оберіть вид навантаження")
            timeBt == "-Обрати час-" -> showErrorMessage("Оберіть час входу ланки")
            sizeList == 1 -> showErrorMessage("Мінімум 2 чол. в ланці")
            checkAllFieldsFilled() == false -> showErrorMessage("Введіть призвища та тиск")
            /* якщо все заповнено вірно то при тисненні кнопки запускається екран розрахунків*/
            else -> {
                binding.textAction.visibility = View.GONE
                val intent = Intent(this, MainActivity3::class.java)
                val minPressure = adapter.zvenoList.minByOrNull { it.pressure.toInt() }?.pressure?.toInt() ?: 0 /*пошук мінімального тиску*/
                val maxPressure = adapter.zvenoList.maxByOrNull { it.pressure.toInt() }?.pressure?.toInt() ?: 0 /*пошук максимального тиску*/
                when {
                    ((binding.accessToWork.isChecked == true && minPressure>=(Constant.reservDrager+2) && (selected2 == 1 || selected2 == 3)) || (binding.accessToWork.isChecked == true && (minPressure>=(Constant.reservASV+2) && selected2 == 2)))-> {
                        //відправка отриманих данних на слідуючий єкран
                        intent.putExtra("minPressure", minPressure)
                        intent.putExtra("aparat", selected2)
                        intent.putExtra("action", selected3)
                        intent.putExtra("timeAction", timeBt)
                        startActivity(intent)
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    }
                    (minPressure<=Constant.minPressureDrager && (selected2 == 1 || selected2 == 3))->showErrorMessage("Мінімальний тиск 270 атм.!")
                    (minPressure<=Constant.minPressureASP && selected2 == 2)-> showErrorMessage("Мінімальний тиск 180 атм.!")
                    (maxPressure>=Constant.maxPressureDrager && (selected2 == 1 || selected2 == 3)) ->showErrorMessage("Максимальний тиск 330 атм.!")
                    (maxPressure>=Constant.maxPressureASP && selected2 == 2) ->showErrorMessage("Максимальний тиск 230 атм.!")
                     else ->  {
                                intent.putExtra("minPressure", minPressure)
                                intent.putExtra("aparat", selected2)
                                intent.putExtra("action", selected3)
                                intent.putExtra("timeAction", timeBt)
                                startActivity(intent)
                         val options = ActivityOptionsCompat.makeCustomAnimation(
                             this,
                             R.anim.fade_in, R.anim.fade_out
                         )
                         ActivityCompat.startActivity(this, intent, options.toBundle())
                     }
                }
            }
        }
    }
    private fun showErrorMessage(errorMessage: String) {
        binding.textAction.visibility = View.VISIBLE
        binding.textAction.text = errorMessage
        binding.textAction.setTextColor(Color.RED)
        /*функція яка використовується для виводу повідомлень*/
    }
    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
    }
    override fun onNothingSelected(parent: AdapterView<*>) {
    }
    private fun showTimePickerDialog() {// функція для вибору часу
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(this, { _, hour, minute ->
        var selectedTime = String.format("%02d:%02d", hour, minute)
        binding.btnSelectTime.text = selectedTime
        }, hourOfDay, minute, true)
        timePickerDialog.show()
    }
    private fun checkAllFieldsFilled(): Boolean {
        for (zveno in adapter.zvenoList)
        {
            if (zveno.sername.isEmpty() || zveno.pressure.isEmpty())
            {
                return false
            }
        }
        return true
    }
    private fun removeLastItem() { //фунція для видалення поля у звені ГДЗС
        val lastIndex = adapter.zvenoList.lastIndex
        if (lastIndex > 0)
        {
            adapter.zvenoList.removeAt(lastIndex)
            adapter.notifyItemRemoved(lastIndex)
        }
    }
    private fun hideKeyboard() {//фунція для приховування клавіатури при натисканні на єкран
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusView = currentFocus
        if (currentFocusView != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocusView.windowToken, 0)
        }
    }
}

