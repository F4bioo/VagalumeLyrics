package fbo.costa.vagalumelyrics.model.search.source


import com.google.gson.annotations.SerializedName

data class DocSource(
    @SerializedName("id")
    val id: String?,

    @SerializedName("langID")
    val langID: Int?,

    @SerializedName("url")
    val url: String?,

    @SerializedName("title")
    val title: String?,

    @SerializedName("band")
    val band: String?,
)
