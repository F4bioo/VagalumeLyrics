package fbo.costa.vagalumelyrics.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fbo.costa.vagalumelyrics.data.SearchRepository
import fbo.costa.vagalumelyrics.model.Search
import fbo.costa.vagalumelyrics.util.state.DataState
import fbo.costa.vagalumelyrics.util.state.SearchStateEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel
@Inject
constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {

    var job: Job? = null

    private val _searchEvent = MutableLiveData<DataState<List<Search>>>()
    val searchEvent: LiveData<DataState<List<Search>>>
        get() = _searchEvent

    fun setStateEvent(searchQuery: String, stateEvent: SearchStateEvent) {
        job = viewModelScope.launch {
            when (stateEvent) {
                is SearchStateEvent.SearchEvent -> {
                    searchRepository.searchExcerpt(searchQuery).collect { _dataState ->
                        _searchEvent.value = _dataState
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}
