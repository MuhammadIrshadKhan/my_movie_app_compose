package com.example.moviapp.di

import android.app.Application
import androidx.room.Room
import com.example.moviapp.data.local_data.MovieDatabase
import com.example.moviapp.data.remote_data.api.MovieApiInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val interceptor : HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client : OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()



    @Provides
    @Singleton
    fun provideMovieApi() : MovieApiInterface{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(MovieApiInterface.BASE_URL)
            .client(client)
            .build()
            .create(MovieApiInterface::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(application: Application) : MovieDatabase{
        return Room.databaseBuilder(
            application,
            MovieDatabase::class.java,
            "movieDb_db"
        ).build()
    }
}