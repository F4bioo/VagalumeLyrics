package fbo.costa.vagalumelyrics.data.repository

import fbo.costa.vagalumelyrics.BuildConfig
import fbo.costa.vagalumelyrics.data.ApiService
import fbo.costa.vagalumelyrics.model.Search
import fbo.costa.vagalumelyrics.model.search.SearchEntity
import fbo.costa.vagalumelyrics.model.search.source.DocSource
import fbo.costa.vagalumelyrics.util.Constants
import fbo.costa.vagalumelyrics.util.EntityMapper
import fbo.costa.vagalumelyrics.util.state.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchRepository
@Inject
constructor(
    private val apiService: ApiService
) : EntityMapper<SearchEntity, List<Search>> {

    fun searchExcerpt(searchQuery: String): Flow<DataState<List<Search>>> = flow {
        emit(DataState.Loading)

        try {
            val response = apiService.searchExcerpt(BuildConfig.API_KEY, searchQuery)
            if (response.isSuccessful) {
                response.body()?.let { _searchEntity ->
                    emit(DataState.Success(mapFromEntityModel(_searchEntity)))
                }
            } else {
                emit(DataState.Error("Unsuccessful: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(DataState.Error("Exception: ${e.message}"))
        }
    }

    override fun mapFromEntityModel(entityModel: SearchEntity): List<Search> {
        var list: List<DocSource> = ArrayList()
        entityModel.result?.docList?.let { _docList ->
            list = _docList
        }

        return list.map { _doc ->
            Search(
                lyricId = _doc.id,
                artistUrl = _doc.url,
                imageUrl = urlBuild(_doc.url),
                title = _doc.title,
                band = _doc.band
            )
        }
    }

    // /u2/omen.html
    private fun urlBuild(url: String?): String {
        val arr = url?.split("/")?.toTypedArray()
        val artist = arr?.get(1)
        return "${Constants.BASE_URL}/${artist}/images/${artist}.jpg"
    }
}
