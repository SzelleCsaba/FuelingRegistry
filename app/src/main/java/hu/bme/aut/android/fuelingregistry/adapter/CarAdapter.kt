package hu.bme.aut.android.fuelingregistry.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.fuelingregistry.FuelActivity
import hu.bme.aut.android.fuelingregistry.data.CarData
import hu.bme.aut.android.fuelingregistry.data.RegDatabase
import hu.bme.aut.android.fuelingregistry.databinding.CarListBinding


class CarAdapter(private val listener: CarClickListener, private val context: Context) :
    RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

    private val items = mutableListOf<CarData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CarViewHolder(
        CarListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val car = items[position]
        holder.binding.tvName.text = "Autó: ${car.name}"
        holder.binding.tvDescription.text = "Leírás: ${car.description}"
        holder.binding.tvFuelTank.text = "Üzemanyagtartály: ${car.capacity}L"

        holder.binding.ibRemove.setOnClickListener {
            listener.onCarRemoved(car)
        }
        holder.binding.ll.setOnClickListener {
            val intent = Intent (context, FuelActivity::class.java)
            intent.putExtra("id", car.id)
            intent.putExtra("name", car.name)
            context.startActivity(intent)
        }
    }

    fun addCar(car: CarData) {
        items.add(car)
        notifyItemInserted(items.size - 1)
    }

    fun deleteCar(car: CarData) {
        var i = items.indexOf(car)
        items.remove(car)
        notifyItemRemoved(i)
    }

    fun update(cars: List<CarData>) {
        items.clear()
        items.addAll(cars)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    interface CarClickListener {
        fun onCarChanged(car: CarData)
        fun onCarRemoved(car: CarData)
    }

    inner class CarViewHolder(val binding: CarListBinding) : RecyclerView.ViewHolder(binding.root)
}