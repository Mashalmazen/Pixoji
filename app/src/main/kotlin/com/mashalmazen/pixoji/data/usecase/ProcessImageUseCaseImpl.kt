package com.mashalmazen.pixoji.data.usecase

import android.graphics.Bitmap
import com.mashalmazen.pixoji.data.image.ImageProcessor
import com.mashalmazen.pixoji.domain.model.ProcessingSettings
import com.mashalmazen.pixoji.domain.model.ProcessingState
import com.mashalmazen.pixoji.domain.repository.EmojiRepository
import com.mashalmazen.pixoji.domain.usecase.ProcessImageUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ProcessImageUseCaseImpl(
    private val emojiRepository: EmojiRepository
) : ProcessImageUseCase {

    private var processingJob: Job? = null
    private val imageProcessor = ImageProcessor()

    override fun executeProcessing(
        inputBitmap: Bitmap,
        settings: ProcessingSettings
    ): Flow<ProcessingState> = flow {
        try {
            emit(ProcessingState.Processing(0f))

            val emojis = if (settings.emojiCategory.name == "ALL") {
                emojiRepository.getAllEmojis()
            } else {
                emojiRepository.getEmojisByCategory(settings.emojiCategory)
            }

            if (emojis.isEmpty()) {
                emit(ProcessingState.Error("No emojis available for the selected category"))
                return@flow
            }

            val outputBitmap = imageProcessor.processImage(
                inputBitmap = inputBitmap,
                emojis = emojis,
                settings = settings,
                onProgress = { progress ->
                    // Progress callback can be used to update UI if needed
                }
            )

            emit(ProcessingState.Success(outputBitmap, inputBitmap))
        } catch (e: Exception) {
            emit(ProcessingState.Error(
                message = e.message ?: "Unknown error during processing",
                throwable = e
            ))
        }
    }.flowOn(Dispatchers.Default)

    override fun cancel() {
        processingJob?.cancel()
    }
}
