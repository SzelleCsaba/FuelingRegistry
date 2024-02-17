package hu.bme.aut.android.fuelingregistry

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.fuelingregistry.adapter.FuelingAdapter
import hu.bme.aut.android.fuelingregistry.data.FuelingData
import hu.bme.aut.android.fuelingregistry.data.RegDatabase
import hu.bme.aut.android.fuelingregistry.databinding.ActivityFuelBinding
import hu.bme.aut.android.fuelingregistry.fragments.NewFuelingDialogFragment
import java.io.Serializable
import kotlin.concurrent.thread

class FuelActivity : AppCompatActivity(), FuelingAdapter.FuelingClickListener, NewFuelingDialogFragment.NewFuelingDialogListener{
    private lateinit var binding: ActivityFuelBinding

    private lateinit var database: RegDatabase
    private lateinit var adapter: FuelingAdapter
    private var carId: Long = 0
    private var carName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFuelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        carId = intent.extras?.getLong("id") ?: carId
        carName = intent.extras?.getString("name") ?: carName

        setSupportActionBar(binding.toolbar2)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.car_name , carName)

        database = RegDatabase.getDatabase(applicationContext)

        binding.fab.setOnClickListener {
            NewFuelingDialogFragment().show(supportFragmentManager, NewFuelingDialogFragment.TAG)
        }
        initRecyclerView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId ==android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initRecyclerView() {
        adapter = FuelingAdapter(this)
        binding.rvFuel.layoutManager = LinearLayoutManager(this)
        binding.rvFuel.adapter = adapter
        loadItemsInBackground()
    }

    private fun loadItemsInBackground() {
        thread {
            val items = database.FuelingDao().getFuelingsOfACar(carId)
            runOnUiThread {
                adapter.update(items)
            }
        }
    }

    override fun onFuelingChanged(fueling: FuelingData) {
        thread {
            database.FuelingDao().update(fueling)
            Log.d("FuelActivity", "Fueling update was successful")
        }
    }

    override fun onFuelingRemoved(fueling: FuelingData) {
        thread {
            database.FuelingDao().deleteItem(fueling.odometerAtFueling, fueling.amount, fueling.carId)
            runOnUiThread {
                adapter.deleteFueling(fueling)
            }
        }
    }

    override fun onFuelingCreated(newFueling: FuelingData) {
        thread {
            newFueling.carId = carId
            val insertId = database.FuelingDao().insert(newFueling)
            newFueling.id = insertId
            runOnUiThread {
                adapter.addFueling(newFueling)
            }
        }
    }
}