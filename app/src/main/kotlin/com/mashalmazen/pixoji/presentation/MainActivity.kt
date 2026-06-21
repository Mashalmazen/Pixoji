package com.mashalmazen.pixoji.presentation

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelProvider
import com.mashalmazen.pixoji.data.datastore.SettingsDataStore
import com.mashalmazen.pixoji.data.image.MediaStoreHelper
import com.mashalmazen.pixoji.data.repository.EmojiDataRepository
import com.mashalmazen.pixoji.data.usecase.ProcessImageUseCaseImpl
import com.mashalmazen.pixoji.data.usecase.SaveImageUseCaseImpl
import com.mashalmazen.pixoji.presentation.screen.MainScreen
import com.mashalmazen.pixoji.presentation.screen.SettingsBottomSheet
import com.mashalmazen.pixoji.presentation.theme.PixojiTheme
import com.mashalmazen.pixoji.presentation.viewmodel.PixojiViewModel
import com.mashalmazen.pixoji.presentation.viewmodel.PixojiViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val settingsDataStore = SettingsDataStore(this)
        val emojiRepository = EmojiDataRepository(this)
        val mediaStoreHelper = MediaStoreHelper(this)
        val processImageUseCase = ProcessImageUseCaseImpl(emojiRepository)
        val saveImageUseCase = SaveImageUseCaseImpl(mediaStoreHelper)

        val viewModelFactory = PixojiViewModelFactory(
            settingsDataStore = settingsDataStore,
            processImageUseCase = processImageUseCase,
            saveImageUseCase = saveImageUseCase
        )

        val viewModel = ViewModelProvider(this, viewModelFactory).get(PixojiViewModel::class.java)

        setContent {
            PixojiTheme(darkTheme = true) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF000000)
                ) {
                    var showSettings by remember { mutableStateOf(false) }

                    MainScreen(
                        viewModel = viewModel,
                        onOpenSettings = { showSettings = true }
                    )

                    SettingsBottomSheet(
                        viewModel = viewModel,
                        onDismiss = { showSettings = false },
                        isVisible = showSettings
                    )
                }
            }
        }
    }
}
