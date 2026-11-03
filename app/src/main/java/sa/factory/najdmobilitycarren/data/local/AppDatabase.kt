package sa.factory.najdmobilitycarren.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NajdMobilityEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun car_rentalDao(): NajdMobilityDao
}
