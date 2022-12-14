package com.shaejaz.artigen.bottompanel.patternconfigs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.shaejaz.artigen.R
import com.shaejaz.artigen.bottompanel.BottomPanelViewModel
import com.shaejaz.artigen.databinding.FragJuliaConfigBinding
import com.shaejaz.artigen.utils.NumberFilter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch

@AndroidEntryPoint
class JuliaConfigFrag : Fragment() {
    private lateinit var binding: FragJuliaConfigBinding
    private val viewModel by activityViewModels<JuliaConfigViewModel>()
    private val bottomPanelViewModel by activityViewModels<BottomPanelViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.frag_julia_config, container, false)
        binding = FragJuliaConfigBinding.bind(view).apply {
            this.viewmodel = viewModel
        }
        binding.lifecycleOwner = this.viewLifecycleOwner

        binding.minField.filters = arrayOf(NumberFilter(2, 7))
        binding.minField.filters = arrayOf(NumberFilter(2, 7))

        binding.color1Picker.selectedColor = viewModel.color1.value
        binding.color2Picker.selectedColor = viewModel.color2.value
        binding.color3Picker.selectedColor = viewModel.color3.value
        binding.bgColorPicker.selectedColor = viewModel.bgColor.value

        binding.color1Picker.setSelectedColorChangedListener {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.color1.emit(it)
            }
        }
        binding.color2Picker.setSelectedColorChangedListener {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.color2.emit(it)
            }
        }
        binding.color3Picker.setSelectedColorChangedListener {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.color3.emit(it)
            }
        }
        binding.bgColorPicker.setSelectedColorChangedListener {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.bgColor.emit(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                merge(
                    viewModel.min,
                    viewModel.max,
                ).collect {
                    if (viewModel.min.value == "" || viewModel.max.value == "") {
                        bottomPanelViewModel.enableConfirmConfigButton.emit(false)
                    } else {
                        bottomPanelViewModel.enableConfirmConfigButton.emit(true)
                    }
                }
            }
        }

        return view
    }
}