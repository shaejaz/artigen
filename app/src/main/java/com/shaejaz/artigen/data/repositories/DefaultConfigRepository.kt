package com.shaejaz.artigen.data.repositories

import com.shaejaz.artigen.data.Config
import com.shaejaz.artigen.data.Pattern
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class DefaultConfigRepository: ConfigRepository {
    private val _availablePatterns = MutableSharedFlow<List<Pattern>>()
    private val _config = MutableSharedFlow<Config>()
    private val _selectPattern = MutableSharedFlow<Pattern>()

    override fun observeAvailablePatterns(): Flow<List<Pattern>> {
        return _availablePatterns
    }

    override suspend fun setAvailablePatterns(patterns: List<Pattern>) {
        _availablePatterns.emit(patterns)
    }

    override fun observeConfig(): Flow<Config> {
        return _config
    }

    override suspend fun setConfig(config: Config) {
        _config.emit(config)
    }

    override fun observeSelectedPattern(): Flow<Pattern> {
        return _selectPattern
    }

    override suspend fun setSelectedPattern(pattern: Pattern) {
        _selectPattern.emit(pattern)
    }
}