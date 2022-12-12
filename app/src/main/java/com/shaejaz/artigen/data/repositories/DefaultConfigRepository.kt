package com.shaejaz.artigen.data.repositories

import com.shaejaz.artigen.data.Config
import com.shaejaz.artigen.data.Pattern
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DefaultConfigRepository : ConfigRepository {
    private val _availablePatterns = MutableStateFlow(listOf(
        "Blocks",
        "Painted"
    ))
    private val _config = MutableStateFlow<Config?>(null)
    private val _selectPattern = MutableStateFlow<Pattern?>(Pattern.Blocks)

    override fun observeAvailablePatterns(): StateFlow<List<String>> {
        return _availablePatterns.asStateFlow()
    }

    override suspend fun setAvailablePatterns(patterns: List<String>) {
        _availablePatterns.emit(patterns)
    }

    override fun observeConfig(): StateFlow<Config?> {
        return _config.asStateFlow()
    }

    override suspend fun setConfig(config: Config) {
        _config.emit(config)
    }

    override fun observeSelectedPattern(): StateFlow<Pattern?> {
        return _selectPattern.asStateFlow()
    }

    override suspend fun setSelectedPattern(pattern: Pattern) {
        _selectPattern.emit(pattern)
    }
}