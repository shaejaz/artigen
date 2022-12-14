package com.shaejaz.artigen.bottompanel

import android.app.WallpaperManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.shaejaz.artigen.R
import com.shaejaz.artigen.databinding.FragEditPatternBinding
import com.shaejaz.artigen.image.ImageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


@AndroidEntryPoint
class EditPattern : Fragment() {
    private lateinit var binding: FragEditPatternBinding
    private val viewModel by activityViewModels<BottomPanelViewModel>()
    private val imageViewModel by activityViewModels<ImageViewModel>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.frag_edit_pattern, container, false)
        binding = FragEditPatternBinding.bind(view).apply {
            this.viewmodel = viewModel
        }
        binding.lifecycleOwner = viewLifecycleOwner

        binding.editPatternButton.setOnClickListener {
            viewModel.editConfigButtonClick()
        }

        binding.generatePatternButton.setOnClickListener {
            imageViewModel.generateImage()
        }

        binding.applyWallpaperButton.setOnClickListener {
            val image = imageViewModel.image.value
            val wallpaperManager = WallpaperManager.getInstance(activity)
            if (image != null) {
                wallpaperManager.setBitmap(image)
                val toast = Toast.makeText(activity, "Pattern set as wallpaper.", Toast.LENGTH_SHORT)
                toast.show()
            }
        }

        binding.shareButton.setOnClickListener {
            viewModel.shareButtonClick()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                imageViewModel.imageBase64String.collect {
                    if (it != null) {
                        viewModel.showApplyWallpaperButton.emit(true)
                    }
                }
            }
        }

        return binding.root
    }
}