package hu.bme.aut.android.fuelingregistry.data

import androidx.room.*

@Dao
interface FuelingDao {
    @Query("SELECT * FROM fuelingdata")
    fun getAll(): List<FuelingData>

    @Insert
    fun insert(fuelingData: FuelingData): Long

    @Update
    fun update(fuelingData: FuelingData)

    @Query("DELETE FROM fuelingdata WHERE odometerAtFueling = :odo AND amount = :amount AND carId = :carId")
    fun deleteItem(odo: Int, amount: Double, carId: Long?)

    @Query("SELECT * FROM fuelingdata INNER JOIN cardata ON  cardata.id =fuelingdata.carId WHERE carId = :id ORDER BY odometerAtFueling ")
    fun getFuelingsOfACar(id: Long): List<FuelingData>
}
