package fbo.costa.vagalumelyrics.model

data class Lyric(
    val success: String?,
    val artistId: String?,
    val artistName: String?,
    val artistUrl: String?,
    val lyricId: String?,
    val lyricName: String?,
    val lyricUrl: String?,
    val lang: Int?,
    val originalLyric: String?,
    val translatedLyric: String?
)
