package sa.factory.najdmobilitycarren.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import sa.factory.najdmobilitycarren.model.NajdMobilityItem

@Entity(tableName = "car_rental_items_cache")
data class NajdMobilityEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val priceSar: Double,
    val badge: String,
    val category: String,
    val status: String
) {
    fun toDomain(): NajdMobilityItem = NajdMobilityItem(
        id = id,
        title = title,
        description = description,
        priceSar = priceSar,
        badge = badge,
        category = category,
        status = status
    )
}
