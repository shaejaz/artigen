package com.shaejaz.artigen.data.repositories

import com.shaejaz.artigen.data.Config
import com.shaejaz.artigen.data.Pattern
import kotlinx.coroutines.flow.Flow


interface ConfigRepository {
    fun observeAvailablePatterns(): Flow<List<Pattern>>

    suspend fun setAvailablePatterns(patterns: List<Pattern>)

    fun observeConfig(): Flow<Config>

    suspend fun setConfig(config: Config)

    fun observeSelectedPattern(): Flow<Pattern>

    suspend fun setSelectedPattern(pattern: Pattern)
}