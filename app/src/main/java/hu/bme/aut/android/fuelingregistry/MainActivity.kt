package hu.bme.aut.android.fuelingregistry

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.fuelingregistry.adapter.CarAdapter
import hu.bme.aut.android.fuelingregistry.data.CarData
import hu.bme.aut.android.fuelingregistry.data.RegDatabase
import hu.bme.aut.android.fuelingregistry.databinding.ActivityMainBinding
import hu.bme.aut.android.fuelingregistry.fragments.NewCarDialogFragment
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity(), CarAdapter.CarClickListener, NewCarDialogFragment.NewCarDialogListener {
    private lateinit var binding: ActivityMainBinding

    private lateinit var database: RegDatabase
    private lateinit var adapter: CarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar1)

        database = RegDatabase.getDatabase(applicationContext)

        binding.fab.setOnClickListener {
            NewCarDialogFragment().show(
                supportFragmentManager,
                NewCarDialogFragment.TAG
            )
        }
        initRecyclerView()
    }
    private fun initRecyclerView() {
        adapter = CarAdapter(this, this)
        binding.rvMain.layoutManager = LinearLayoutManager(this)
        binding.rvMain.adapter = adapter
        loadItemsInBackground()
    }

    private fun loadItemsInBackground() {
        thread {
            val items = database.CarDao().getAll()
            runOnUiThread {
                adapter.update(items)
            }
        }
    }
    override fun onCarChanged(car: CarData) {
        thread {
            database.CarDao().update(car)
            Log.d("MainActivity", "Car update was successful")
        }
    }
    override fun onCarCreated(newCar: CarData) {
        thread {
            val insertId = database.CarDao().insert(newCar)
            newCar.id = insertId
            runOnUiThread {
                adapter.addCar(newCar)
            }
        }
    }

    override fun onCarRemoved(car: CarData) {
        thread {
            database.CarDao().deleteItem(car)
            database.CarDao().deleteCascade(car.id)
            runOnUiThread {
                adapter.deleteCar(car)
            }
        }
    }
}