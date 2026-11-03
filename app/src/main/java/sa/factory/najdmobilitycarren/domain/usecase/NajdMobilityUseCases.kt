package sa.factory.najdmobilitycarren.domain.usecase

import sa.factory.najdmobilitycarren.domain.repository.NajdMobilityRepository
import sa.factory.najdmobilitycarren.model.NajdMobilityItem
import sa.factory.najdmobilitycarren.model.NajdMobilityOrder
import sa.factory.najdmobilitycarren.model.NajdMobilityTelemetry
import kotlinx.coroutines.flow.Flow

class GetCatalogUseCase(private val repository: NajdMobilityRepository) {
    operator fun invoke(): Flow<List<NajdMobilityItem>> = repository.catalogFlow
}

class ManageCartUseCase(private val repository: NajdMobilityRepository) {
    val cart = repository.activeCart
    fun add(item: NajdMobilityItem) = repository.addToCart(item)
    fun remove(item: NajdMobilityItem) = repository.removeFromCart(item)
    fun clear() = repository.clearCart()
}

class ProcessCheckoutUseCase(private val repository: NajdMobilityRepository) {
    suspend operator fun invoke(paymentMethod: String = "Mada Contactless EMV"): NajdMobilityOrder =
        repository.checkout(paymentMethod)
}

class ObserveHardwareTelemetryUseCase(private val repository: NajdMobilityRepository) {
    operator fun invoke(): Flow<NajdMobilityTelemetry> = repository.telemetryFlow
}
