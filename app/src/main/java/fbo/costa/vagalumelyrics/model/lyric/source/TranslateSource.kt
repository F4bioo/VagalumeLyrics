package fbo.costa.vagalumelyrics.model.lyric.source


import com.google.gson.annotations.SerializedName

data class TranslateSource(
    @SerializedName("id")
    val id: String?,

    @SerializedName("lang")
    val lang: Int?,

    @SerializedName("url")
    val url: String?,

    @SerializedName("text")
    val translate: String?
)
