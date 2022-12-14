package com.shaejaz.artigen.toppanel

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.textfield.TextInputLayout
import com.shaejaz.artigen.R
import com.shaejaz.artigen.data.BlocksConfig
import com.shaejaz.artigen.data.JuliaConfig
import com.shaejaz.artigen.data.Pattern
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class TopPanel : Fragment() {
    private val viewModel by activityViewModels<TopPanelViewModel>()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.frag_top_panel, container, false)

        val patterns = viewModel.getAvailablePatterns().value
        val arrayAdaptor = activity?.let { ArrayAdapter(it, R.layout.dropdown_item, patterns) }

        val autoCompleteTextView = view.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
        autoCompleteTextView.setAdapter(arrayAdaptor)
        autoCompleteTextView.setOnTouchListener { _, _ ->
            autoCompleteTextView.showDropDown()
            true
        }

        autoCompleteTextView.setText(when (viewModel.getSelectedPattern().value) {
            Pattern.Blocks -> "Blocks"
            Pattern.Julia -> "Julia"
            else -> "Blocks"
        }, false)

        autoCompleteTextView.onItemClickListener =
            OnItemClickListener { _, _, position, _ ->
                val selectedItem = autoCompleteTextView.adapter.getItem(position).toString()

                viewModel.setSelectedPattern(when (selectedItem) {
                    "Blocks" -> Pattern.Blocks
                    "Julia" -> Pattern.Julia
                    else -> Pattern.Blocks
                })

                viewModel.setConfig(when (selectedItem) {
                    "Blocks" -> BlocksConfig(
                        x = 0,
                        y = 0,
                        color1 = "5e062b",
                        color2 = "171585",
                        color3 = "b8cf38",
                        bgColor = "ffffff",
                        blockSize = 2,
                        lineSize = 2,
                        density = 1.0f,
                    )
                    "Julia" -> JuliaConfig(
                        x = 0,
                        y = 0,
                        color1 = "5e062b",
                        color2 = "171585",
                        color3 = "b8cf38",
                        bgColor = "ffffff",
                        min = 3,
                        max = 6,
                    )
                    else -> BlocksConfig(
                        x = 0,
                        y = 0,
                        color1 = "5e062b",
                        color2 = "171585",
                        color3 = "b8cf38",
                        bgColor = "ffffff",
                        blockSize = 2,
                        lineSize = 2,
                        density = 1.0f,
                    )
                })
            }

        val textInputView = view.findViewById<TextInputLayout>(R.id.text_input_layout)
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.enablePatternSelectMenu.collect {
                    textInputView.isEnabled = it
                }
            }
        }

        // Inflate the layout for this fragment
        return view
    }
}