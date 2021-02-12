package fbo.costa.vagalumelyrics.ui.lyric

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import fbo.costa.vagalumelyrics.R
import fbo.costa.vagalumelyrics.databinding.LyricFragmentBinding
import fbo.costa.vagalumelyrics.model.Lyric
import fbo.costa.vagalumelyrics.util.Interface_Impl
import fbo.costa.vagalumelyrics.util.state.DataState
import fbo.costa.vagalumelyrics.util.state.LyricStateEvent

@AndroidEntryPoint
class LyricFragment : Fragment(),
    Interface_Impl.ChildView<Lyric?> {

    private val viewModel: LyricViewModel by viewModels()
    private var _binding: LyricFragmentBinding? = null
    private val binding get() = _binding!!
    private val args: LyricFragmentArgs by navArgs()

    companion object {
        private const val CHILD_VIEW_NOTHING = 0
        private const val CHILD_VIEW_LOADING = 1
        private const val CHILD_VIEW_EMPTY = 2
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LyricFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        observeLyricViewModelEvents()
        observeImageViewModelEvents()
        setListeners()

        // Get data from Other Fragment
        args.searchArgs?.lyricId?.let { _lyricId ->
            viewModel.searchByLyricId(_lyricId, LyricStateEvent.LyricEvent)
        }
    }

    override fun getChild(p0: Lyric?) = when (p0?.success == "exact") {
        true -> {
            viewModel.getImageByArtistId(p0?.artistId, LyricStateEvent.LyricEvent)
            CHILD_VIEW_NOTHING
        }
        else -> {
            binding.includeState.empty.text = getString(R.string.text_lyric_not_found)
            CHILD_VIEW_EMPTY
        }
    }

    private fun observeLyricViewModelEvents() {
        viewModel.lyricEvent.observe(viewLifecycleOwner) { _dataState ->
            when (_dataState) {
                is DataState.Success -> {
                    val lyric = _dataState.data
                    success(lyric)
                }
                is DataState.Loading -> {
                    binding.includeState.root
                        .displayedChild = CHILD_VIEW_LOADING
                }
                is DataState.Error -> {
                    binding.includeState.empty.text = _dataState.message
                    binding.includeState.root
                        .displayedChild = CHILD_VIEW_EMPTY
                }
            }
        }
    }

    private fun observeImageViewModelEvents() {
        viewModel.imageEvent.observe(viewLifecycleOwner) { _dataState ->
            when (_dataState) {
                is DataState.Success -> {
                    val image = _dataState.data
                    binding.imageArtist.loadImage(image.urlImage)
                    binding.progress.isVisible = false
                }
                is DataState.Loading -> {
                    binding.progress.isVisible = true
                }
                is DataState.Error -> {
                    binding.progress.isVisible = false
                }
            }
        }
    }

    private fun setListeners() {
        binding.fab.setOnClickListener {
            binding.flipperLyric.showNext()
        }
    }

    private fun success(lyric: Lyric?) {
        binding.apply {
            textOriginalLyric.text = lyric?.originalLyric
            textTranslatedLyric.text = lyric?.translatedLyric
                ?: getString(R.string.text_translation_not_found)
            toolbarLyric.title = lyric?.artistName
            progress.isVisible = true
            includeState.root
                .displayedChild = getChild(lyric)
        }
    }

    private fun ImageView.loadImage(imageUrl: String?) {
        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.ic_image_placeholder_100dp)
            .error(R.drawable.ic_image_error_100dp)
            .centerCrop()
            .into(this)
    }
}
