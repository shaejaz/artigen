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
import com.shaejaz.artigen.R
import com.shaejaz.artigen.data.Pattern
import dagger.hilt.android.AndroidEntryPoint


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
            Pattern.Painted -> "Painted"
            else -> "Blocks"
        }, false)

        autoCompleteTextView.onItemClickListener =
            OnItemClickListener { _, _, position, _ ->
                val selectedItem = autoCompleteTextView.adapter.getItem(position).toString()

                viewModel.setSelectedPattern(when (selectedItem) {
                    "Blocks" -> Pattern.Blocks
                    "Painted" -> Pattern.Painted
                    else -> Pattern.Blocks
                })
            }

        // Inflate the layout for this fragment
        return view
    }
}