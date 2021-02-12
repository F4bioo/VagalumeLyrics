package fbo.costa.vagalumelyrics.data

import fbo.costa.vagalumelyrics.model.Lyric
import fbo.costa.vagalumelyrics.model.lyric.LyricEntity
import fbo.costa.vagalumelyrics.model.lyric.source.MusicSource
import fbo.costa.vagalumelyrics.util.EntityMapper
import fbo.costa.vagalumelyrics.util.state.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LyricRepository
@Inject
constructor(
    private val apiService: ApiService
) : EntityMapper<LyricEntity, Lyric> {

    fun searchByLyricId(lyricId: String): Flow<DataState<Lyric>> = flow {
        emit(DataState.Loading)

        try {
            val response = apiService.searchByLyricId(lyricId)
            if (response.isSuccessful) {
                response.body()?.let { _lyricEntity ->
                    emit(DataState.Success(mapFromEntityModel(_lyricEntity)))
                }
            } else {
                emit(DataState.Error("Unsuccessful: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(DataState.Error("Exception: ${e.message}"))
        }
    }

    override fun mapFromEntityModel(entityModel: LyricEntity): Lyric {
        var music: MusicSource? = null
        entityModel.musicList?.get(0)?.let { _music ->
            music = _music
        }

        return Lyric(
            success = entityModel.type,
            artistId = entityModel.artist?.id,
            artistName = entityModel.artist?.name,
            artistUrl = entityModel.artist?.url,
            lyricId = music?.id,
            lyricName = music?.name,
            lyricUrl = music?.url,
            lang = music?.lang,
            originalLyric = music?.text,
            translatedLyric = music?.translate?.get(0)?.translate
        )
    }
}
