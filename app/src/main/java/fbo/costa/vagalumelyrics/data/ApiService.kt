package fbo.costa.vagalumelyrics.data

import fbo.costa.vagalumelyrics.BuildConfig
import fbo.costa.vagalumelyrics.model.image.ImageEntity
import fbo.costa.vagalumelyrics.model.lyric.LyricEntity
import fbo.costa.vagalumelyrics.model.search.SearchEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    // search.excerpt?apikey={apiKey}&q={searchQuery}&limit=1
    // 10 is the maximum limit provided by the api
    @GET("search.excerpt")
    suspend fun searchExcerpt(
        @Query("apikey") apiKey: String,
        @Query("q") searchQuery: String,
        @Query("limit") limit: Int = 10
    ): Response<SearchEntity>

    // search.php?musid={lyricId}&apikey={apiKey}
    @GET("search.php")
    suspend fun searchByLyricId(
        @Query("musid") lyricId: String,
        @Query("apikey") apiKey: String = BuildConfig.API_KEY
    ): Response<LyricEntity>

    // image.php?bandID={artistId}&limit=1&apikey={apiKey}
    // 10 is the maximum limit provided by the api
    @GET("image.php")
    suspend fun getImageByArtistId(
        @Query("bandID") artistId: String?,
        @Query("limit") limit: Int = 1,
        @Query("apikey") apiKey: String = BuildConfig.API_KEY
    ): Response<ImageEntity>
}
