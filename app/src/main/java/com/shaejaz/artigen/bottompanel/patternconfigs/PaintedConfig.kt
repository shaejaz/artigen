package com.shaejaz.artigen.bottompanel.patternconfigs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.shaejaz.artigen.R
import com.shaejaz.artigen.bottompanel.BottomPanelViewModel
import com.shaejaz.artigen.data.PaintedConfig
import com.shaejaz.artigen.utils.ColorPicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PaintedConfig : Fragment() {
    private val viewModel by viewModels<BottomPanelViewModel>()
    private var primaryColor: String? = "#FFFFFFFF"
    private var secondaryColor: String? = "#FFFFFFFF"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.frag_painted_config, container, false)

        val primaryColorPicker = view.findViewById<ColorPicker>(R.id.primary_color_picker)
        primaryColorPicker.setSelectedColorChangedListener {
            Log.i("MAIN", it)

            primaryColor = it
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.setConfig(
                    PaintedConfig(
                        x = 0, y = 0,
                        primaryColor = primaryColor!!,
                        secondaryColor = secondaryColor!!,
                    )
                )
            }
        }

        val secondaryColorPicker = view.findViewById<ColorPicker>(R.id.secondary_color_picker)
        secondaryColorPicker.setSelectedColorChangedListener {
            Log.i("MAIN", it)

            secondaryColor = it
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.setConfig(
                    PaintedConfig(
                        x = 0, y = 0,
                        primaryColor = primaryColor!!,
                        secondaryColor = secondaryColor!!,
                    )
                )
            }
        }

        return view
    }
}