package com.shaejaz.artigen.bottompanel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.shaejaz.artigen.R
import com.shaejaz.artigen.bottompanel.patternconfigs.PatternConfigContainer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BottomPanel : Fragment() {
    private val viewModel by activityViewModels<BottomPanelViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.frag_bottom_panel, container, false)

        parentFragmentManager.commit {
            setReorderingAllowed(true)
            add<EditPattern>(R.id.nested_bottom_container)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.editConfigButtonClick.collect {
                        val frag = PatternConfigContainer()
                        parentFragmentManager.commit {
                            setReorderingAllowed(true)
                            replace(R.id.nested_bottom_container, frag)
                        }
                    }
                }

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