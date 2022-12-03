package com.shaejaz.artigen.patternconfig

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.shaejaz.artigen.R
import com.shaejaz.artigen.data.Pattern
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PatternConfigFragment : Fragment() {
    private val viewModel by viewModels<PatternConfigViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getSelectedPattern().collect{
                    when (it) {
                        Pattern.Painted -> {
                            parentFragmentManager.commit {
                                setReorderingAllowed(true)
                                add<PaintedConfigOptions>(R.id.fragment_container)
                            }
                        }
                    }
                }
            }
        }

        return inflater.inflate(R.layout.fragment_pattern_config, container, false)
    }
}