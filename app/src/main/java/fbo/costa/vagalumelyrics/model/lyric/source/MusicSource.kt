package fbo.costa.vagalumelyrics.model.lyric.source


import com.google.gson.annotations.SerializedName

data class MusicSource(
    @SerializedName("id")
    val id: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("url")
    val url: String?,

    @SerializedName("lang")
    val lang: Int?,

    @SerializedName("text")
    val text: String?,

    @SerializedName("translate")
    val translate: List<TranslateSource>?
)
