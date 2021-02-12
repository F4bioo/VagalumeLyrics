package fbo.costa.vagalumelyrics.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Search(
    val lyricId: String?,
    val artistUrl: String?,
    val imageUrl: String?,
    val title: String?,
    val band: String?,
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        if (javaClass != other?.javaClass) return false

        val search = other as Search
        return lyricId == search.lyricId
                && imageUrl == search.imageUrl
                && title == search.title
                && band == search.band
    }

    override fun hashCode(): Int {
        return Objects.hash(
            lyricId,
            imageUrl,
            title,
            band
        )
    }

    override fun toString(): String {
        return "Search(lyricId=$lyricId, imageUrl=$imageUrl, title=$title, band=$band)"
    }
}
