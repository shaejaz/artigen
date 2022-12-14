package com.shaejaz.artigen.bottompanel.patternconfigs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.shaejaz.artigen.R
import com.shaejaz.artigen.bottompanel.BottomPanelViewModel
import com.shaejaz.artigen.data.Pattern
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PatternConfigContainer : Fragment() {
    private val viewModel by activityViewModels<BottomPanelViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.frag_pattern_config_container, container, false)

        var frag: Fragment? = when (viewModel.observeSelectedPattern().value) {
            Pattern.Painted -> {
                PaintedConfig()
            }
            Pattern.Blocks -> {
                BlocksConfig()
            }
            else -> {
                null
            }
        }

        if (frag != null) {
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.fragment_container, frag)
            }
        }

        val cancelButton = view.findViewById<FloatingActionButton>(R.id.cancel_button)
        cancelButton.setOnClickListener {
            viewModel.cancelEditConfigButtonClick()
        }

        val applyButton = view.findViewById<FloatingActionButton>(R.id.apply_button)
        applyButton.setOnClickListener {
            viewModel.applyEditConfigButtonClick()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.enableConfirmConfigButton.collect {
                    applyButton.isEnabled = it
                }
            }
        }

        return view
    }
}