package sa.factory.najdmobilitycarren.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import sa.factory.najdmobilitycarren.domain.usecase.*
import sa.factory.najdmobilitycarren.model.NajdMobilityItem
import sa.factory.najdmobilitycarren.model.NajdMobilityOrder
import sa.factory.najdmobilitycarren.model.NajdMobilityTelemetry
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class NajdMobilityUiState(
    val catalog: List<NajdMobilityItem> = emptyList(),
    val filteredCatalog: List<NajdMobilityItem> = emptyList(),
    val selectedCategory: String = "الكل",
    val cart: List<NajdMobilityItem> = emptyList(),
    val telemetry: NajdMobilityTelemetry? = null,
    val lastOrder: NajdMobilityOrder? = null,
    val isCheckingOut: Boolean = false,
    val activeTab: String = "home"
)

class NajdMobilityViewModel(
    private val getCatalogUseCase: GetCatalogUseCase,
    private val manageCartUseCase: ManageCartUseCase,
    private val processCheckoutUseCase: ProcessCheckoutUseCase,
    private val observeHardwareTelemetryUseCase: ObserveHardwareTelemetryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NajdMobilityUiState())
    val uiState: StateFlow<NajdMobilityUiState> = _uiState.asStateFlow()

    init {
        // Observe Catalog
        getCatalogUseCase().onEach { list ->
            _uiState.update { it.copy(catalog = list, filteredCatalog = list) }
        }.launchIn(viewModelScope)

        // Observe Cart
        manageCartUseCase.cart.onEach { cartItems ->
            _uiState.update { it.copy(cart = cartItems) }
        }.launchIn(viewModelScope)

        // Observe Telemetry
        observeHardwareTelemetryUseCase().onEach { telem ->
            _uiState.update { it.copy(telemetry = telem) }
        }.launchIn(viewModelScope)
    }

    fun selectCategory(category: String) {
        _uiState.update { state ->
            val filtered = if (category == "الكل") state.catalog else state.catalog.filter { it.category == category }
            state.copy(selectedCategory = category, filteredCatalog = filtered)
        }
    }

    fun switchTab(tab: String) {
        _uiState.update { it.copy(activeTab = tab) }
    }

    fun addToCart(item: NajdMobilityItem) = manageCartUseCase.add(item)
    fun removeFromCart(item: NajdMobilityItem) = manageCartUseCase.remove(item)

    fun checkout() {
        viewModelScope.launch {
            _uiState.update { it.copy(isCheckingOut = true) }
            val order = processCheckoutUseCase()
            _uiState.update { it.copy(isCheckingOut = false, lastOrder = order, activeTab = "checkout") }
        }
    }
}
