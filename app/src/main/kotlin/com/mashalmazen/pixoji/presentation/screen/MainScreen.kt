package com.mashalmazen.pixoji.presentation.screen

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import coil.compose.AsyncImage
import com.mashalmazen.pixoji.domain.model.ProcessingState
import com.mashalmazen.pixoji.presentation.viewmodel.PixojiViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: PixojiViewModel,
    onOpenSettings: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showImageComparison by remember { mutableStateOf(false) }
    var comparisonOffset by remember { mutableStateOf(0.5f) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            try {
                // Load bitmap from URI
                // This requires Android context, should be done in ViewModel
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    LaunchedEffect(uiState.saveMessage) {
        if (uiState.saveMessage.isNotEmpty()) {
            snackbarHostState.showSnackbar(
                message = uiState.saveMessage,
                duration = SnackbarDuration.Short
            )
            viewModel.clearSaveMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pixoji") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1F1F1F),
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = onOpenSettings) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = Color(0xFF000000)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF000000))
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            when (val state = uiState.processingState) {
                is ProcessingState.Idle, is ProcessingState.NoImageSelected -> {
                    IdleStateContent(
                        selectedImage = uiState.selectedImageBitmap,
                        onSelectImage = { imagePickerLauncher.launch("image/*") },
                        onProcess = { viewModel.startProcessing() },
                        isImageSelected = uiState.selectedImageBitmap != null
                    )
                }

                is ProcessingState.Processing -> {
                    ProcessingStateContent(
                        progress = state.progress,
                        onCancel = { viewModel.cancelProcessing() }
                    )
                }

                is ProcessingState.Success -> {
                    SuccessStateContent(
                        originalBitmap = state.originalBitmap,
                        processedBitmap = state.outputBitmap,
                        onSave = { viewModel.saveProcessedImage() },
                        isSaving = uiState.isSaving,
                        onProcessAnother = {
                            viewModel.clearProcessingState()
                        }
                    )
                }

                is ProcessingState.Error -> {
                    ErrorStateContent(
                        message = state.message,
                        onRetry = { viewModel.clearProcessingState() }
                    )
                }
            }
        }
    }
}

@Composable
fun IdleStateContent(
    selectedImage: Bitmap?,
    onSelectImage: () -> Unit,
    onProcess: () -> Unit,
    isImageSelected: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF000000))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(Color(0xFF1F1F1F))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (selectedImage != null) {
                AsyncImage(
                    model = selectedImage,
                    contentDescription = "Selected Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            } else {
                Text(
                    text = "No image selected\nTap button to select an image",
                    color = Color(0xFF888888),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Button(
            onClick = onSelectImage,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Select Image")
        }

        Button(
            onClick = onProcess,
            enabled = isImageSelected,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Generate Emoji Mosaic")
        }

        Text(
            text = "Configure settings before processing",
            color = Color(0xFF888888),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
fun ProcessingStateContent(
    progress: Float,
    onCancel: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF000000))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        CircularProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .padding(vertical = 16.dp),
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Processing ${(progress * 100).toInt()}%",
            color = Color.White,
            style = MaterialTheme.typography.headlineSmall
        )

        Button(
            onClick = onCancel,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text("Cancel")
        }
    }
}

@Composable
fun SuccessStateContent(
    originalBitmap: Bitmap,
    processedBitmap: Bitmap,
    onSave: () -> Unit,
    isSaving: Boolean,
    onProcessAnother: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF000000))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Emoji Mosaic Generated!",
            color = Color.White,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(Color(0xFF1F1F1F))
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = processedBitmap,
                contentDescription = "Processed Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }

        Button(
            onClick = onSave,
            enabled = !isSaving,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            if (isSaving) {
                CircularProgressIndicator(
                    modifier = Modifier.height(20.dp),
                    color = Color.White
                )
            } else {
                Text("Save to Gallery")
            }
        }

        Button(
            onClick = onProcessAnother,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Process Another Image")
        }
    }
}

@Composable
fun ErrorStateContent(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF000000))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        Text(
            text = "Error",
            color = Color(0xFFCF6679),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Text(
            text = message,
            color = Color(0xFF888888),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Button(
            onClick = onRetry,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Try Again")
        }
    }
}
