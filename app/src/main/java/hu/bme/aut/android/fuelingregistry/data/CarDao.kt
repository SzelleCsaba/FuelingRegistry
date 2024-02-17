package hu.bme.aut.android.fuelingregistry.data

import androidx.room.*

@Dao
interface CarDao {
    @Query("SELECT * FROM cardata")
    fun getAll(): List<CarData>

    @Insert
    fun insert(carData: CarData): Long

    @Update
    fun update(carData: CarData)

    @Delete
    fun deleteItem(carData: CarData)

    @Query("DELETE FROM fuelingdata WHERE carId = :carId")
    fun deleteCascade(carId: Long?)

}