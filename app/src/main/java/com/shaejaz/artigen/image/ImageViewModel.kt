package com.shaejaz.artigen.image

import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaejaz.artigen.data.Config
import com.shaejaz.artigen.data.repositories.ConfigRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ImageViewModel @Inject constructor(
    private val configRepository: ConfigRepository
) : ViewModel() {
//    companion object {
//        init {
//            System.loadLibrary("artigen_android")
//        }
//    }

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

    fun observeConfig(): Flow<Config?> {
        return configRepository.observeConfig()
    }
}