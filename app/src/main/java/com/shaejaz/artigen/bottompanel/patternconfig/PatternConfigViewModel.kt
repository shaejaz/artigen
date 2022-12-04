package com.shaejaz.artigen.bottompanel.patternconfig

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.shaejaz.artigen.data.Config
import com.shaejaz.artigen.data.Pattern
import com.shaejaz.artigen.data.repositories.ConfigRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PatternConfigViewModel @Inject constructor(
    private val repository: ConfigRepository
) : ViewModel() {
    private val _openConfigFragment = MutableStateFlow<Fragment?>(null)
    private val _cancelConfigChanges = MutableStateFlow<Unit>(Unit)

    fun getSelectedPattern(): Flow<Pattern?> {
        return repository.observeSelectedPattern()
    }

    suspend fun setConfig(config: Config) {
        repository.setConfig(config)
    }

    fun getConfig(): Flow<Config?> {
        return repository.observeConfig()
    }

    suspend fun newConfigFragmentOpened(fragment: Fragment) {
        _openConfigFragment.emit(fragment)
    }

    fun observeOpenConfigFragment(): StateFlow<Fragment?> {
        return _openConfigFragment
    }

    suspend fun cancelConfigChanges() {
        _cancelConfigChanges.emit(Unit)
    }

    fun observeCancelConfigChanges(): StateFlow<Unit> {
        return _cancelConfigChanges
    }
}