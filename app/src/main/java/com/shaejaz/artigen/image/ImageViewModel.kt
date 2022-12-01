package com.shaejaz.artigen.image

import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class ImageViewModel: ViewModel() {
    companion object {
        init {
            System.loadLibrary("artigen_android")
        }
    }

    private val decoder = Base64.getDecoder()

    private val _imageBase64String = MutableStateFlow<String?>(null)
    val imageBase64String: StateFlow<String?> = _imageBase64String

    val image = _imageBase64String.map { it ->
        if (it == null) {
            return@map null
        }

        val bytes = decoder.decode(it)
        return@map BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    private val _imageGenerating = MutableStateFlow(false)
    val imageGenerating: StateFlow<Boolean> = _imageGenerating

    private external fun generateImageJNI(): String
    private suspend fun generateImageJNIAsync(): String {
        return withContext(Dispatchers.Default) {
            return@withContext generateImageJNI()
        }
    }

    fun generateImage() {
        viewModelScope.launch {
            _imageGenerating.emit(true)
            val imageCode = generateImageJNIAsync()
            _imageGenerating.emit(false)
            _imageBase64String.emit(imageCode)
        }
    }
}