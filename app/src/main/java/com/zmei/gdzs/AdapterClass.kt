import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zmei.gdzs.R
import com.zmei.gdzs.databinding.ZvenoActivityBinding

class AdapterClass : RecyclerView.Adapter<AdapterClass.HolderClass>() {
    val zvenoList = ArrayList<ZvenoDataClass>()

    class HolderClass(item: View) : RecyclerView.ViewHolder(item) {
        val binding = ZvenoActivityBinding.bind(item)

        fun bind(zveno: ZvenoDataClass) = with(binding) {
            edSername.setText(zveno.sername)
            edPressure.setText(zveno.pressure)

            edSername.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    zveno.sername = s.toString()
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            edPressure.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    zveno.pressure = s.toString()
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderClass {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.zveno_activity, parent, false)
        return HolderClass(view)
    }

    override fun getItemCount(): Int {
        return zvenoList.size
    }

    override fun onBindViewHolder(holder: HolderClass, position: Int) {
        holder.bind(zvenoList[position])
    }

    fun addZveno() {
        zvenoList.add(ZvenoDataClass("", ""))
        notifyItemInserted(zvenoList.size - 1)
    }
}

class ZvenoDataClass(var sername: String, var pressure: String)
