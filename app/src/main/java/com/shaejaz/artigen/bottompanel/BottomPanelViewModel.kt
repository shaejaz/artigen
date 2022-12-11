package com.shaejaz.artigen.bottompanel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaejaz.artigen.data.Config
import com.shaejaz.artigen.data.Pattern
import com.shaejaz.artigen.data.repositories.ConfigRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
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

    fun observeSelectedPattern(): StateFlow<Pattern?> {
        return configRepository.observeSelectedPattern()
    }

    fun observeConfig(): StateFlow<Config?> {
        return configRepository.observeConfig()
    }

    suspend fun setConfig(config: Config) {
        configRepository.setConfig(config)
    }
}