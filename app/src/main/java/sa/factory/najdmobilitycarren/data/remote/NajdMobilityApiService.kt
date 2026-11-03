package sa.factory.najdmobilitycarren.data.remote

import sa.factory.najdmobilitycarren.model.NajdMobilityItem
import sa.factory.najdmobilitycarren.model.NajdMobilityOrder

interface NajdMobilityApiService {
    suspend fun fetchCatalog(): List<NajdMobilityItem>
    suspend fun submitOrder(order: NajdMobilityOrder): Boolean
}
