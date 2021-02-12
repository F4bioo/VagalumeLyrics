package fbo.costa.vagalumelyrics.model.search.source

import com.google.gson.annotations.SerializedName

data class ResultSource(
    @SerializedName("numFound")
    val numFound: Int?,

    @SerializedName("docs")
    val docList: List<DocSource>?
)
