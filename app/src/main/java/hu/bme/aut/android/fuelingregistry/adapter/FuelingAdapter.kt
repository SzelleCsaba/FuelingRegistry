package hu.bme.aut.android.fuelingregistry.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.fuelingregistry.data.FuelingData
import hu.bme.aut.android.fuelingregistry.databinding.FuelingListBinding
import java.io.Serializable
import kotlin.math.roundToInt

class FuelingAdapter(private val listener: FuelingClickListener) :
    RecyclerView.Adapter<FuelingAdapter.FuelingViewHolder>() {

    private val items = mutableListOf<FuelingData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = FuelingViewHolder(
        FuelingListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: FuelingViewHolder, position: Int) {
        val fueling = items[position]

        holder.binding.tvType.text = "üzemanyag: ${replaceEnum(fueling.type)}"
        holder.binding.tvAmount.text = "mennyiség: ${fueling.amount}L"
        holder.binding.tvPricePerLiter.text = "ár: ${fueling.pricePerLiter}Ft/L"
        holder.binding.tvTotalPrice.text = "összeg: ${(fueling.amount * fueling.pricePerLiter).roundToInt()}Ft"
        holder.binding.OdometerAtFueling.text = "óraállás: ${fueling.odometerAtFueling}km"
        holder.binding.tvDate.text = "${fueling.dateYear}.${fueling.dateMonth}.${fueling.dateDay}."
        if (fueling.isFullTank && position > 0)
            holder.binding.FuelConsumption.text = "fogyasztás: ${(fueling.amount/(fueling.odometerAtFueling -items[position-1].odometerAtFueling)*100*100).roundToInt()/100.0}L/100km"
        else holder.binding.FuelConsumption.text =""

        holder.binding.ibRemove2.setOnClickListener {
            listener.onFuelingRemoved(fueling)
        }
    }

    private fun replaceEnum(type: FuelingData.Type): String {
        return when (type) {
            FuelingData.Type.stdgasoline -> "95 Benzin"
            FuelingData.Type.stddiesel -> "Dízel"
            FuelingData.Type.premiumgasoline -> "100 Benzin"
            FuelingData.Type.premiumdiesel -> "Prémium Dízel"
        }
    }

    fun addFueling(fueling: FuelingData) {
        items.add(fueling)
        notifyItemInserted(items.size - 1)
    }

    fun deleteFueling(fueling: FuelingData) {
        var i = items.indexOf(fueling)
        items.remove(fueling)
        notifyItemRemoved(i)
    }

    fun update(fuelings: List<FuelingData>) {
        items.clear()
        items.addAll(fuelings)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    interface FuelingClickListener {
        fun onFuelingChanged(fueling: FuelingData)
        fun onFuelingRemoved(fueling: FuelingData)
    }

    inner class FuelingViewHolder(val binding: FuelingListBinding) : RecyclerView.ViewHolder(binding.root)
}