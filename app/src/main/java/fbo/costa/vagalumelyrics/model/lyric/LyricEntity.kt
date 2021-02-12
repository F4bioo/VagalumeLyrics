package fbo.costa.vagalumelyrics.model.lyric

import com.google.gson.annotations.SerializedName
import fbo.costa.vagalumelyrics.model.lyric.source.ArtistSource
import fbo.costa.vagalumelyrics.model.lyric.source.MusicSource

data class LyricEntity(
    @SerializedName("type")
    val type: String?,

    @SerializedName("art")
    val artist: ArtistSource?,

    @SerializedName("mus")
    val musicList: List<MusicSource>?,

    @SerializedName("badwords")
    val badWords: Boolean?
)
