package com.mashalmazen.pixoji.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mashalmazen.pixoji.domain.model.EmojiCategory
import com.mashalmazen.pixoji.domain.model.ResolutionMultiplier
import com.mashalmazen.pixoji.domain.model.ThemeMode
import com.mashalmazen.pixoji.presentation.viewmodel.PixojiViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsBottomSheet(
    viewModel: PixojiViewModel,
    onDismiss: () -> Unit,
    isVisible: Boolean
) {
    if (isVisible) {
        val uiState by viewModel.uiState.collectAsState()
        val settings = uiState.currentSettings

        ModalBottomSheet(
            onDismissRequest = onDismiss,
            containerColor = Color(0xFF1F1F1F),
            contentColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1F1F1F))
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Settings",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                SettingSection(
                    title = "Grid Density",
                    description = "${settings.gridDensity} blocks"
                ) {
                    Slider(
                        value = settings.gridDensity.toFloat(),
                        onValueChange = { viewModel.updateGridDensity(it.toInt()) },
                        valueRange = 80f..500f,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                SettingSection(
                    title = "Output Resolution",
                    description = ResolutionMultiplier.values()
                        .find { it.value == settings.resolutionMultiplier }?.displayName
                        ?: "1x (Fast)"
                ) {
                    ResolutionMultiplier.values().forEach { multiplier ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = settings.resolutionMultiplier == multiplier.value,
                                onClick = { viewModel.updateResolutionMultiplier(multiplier.value) }
                            )
                            Text(
                                text = multiplier.displayName,
                                color = Color.White,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                SettingSection(
                    title = "Emoji Category",
                    description = settings.emojiCategory.displayName
                ) {
                    EmojiCategory.values().forEach { category ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = settings.emojiCategory == category,
                                onClick = { viewModel.updateEmojiCategory(category) }
                            )
                            Text(
                                text = category.displayName,
                                color = Color.White,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                SettingSection(
                    title = "Matching Algorithm",
                    description = if (settings.usePreciseMatching) "Precise (LAB)" else "Fast (RGB)"
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(
                            checked = settings.usePreciseMatching,
                            onCheckedChange = { viewModel.updatePreciseMatching(it) }
                        )
                        Text(
                            text = "Use LAB Color Space",
                            color = Color.White,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                SettingSection(
                    title = "Transparency",
                    description = if (settings.makeTransparent) "Enabled" else "Disabled"
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(
                            checked = settings.makeTransparent,
                            onCheckedChange = { viewModel.updateTransparentBackground(it) }
                        )
                        Text(
                            text = "Transparent Background",
                            color = Color.White,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                SettingSection(
                    title = "Theme",
                    description = settings.themeMode.name
                ) {
                    ThemeMode.values().forEach { mode ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = settings.themeMode == mode,
                                onClick = { viewModel.updateThemeMode(mode) }
                            )
                            Text(
                                text = mode.name,
                                color = Color.White,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text("Close")
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun SettingSection(
    title: String,
    description: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            color = Color.White,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = description,
            color = Color(0xFF888888),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp)
        )
        content()
    }
}
