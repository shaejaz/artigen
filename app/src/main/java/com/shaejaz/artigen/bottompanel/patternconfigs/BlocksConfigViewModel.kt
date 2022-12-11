package com.shaejaz.artigen.bottompanel.patternconfigs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaejaz.artigen.data.repositories.ConfigRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlocksConfigViewModel @Inject constructor(
    private val configRepository: ConfigRepository
) : ViewModel() {
    val color1 = MutableStateFlow("#5e062b")
    val color2 = MutableStateFlow("#171585")
    val color3 = MutableStateFlow("#b8cf38")
    val bgColor = MutableStateFlow("#ffffff")
    val blockSize = MutableStateFlow("2")
    val lineSize = MutableStateFlow("2")
    val density = MutableStateFlow("1.0")

    fun saveConfig() {
        if (blockSize.value != "" && lineSize.value != "" && density.value != "") {
            val config = com.shaejaz.artigen.data.BlocksConfig(
                x = 0, y = 0,
                color1 = color1.value,
                color2 = color2.value,
                color3 = color3.value,
                bgColor = bgColor.value,
                blockSize = blockSize.value.toInt(),
                lineSize = lineSize.value.toInt(),
                density = density.value.toFloat(),
            )

            viewModelScope.launch {
                configRepository.setConfig(config)
            }
        }
    }
}
