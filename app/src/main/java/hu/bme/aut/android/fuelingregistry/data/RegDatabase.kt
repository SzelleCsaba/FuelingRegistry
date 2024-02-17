package hu.bme.aut.android.fuelingregistry.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [CarData::class, FuelingData::class], version = 1)
@TypeConverters(value = [FuelingData.Type::class])
abstract class RegDatabase : RoomDatabase() {
        abstract fun FuelingDao(): FuelingDao
        abstract fun CarDao(): CarDao

        companion object {
            fun getDatabase(applicationContext: Context): RegDatabase {
                return Room.databaseBuilder(
                    applicationContext,
                    RegDatabase::class.java,
                    "fueling-registry"
                ).build()
            }
        }
}