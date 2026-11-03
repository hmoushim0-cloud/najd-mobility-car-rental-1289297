package sa.factory.najdmobilitycarren.domain.repository

import sa.factory.najdmobilitycarren.model.NajdMobilityItem
import sa.factory.najdmobilitycarren.model.NajdMobilityOrder
import sa.factory.najdmobilitycarren.model.NajdMobilityTelemetry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface NajdMobilityRepository {
    val catalogFlow: Flow<List<NajdMobilityItem>>
    val activeCart: StateFlow<List<NajdMobilityItem>>
    val telemetryFlow: Flow<NajdMobilityTelemetry>
    
    fun addToCart(item: NajdMobilityItem)
    fun removeFromCart(item: NajdMobilityItem)
    fun clearCart()
    suspend fun checkout(paymentMethod: String): NajdMobilityOrder
    suspend fun refreshCatalog()
}
