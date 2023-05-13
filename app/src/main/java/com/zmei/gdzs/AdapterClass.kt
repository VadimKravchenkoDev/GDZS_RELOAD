package com.zmei.gdzs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zmei.gdzs.databinding.ZvenoActivityBinding

class AdapterClass : RecyclerView.Adapter <AdapterClass.HolderClass>(){
    val zvenoList = ArrayList<ZvenoDataClass>()
    class HolderClass(item: View): RecyclerView.ViewHolder(item) {
        val binding = ZvenoActivityBinding.bind(item)
        fun bind(zveno: ZvenoDataClass)=with(binding){

            edSername.setText(zveno.sername)
            edPressure.setText(zveno.pressure.toString())

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
        zvenoList.add(ZvenoDataClass("", 0))
        notifyItemInserted(zvenoList.size - 1)
    }

}