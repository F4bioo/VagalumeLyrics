package fbo.costa.vagalumelyrics.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fbo.costa.vagalumelyrics.BaseApplication
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Singleton
    @Provides
    fun provideApplication(
        @ApplicationContext application: Context
    ): BaseApplication {
        return application as BaseApplication
    }
}
