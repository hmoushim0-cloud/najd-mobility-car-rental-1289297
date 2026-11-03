package sa.factory.najdmobilitycarren.data.repository

import sa.factory.najdmobilitycarren.domain.repository.NajdMobilityRepository
import sa.factory.najdmobilitycarren.hardware.HardwareSensorDriver
import sa.factory.najdmobilitycarren.model.NajdMobilityItem
import sa.factory.najdmobilitycarren.model.NajdMobilityOrder
import sa.factory.najdmobilitycarren.model.NajdMobilityTelemetry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NajdMobilityRepositoryImpl(
    private val sensorDriver: HardwareSensorDriver
) : NajdMobilityRepository {

    private val _catalog = MutableStateFlow<List<NajdMobilityItem>>(listOf(
        NajdMobilityItem("1", "مراجعة كراسات الشروط", "تحليل شامل لكواشف الشروط والمتطلبات الفنية لشراكة OBD-II مع نظام إدارة المناقصات", 150.0, "مكتمل", "المناقصات مفتوحة"),
        NajdMobilityItem("2", "إدارة التقييم المسبق", "تصفية المتقدمين وتقييم التماسات مع تسليط الضوء على الفعاليات الغير متجانسة عبر BLE", 280.0, "محدث", "التأهيل المسبق"),
        NajdMobilityItem("3", "توقيع العقد الرقمي", "دعم HSM لتوقيع العقود باستخدام مفاتيح BLE الفريدة مع رسم بياني للويف", 95.0, "مشفر", "تقييم العروض")
    ))
    override val catalogFlow: Flow<List<NajdMobilityItem>> = _catalog.asStateFlow()

    private val _cart = MutableStateFlow<List<NajdMobilityItem>>(emptyList())
    override val activeCart: StateFlow<List<NajdMobilityItem>> = _cart.asStateFlow()

    override val telemetryFlow: Flow<NajdMobilityTelemetry> = sensorDriver.observePeripheralStream()
        .map { frame ->
            NajdMobilityTelemetry(
                peripheralName = frame.peripheralName,
                status = frame.connectionState,
                liveReading = frame.livePayload,
                batteryPct = 96,
                signalDbm = -54
            )
        }

    override fun addToCart(item: NajdMobilityItem) {
        val current = _cart.value.toMutableList()
        current.add(item)
        _cart.value = current
    }

    override fun removeFromCart(item: NajdMobilityItem) {
        val current = _cart.value.toMutableList()
        current.remove(item)
        _cart.value = current
    }

    override fun clearCart() {
        _cart.value = emptyList()
    }

    override suspend fun checkout(paymentMethod: String): NajdMobilityOrder {
        val currentItems = _cart.value.toList()
        val subtotal = currentItems.sumOf { it.priceSar }
        val vat = subtotal * 0.15
        val total = subtotal + vat
        val order = NajdMobilityOrder(
            orderId = "ORD-${System.currentTimeMillis() % 100000}",
            items = currentItems,
            subtotalSar = subtotal,
            vatSar = vat,
            totalSar = total,
            paymentMethod = paymentMethod
        )
        clearCart()
        return order
    }

    override suspend fun refreshCatalog() {
        // Offline-first sync logic
    }
}
