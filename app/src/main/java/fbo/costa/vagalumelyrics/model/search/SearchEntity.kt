package fbo.costa.vagalumelyrics.model.search

import com.google.gson.annotations.SerializedName
import fbo.costa.vagalumelyrics.model.search.source.ResultSource

data class SearchEntity(
    @SerializedName("response")
    val result: ResultSource?
)
