package com.mashalmazen.pixoji.data.usecase

import android.graphics.Bitmap
import com.mashalmazen.pixoji.data.image.MediaStoreHelper
import com.mashalmazen.pixoji.domain.usecase.SaveImageUseCase

class SaveImageUseCaseImpl(
    private val mediaStoreHelper: MediaStoreHelper
) : SaveImageUseCase {

    override suspend fun saveImage(bitmap: Bitmap, filename: String): Result<String> {
        return mediaStoreHelper.saveBitmapToFile(bitmap, filename)
    }

    override suspend fun saveImageToGallery(bitmap: Bitmap): Result<String> {
        return mediaStoreHelper.saveToMediaStore(bitmap)
    }
}
