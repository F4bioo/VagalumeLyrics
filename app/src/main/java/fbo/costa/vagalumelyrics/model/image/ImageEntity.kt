package fbo.costa.vagalumelyrics.model.image

import com.google.gson.annotations.SerializedName
import fbo.costa.vagalumelyrics.model.image.source.ImageSource

data class ImageEntity(
    @SerializedName("images")
    val imageList: List<ImageSource>?
)
