package com.shaejaz.artigen.bottompanel

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.transition.Visibility
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaejaz.artigen.data.Config
import com.shaejaz.artigen.data.Pattern
import com.shaejaz.artigen.data.repositories.ConfigRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class BottomPanelViewModel @Inject constructor(
    private val configRepository: ConfigRepository
) : ViewModel() {
    private val _editConfigButtonClick = MutableSharedFlow<Unit>()
    val editConfigButtonClick = _editConfigButtonClick.asSharedFlow()
    private val _cancelEditConfigButtonClick = MutableSharedFlow<Unit>()
    val cancelEditConfigButtonClick = _cancelEditConfigButtonClick.asSharedFlow()
    private val _applyEditConfigButtonClick = MutableSharedFlow<Unit>()
    val applyEditConfigButtonClick = _applyEditConfigButtonClick.asSharedFlow()
    private val _shareButtonClick = MutableSharedFlow<Unit>()
    val shareButtonClick = _shareButtonClick.asSharedFlow()

    val showApplyWallpaperButton = MutableStateFlow(false)
    val enableConfirmConfigButton = MutableStateFlow(true)

    fun editConfigButtonClick() {
        viewModelScope.launch {
            _editConfigButtonClick.emit(Unit)
        }
    }

    fun cancelEditConfigButtonClick() {
        viewModelScope.launch {
            _cancelEditConfigButtonClick.emit(Unit)
        }
    }

    fun applyEditConfigButtonClick() {
        viewModelScope.launch {
            _applyEditConfigButtonClick.emit(Unit)
        }
    }

    fun shareButtonClick() {
        viewModelScope.launch {
            _shareButtonClick.emit(Unit)
        }
    }

    fun observeSelectedPattern(): StateFlow<Pattern?> {
        return configRepository.observeSelectedPattern()
    }

    fun observeConfig(): StateFlow<Config?> {
        return configRepository.observeConfig()
    }

    suspend fun setConfig(config: Config) {
        configRepository.setConfig(config)
    }

    @Throws(IOException::class)
    fun saveBitmap(
        context: Context, bitmap: Bitmap, format: Bitmap.CompressFormat,
        mimeType: String, displayName: String
    ): Uri {
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
        }

        val resolver = context.contentResolver
        var uri: Uri? = null

        try {
            uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                ?: throw IOException("Failed to create new MediaStore record.")

            resolver.openOutputStream(uri)?.use {
                if (!bitmap.compress(format, 95, it))
                    throw IOException("Failed to save bitmap.")
            } ?: throw IOException("Failed to open output stream.")

            return uri

        } catch (e: IOException) {
            uri?.let { orphanUri ->
                resolver.delete(orphanUri, null, null)
            }

            throw e
        }
    }
}