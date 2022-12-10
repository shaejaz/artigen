package com.shaejaz.artigen.bottompanel.patternconfigs

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.shaejaz.artigen.R
import com.shaejaz.artigen.bottompanel.BottomPanelViewModel
import com.shaejaz.artigen.utils.ColorPicker
import com.shaejaz.artigen.utils.NumberFilter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import com.shaejaz.artigen.data.BlocksConfig as BlockConfigData


@AndroidEntryPoint
class BlocksConfig : Fragment() {
    private val viewModel by viewModels<BottomPanelViewModel>()
    private var config: BlockConfigData = BlockConfigData(0, 0, "", "", "", "", 2, 2, 1.0f)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.frag_blocks_config, container, false)

        val blockSizeField = view.findViewById<EditText>(R.id.block_size_field)
        blockSizeField.filters = arrayOf(NumberFilter(1, 5))
        val lineSizeField = view.findViewById<EditText>(R.id.line_size_field)
        lineSizeField.filters = arrayOf(NumberFilter(1, 5))
        val densityField = view.findViewById<EditText>(R.id.density_field)
        densityField.filters = arrayOf(NumberFilter(0.0f, 5.0f))

        val color1Picker = view.findViewById<ColorPicker>(R.id.color1_picker)
        color1Picker.setSelectedColorChangedListener {
            viewLifecycleOwner.lifecycleScope.launch {
                config = config.copy().apply { color1 = it }
                viewModel.setConfig(config)
            }
        }

        val color2Picker = view.findViewById<ColorPicker>(R.id.color2_picker)
        color2Picker.setSelectedColorChangedListener {
            viewLifecycleOwner.lifecycleScope.launch {
                config = config.copy().apply { color2 = it }
                viewModel.setConfig(config)
            }
        }

        val color3Picker = view.findViewById<ColorPicker>(R.id.color3_picker)
        color3Picker.setSelectedColorChangedListener {
            viewLifecycleOwner.lifecycleScope.launch {
                config = config.copy().apply { color3 = it }
                viewModel.setConfig(config)
            }
        }

        val bgColorPicker = view.findViewById<ColorPicker>(R.id.bg_color_picker)
        bgColorPicker.setSelectedColorChangedListener {
            viewLifecycleOwner.lifecycleScope.launch {
                config = config.copy().apply { bgColor = it }
                viewModel.setConfig(config)
            }
        }

        blockSizeField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                viewLifecycleOwner.lifecycleScope.launch {
                    if (s.toString() != "") {
                        config = config.copy().apply { blockSize = s.toString().toInt() }
                        viewModel.setConfig(config)
                    } else {
                        config = config.copy().apply { blockSize = 1 }
                        viewModel.setConfig(config)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        lineSizeField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                viewLifecycleOwner.lifecycleScope.launch {
                    if (s.toString() != "") {
                        config = config.copy().apply { lineSize = s.toString().toInt() }
                        viewModel.setConfig(config)
                    } else {
                        config = config.copy().apply { lineSize = 1 }
                        viewModel.setConfig(config)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        densityField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                viewLifecycleOwner.lifecycleScope.launch {
                    if (s.toString() != "") {
                        config = config.copy().apply { density = s.toString().toFloat() }
                        viewModel.setConfig(config)
                    } else {
                        config = config.copy().apply { density = 1.0f }
                        viewModel.setConfig(config)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        return view
    }
}