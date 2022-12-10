package com.shaejaz.artigen.bottompanel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.shaejaz.artigen.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditPattern : Fragment() {
    private val viewModel by activityViewModels<BottomPanelViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.frag_edit_pattern, container, false)

        val editConfigButton = view.findViewById<FloatingActionButton>(R.id.edit_pattern_button)
        editConfigButton.setOnClickListener {
            viewModel.editConfigButtonClick()
        }

        return view
    }
}