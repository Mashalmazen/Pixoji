package com.mashalmazen.pixoji.presentation.viewmodel

import androidx.lifecycle.ViewModelProvider
import com.mashalmazen.pixoji.data.datastore.SettingsDataStore
import com.mashalmazen.pixoji.domain.usecase.ProcessImageUseCase
import com.mashalmazen.pixoji.domain.usecase.SaveImageUseCase

class PixojiViewModelFactory(
    private val settingsDataStore: SettingsDataStore,
    private val processImageUseCase: ProcessImageUseCase,
    private val saveImageUseCase: SaveImageUseCase
) : ViewModelProvider.Factory {

    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(PixojiViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            PixojiViewModel(
                settingsDataStore = settingsDataStore,
                processImageUseCase = processImageUseCase,
                saveImageUseCase = saveImageUseCase
            ) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
