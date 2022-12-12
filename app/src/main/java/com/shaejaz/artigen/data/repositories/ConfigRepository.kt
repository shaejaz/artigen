package com.shaejaz.artigen.data.repositories

import com.shaejaz.artigen.data.Config
import com.shaejaz.artigen.data.Pattern
import kotlinx.coroutines.flow.StateFlow

interface ConfigRepository {
    fun setDeviceXY(x: Int, y: Int)

    fun observeAvailablePatterns(): StateFlow<List<String>>

    suspend fun setAvailablePatterns(patterns: List<String>)

    fun observeConfig(): StateFlow<Config?>

    suspend fun setConfig(config: Config)

    fun observeSelectedPattern(): StateFlow<Pattern?>

    suspend fun setSelectedPattern(pattern: Pattern)
}