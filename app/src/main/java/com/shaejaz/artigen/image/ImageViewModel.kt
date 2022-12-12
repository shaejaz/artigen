package com.shaejaz.artigen.image

import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.shaejaz.artigen.data.Config
import com.shaejaz.artigen.data.Pattern
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

    private external fun generateImageJNI(pattern: String, config: String): String
    private suspend fun generateImageJNIAsync(pattern: String, config: String): String {
        return withContext(Dispatchers.Default) {
            return@withContext generateImageJNI(pattern, config)
        }
    }

    fun generateImage() {
        val gson = Gson()
        val config = gson.toJson(configRepository.observeConfig().value)

        val pattern = when (configRepository.observeSelectedPattern().value) {
            Pattern.Blocks -> "Blocks"
            Pattern.Painted -> "Painted"
            else -> ""
        }

        viewModelScope.launch {
            _imageGenerating.emit(true)
            val imageCode = generateImageJNIAsync(pattern, config)
            _imageGenerating.emit(false)
            _imageBase64String.emit(imageCode)
        }
    }

    fun setDeviceXY(x: Int, y: Int) {
        configRepository.setDeviceXY(x, y)
    }

    fun setConfig(config: Config) {
        viewModelScope.launch {
            configRepository.setConfig(config)
        }
    }

    fun observeConfig(): Flow<Config?> {
        return configRepository.observeConfig()
    }

    fun observeSelectedPattern(): StateFlow<Pattern?> {
        return configRepository.observeSelectedPattern()
    }
}