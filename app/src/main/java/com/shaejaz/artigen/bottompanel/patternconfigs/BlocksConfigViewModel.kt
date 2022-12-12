package com.shaejaz.artigen.bottompanel.patternconfigs

import android.util.DisplayMetrics
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
    val color1 = MutableStateFlow("5e062b")
    val color2 = MutableStateFlow("171585")
    val color3 = MutableStateFlow("b8cf38")
    val bgColor = MutableStateFlow("ffffff")
    val blockSize = MutableStateFlow("2")
    val lineSize = MutableStateFlow("2")
    val density = MutableStateFlow("1.0")

    fun saveConfig() {
        if (blockSize.value != "" && lineSize.value != "" && density.value != "") {
            val display = DisplayMetrics()

            val cleanedColor1 = cleanColor(color1.value)
            val cleanedColor2 = cleanColor(color2.value)
            val cleanedColor3 = cleanColor(color3.value)
            val cleanedBgColor = cleanColor(bgColor.value)

            val config = com.shaejaz.artigen.data.BlocksConfig(
                x = display.widthPixels, y = display.heightPixels,
                color1 = cleanedColor1,
                color2 = cleanedColor2,
                color3 = cleanedColor3,
                bgColor = cleanedBgColor,
                blockSize = blockSize.value.toInt(),
                lineSize = lineSize.value.toInt(),
                density = density.value.toFloat(),
            )

            viewModelScope.launch {
                configRepository.setConfig(config)
            }
        }
    }

    fun cleanColor(color: String): String {
        var newColor = ""
        if (color.length > 6) {
            newColor = color.removePrefix("#")
            if (newColor.length > 6) {
                newColor = newColor.removePrefix("FF")
            }

            return newColor
        } else {
            return color
        }
    }
}
