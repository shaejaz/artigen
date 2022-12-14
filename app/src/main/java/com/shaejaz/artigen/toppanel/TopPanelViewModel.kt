package com.shaejaz.artigen.toppanel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaejaz.artigen.data.Config
import com.shaejaz.artigen.data.Pattern
import com.shaejaz.artigen.data.repositories.ConfigRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopPanelViewModel @Inject constructor(
    private val configRepository: ConfigRepository
) : ViewModel() {
    val enablePatternSelectMenu = MutableStateFlow(true)

    fun setSelectedPattern(pattern: Pattern) {
        viewModelScope.launch {
            configRepository.setSelectedPattern(pattern)
        }
    }

    fun setConfig(config: Config) {
        viewModelScope.launch {
            configRepository.setConfig(config)
        }
    }

    fun getSelectedPattern(): StateFlow<Pattern?> {
        return configRepository.observeSelectedPattern()
    }

    fun getAvailablePatterns(): StateFlow<List<String>> {
        return configRepository.observeAvailablePatterns()
    }
}