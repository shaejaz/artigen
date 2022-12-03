package com.shaejaz.artigen.patternconfig

import androidx.lifecycle.ViewModel
import com.shaejaz.artigen.data.Config
import com.shaejaz.artigen.data.Pattern
import com.shaejaz.artigen.data.repositories.ConfigRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PatternConfigViewModel @Inject constructor(
    private val repository: ConfigRepository
) : ViewModel() {
    fun getSelectedPattern(): Flow<Pattern> {
        return repository.observeSelectedPattern()
    }

    suspend fun setConfig(config: Config) {
        repository.setConfig(config)
    }

    fun getConfig(): Flow<Config> {
        return repository.observeConfig()
    }
}