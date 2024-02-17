package hu.bme.aut.android.fuelingregistry.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

@Entity(tableName = "fuelingdata")
data class FuelingData(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "amount") var amount: Double,
    @ColumnInfo(name = "carId") var carId: Long? = null,
    @ColumnInfo(name = "type") var type: Type,
    @ColumnInfo(name = "pricePerLiter") var pricePerLiter: Int,
    @ColumnInfo(name = "isFullTank") var isFullTank: Boolean,
    @ColumnInfo(name = "odometerAtFueling") var odometerAtFueling: Int,
    @ColumnInfo(name = "dateDay") var dateDay: Int,
    @ColumnInfo(name = "dateMonth") var dateMonth: Int,
    @ColumnInfo(name = "dateYear") var dateYear: Int,
){
    enum class Type {
        stdgasoline, stddiesel, premiumgasoline, premiumdiesel;
        companion object {
            @JvmStatic
            @TypeConverter
            fun getByOrdinal(ordinal: Int): Type? {
                var ret: Type? = null
                for (type in values()) {
                    if (type.ordinal == ordinal) {
                        ret = type
                        break
                    }
                }
                return ret
            }

            @JvmStatic
            @TypeConverter
            fun toInt(type: Type): Int {
                return type.ordinal
            }
        }
    }
}
