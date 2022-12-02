package com.shaejaz.artigen.patternconfig

import androidx.lifecycle.ViewModel

class PatternConfigViewModel : ViewModel() {
    fun getSelectedPattern(): String {
        return "Julia"
    }
}