package fbo.costa.vagalumelyrics.ui.search

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import fbo.costa.vagalumelyrics.R
import fbo.costa.vagalumelyrics.databinding.SearchFragmentBinding
import fbo.costa.vagalumelyrics.extension.navigateWithAnimations
import fbo.costa.vagalumelyrics.ui.adapter.LyricAdapter
import fbo.costa.vagalumelyrics.util.Interface_Impl
import fbo.costa.vagalumelyrics.util.NetworkLiveData
import fbo.costa.vagalumelyrics.util.UtilQueryTextListener
import fbo.costa.vagalumelyrics.util.state.DataState
import fbo.costa.vagalumelyrics.util.state.SearchStateEvent
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment(),
    Interface_Impl.ChildView<Int>,
    Interface_Impl.NetworkChecker {

    private val viewModel: SearchViewModel by viewModels()
    private var _binding: SearchFragmentBinding? = null
    private var searchView: SearchView? = null
    private val binding get() = _binding!!
    private var collapse: Boolean = false

    @Inject
    lateinit var cnnLiveData: NetworkLiveData

    companion object {
        private const val CHILD_VIEW_NOTHING = 0
        private const val CHILD_VIEW_LOADING = 1
        private const val CHILD_VIEW_EMPTY = 2
        private const val CHILD_VIEW_OFFLINE = 3
    }

    private val lyricAdapter by lazy {
        LyricAdapter { _search ->
            val directions = SearchFragmentDirections
                .actionSearchFragmentToLyricFragment(_search)
            if (cnnLiveData.isOnline()) {
                // Send data to Other Fragment
                //findNavController().navigate(directions) // No animation
                findNavController().navigateWithAnimations(directions)
            } else Toast.makeText(
                requireContext(),
                getString(R.string.text_connection),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (_binding == null) {
            _binding = SearchFragmentBinding.inflate(inflater, container, false)

        } else {
            // Do not inflate the layout again.
            // The returned View of onCreateView will be added into the fragment.
            // However it is not allowed to be added twice even if the parent is same.
            // So we must remove _rootView from the existing parent view group
            // (it will be added back).
            binding.root.removeView(binding.root)
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        checkNetwork(cnnLiveData.isOnline())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        setHasOptionsMenu(true)

        observeNetwork()
        observeAdapterViewModelEvents()
        iniRecycler()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
        val itemSearch = menu.findItem(R.id.action_search)
        searchView = itemSearch?.actionView as SearchView
        setSearchView()
        collapse = true
    }

    override fun getChild(p0: Int) = when (p0 > 0) {
        true -> CHILD_VIEW_NOTHING
        else -> {
            binding.includeState.empty.text = getString(R.string.text_not_found)
            CHILD_VIEW_EMPTY
        }
    }

    override fun checkNetwork(isOnline: Boolean) {
        binding.includeState.root
            .displayedChild = if (isOnline)
            CHILD_VIEW_NOTHING else CHILD_VIEW_OFFLINE
    }

    private fun observeNetwork() {
        cnnLiveData.observe(viewLifecycleOwner) { _isOnline ->
            checkNetwork(_isOnline)
        }
    }

    private fun observeAdapterViewModelEvents() {
        viewModel.searchEvent.observe(viewLifecycleOwner) { _dataState ->
            when (_dataState) {
                is DataState.Success -> {
                    val lyricList = _dataState.data
                    lyricAdapter.submitList(lyricList)
                    binding.includeState.root
                        .displayedChild = getChild(lyricAdapter.itemCount)
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

    private fun iniRecycler() {
        binding.recyclerSearch.adapter = lyricAdapter
    }

    private fun setSearchView() {
        searchView?.setIconifiedByDefault(false)
        searchView?.isIconified = collapse
        searchView?.findViewById<View>(androidx.appcompat.R.id.search_plate)
            ?.setBackgroundColor(Color.TRANSPARENT)
        searchView?.queryHint = getString(R.string.text_search)

        searchView?.setOnQueryTextListener(UtilQueryTextListener(lifecycle) { _newText ->
            _newText?.let { _query ->
                if (_query.isNotEmpty()) {
                    viewModel.setStateEvent(_query, SearchStateEvent.SearchEvent)
                }
            }
        })
    }
}
