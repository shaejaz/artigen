package com.shaejaz.artigen.bottompanel

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
import com.shaejaz.artigen.R
import com.shaejaz.artigen.bottompanel.patternconfig.PatternConfigFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BottomPanel : Fragment() {
    private val viewModel by activityViewModels<BottomPanelViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bottom_panel, container, false)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.editConfigButtonClick.collect {
                        val frag = PatternConfigFragment()
                        parentFragmentManager.commit {
                            setReorderingAllowed(true)
                            replace(R.id.nested_bottom_container, frag)
                        }
                    }
                }
//
                launch {
                    viewModel.cancelEditConfigButtonClick.collect {
                        val frag = EditPattern()
                        parentFragmentManager.commit {
                            setReorderingAllowed(true)
                            replace(R.id.nested_bottom_container, frag)
                        }
                    }
                }
            }
        }

        return view
    }
}