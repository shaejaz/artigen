package com.shaejaz.artigen.patternconfig

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.shaejaz.artigen.R

class PatternConfigFragment : Fragment() {
    private val viewModel: PatternConfigViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val pattern = viewModel.getSelectedPattern()

        if (pattern == "Julia") {
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                add<PaintedConfigOptions>(R.id.fragment_container)
            }
        }

        return inflater.inflate(R.layout.fragment_pattern_config, container, false)
    }
}