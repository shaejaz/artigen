package com.shaejaz.artigen.patternconfig

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaejaz.artigen.data.Config
import com.shaejaz.artigen.data.repositories.DefaultConfigRepository
import kotlinx.coroutines.launch

class OptionsViewModel(
    private val configRepository: DefaultConfigRepository
): ViewModel() {
    fun setOptions(options: Config) {
        viewModelScope.launch {
            configRepository.setConfig(options)
        }
    }
}