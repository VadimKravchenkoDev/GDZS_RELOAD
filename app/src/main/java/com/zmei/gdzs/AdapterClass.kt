package com.zmei.gdzs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class AdapterClass : RecyclerView.Adapter <AdapterClass.HolderClass>(){
    val plantList = ArrayList<PlantDataClass>()
    class HolderClass(item: View): RecyclerView.ViewHolder(item) {
        val binding = PlantActivityBinding.bind(item)
        fun bind(plant: PlantDataClass)=with(binding){
            imageId.setImageResource(plant.imageId)
            titleId.text = plant.title

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderClass {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.plant_activity, parent, false)
        return HolderClass(view)
    }

    override fun getItemCount(): Int {
        return plantList.size
    }

    override fun onBindViewHolder(holder: HolderClass, position: Int) {
        holder.bind(plantList[position])
    }

    fun addPlant (plant: PlantDataClass){
        plantList.add(plant)
        notifyDataSetChanged()
    }
}