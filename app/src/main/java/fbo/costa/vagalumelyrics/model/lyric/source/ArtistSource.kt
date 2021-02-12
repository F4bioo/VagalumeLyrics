package fbo.costa.vagalumelyrics.model.lyric.source


import com.google.gson.annotations.SerializedName

data class ArtistSource(
    @SerializedName("id")
    val id: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("url")
    val url: String?
)
