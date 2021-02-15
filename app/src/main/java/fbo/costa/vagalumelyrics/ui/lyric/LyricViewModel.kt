package fbo.costa.vagalumelyrics.ui.lyric

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fbo.costa.vagalumelyrics.data.repository.ImageRepository
import fbo.costa.vagalumelyrics.data.repository.LyricRepository
import fbo.costa.vagalumelyrics.model.Image
import fbo.costa.vagalumelyrics.model.Lyric
import fbo.costa.vagalumelyrics.util.state.DataState
import fbo.costa.vagalumelyrics.util.state.LyricStateEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LyricViewModel
@Inject
constructor(
    private val lyricRepository: LyricRepository,
    private val imageRepository: ImageRepository
) : ViewModel() {

    var job: Job? = null

    private val _lyricEvent = MutableLiveData<DataState<Lyric>>()
    val lyricEvent: LiveData<DataState<Lyric>>
        get() = _lyricEvent

    private val _imageEvent = MutableLiveData<DataState<Image>>()
    val imageEvent: LiveData<DataState<Image>>
        get() = _imageEvent

    fun searchByLyricId(lyricId: String, stateEvent: LyricStateEvent) {
        job = viewModelScope.launch {
            when (stateEvent) {
                is LyricStateEvent.LyricEvent -> {
                    lyricRepository.searchByLyricId(lyricId).collect { _dataState ->
                        _lyricEvent.value = _dataState
                    }
                }
            }
        }
    }

    fun getImageByArtistId(artistId: String?, stateEvent: LyricStateEvent) {
        job = viewModelScope.launch {
            when (stateEvent) {
                is LyricStateEvent.LyricEvent -> {
                    imageRepository.getImageByArtistId(artistId).collect { _dataState ->
                        _imageEvent.value = _dataState
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
