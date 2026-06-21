package com.mashalmazen.pixoji.presentation.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mashalmazen.pixoji.data.datastore.SettingsDataStore
import com.mashalmazen.pixoji.data.usecase.ProcessImageUseCaseImpl
import com.mashalmazen.pixoji.data.usecase.SaveImageUseCaseImpl
import com.mashalmazen.pixoji.domain.model.EmojiCategory
import com.mashalmazen.pixoji.domain.model.ProcessingSettings
import com.mashalmazen.pixoji.domain.model.ProcessingState
import com.mashalmazen.pixoji.domain.model.ThemeMode
import com.mashalmazen.pixoji.domain.repository.EmojiRepository
import com.mashalmazen.pixoji.domain.usecase.ProcessImageUseCase
import com.mashalmazen.pixoji.domain.usecase.SaveImageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PixojiUiState(
    val processingState: ProcessingState = ProcessingState.Idle,
    val currentSettings: ProcessingSettings = ProcessingSettings(),
    val isSaving: Boolean = false,
    val saveMessage: String = "",
    val selectedImageBitmap: Bitmap? = null
)

class PixojiViewModel(
    private val settingsDataStore: SettingsDataStore,
    private val processImageUseCase: ProcessImageUseCase,
    private val saveImageUseCase: SaveImageUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PixojiUiState())
    val uiState: StateFlow<PixojiUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            settingsDataStore.settingsFlow.collect { settings ->
                _uiState.update { state ->
                    state.copy(currentSettings = settings)
                }
            }
        }
    }

    fun setSelectedImage(bitmap: Bitmap) {
        _uiState.update { state ->
            state.copy(selectedImageBitmap = bitmap)
        }
    }

    fun startProcessing() {
        val selectedImage = _uiState.value.selectedImageBitmap ?: return
        val settings = _uiState.value.currentSettings

        viewModelScope.launch {
            processImageUseCase.executeProcessing(selectedImage, settings)
                .collect { state ->
                    _uiState.update { it.copy(processingState = state) }
                }
        }
    }

    fun cancelProcessing() {
        processImageUseCase.cancel()
        _uiState.update { state ->
            state.copy(processingState = ProcessingState.Idle)
        }
    }

    fun saveProcessedImage() {
        val successState = _uiState.value.processingState as? ProcessingState.Success
            ?: return

        viewModelScope.launch {
            _uiState.update { state -> state.copy(isSaving = true) }

            val result = saveImageUseCase.saveImageToGallery(successState.outputBitmap)

            result.onSuccess { path ->
                _uiState.update { state ->
                    state.copy(
                        isSaving = false,
                        saveMessage = "Image saved successfully to Pixoji folder"
                    )
                }
            }

            result.onFailure { error ->
                _uiState.update { state ->
                    state.copy(
                        isSaving = false,
                        saveMessage = "Failed to save image: ${error.message}"
                    )
                }
            }
        }
    }

    fun updateGridDensity(density: Int) {
        viewModelScope.launch {
            settingsDataStore.updateGridDensity(density)
        }
    }

    fun updateResolutionMultiplier(multiplier: Int) {
        viewModelScope.launch {
            settingsDataStore.updateResolutionMultiplier(multiplier)
        }
    }

    fun updateEmojiCategory(category: EmojiCategory) {
        viewModelScope.launch {
            settingsDataStore.updateEmojiCategory(category)
        }
    }

    fun updatePreciseMatching(enabled: Boolean) {
        viewModelScope.launch {
            settingsDataStore.updatePreciseMatching(enabled)
        }
    }

    fun updateTransparentBackground(enabled: Boolean) {
        viewModelScope.launch {
            settingsDataStore.updateTransparentBackground(enabled)
        }
    }

    fun updateThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            settingsDataStore.updateThemeMode(mode)
        }
    }

    fun clearSaveMessage() {
        _uiState.update { state ->
            state.copy(saveMessage = "")
        }
    }

    fun clearProcessingState() {
        _uiState.update { state ->
            state.copy(processingState = ProcessingState.Idle)
        }
    }
}
