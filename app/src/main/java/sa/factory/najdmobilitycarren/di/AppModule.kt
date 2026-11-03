package sa.factory.najdmobilitycarren.di

import sa.factory.najdmobilitycarren.data.repository.NajdMobilityRepositoryImpl
import sa.factory.najdmobilitycarren.domain.repository.NajdMobilityRepository
import sa.factory.najdmobilitycarren.domain.usecase.*
import sa.factory.najdmobilitycarren.hardware.HardwareSensorDriver
import sa.factory.najdmobilitycarren.ui.viewmodel.NajdMobilityViewModel

class DiContainer {
    val sensorDriver = HardwareSensorDriver()
    val repository: NajdMobilityRepository = NajdMobilityRepositoryImpl(sensorDriver)

    val getCatalogUseCase = GetCatalogUseCase(repository)
    val manageCartUseCase = ManageCartUseCase(repository)
    val processCheckoutUseCase = ProcessCheckoutUseCase(repository)
    val observeHardwareTelemetryUseCase = ObserveHardwareTelemetryUseCase(repository)

    fun provideViewModel(): NajdMobilityViewModel = NajdMobilityViewModel(
        getCatalogUseCase,
        manageCartUseCase,
        processCheckoutUseCase,
        observeHardwareTelemetryUseCase
    )
}
