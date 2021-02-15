package fbo.costa.vagalumelyrics.data.repository

import fbo.costa.vagalumelyrics.data.ApiService
import fbo.costa.vagalumelyrics.model.Image
import fbo.costa.vagalumelyrics.model.image.ImageEntity
import fbo.costa.vagalumelyrics.util.EntityMapper
import fbo.costa.vagalumelyrics.util.state.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ImageRepository
@Inject
constructor(
    private val apiService: ApiService
) : EntityMapper<ImageEntity, Image> {

    fun getImageByArtistId(artistId: String?): Flow<DataState<Image>> = flow {
        emit(DataState.Loading)

        try {
            val response = apiService.getImageByArtistId(artistId)
            if (response.isSuccessful) {
                response.body()?.let { _imageEntity ->
                    emit(DataState.Success(mapFromEntityModel(_imageEntity)))
                }
            } else {
                emit(DataState.Error("Unsuccessful: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(DataState.Error("Exception: ${e.message}"))
        }
    }

    override fun mapFromEntityModel(entityModel: ImageEntity): Image {
        return Image(
            urlImage = entityModel.imageList?.get(0)?.url
        )
    }
}
