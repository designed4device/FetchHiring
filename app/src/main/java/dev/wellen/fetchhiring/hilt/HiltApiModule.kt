package dev.wellen.fetchhiring.hilt

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.wellen.fetchhiring.model.FetchHiringApi
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HiltApiModule {

    @Singleton
    @Provides
    fun provideFetchHiringApi(): FetchHiringApi {
        return Retrofit.Builder()
            .baseUrl("https://fetch-hiring.s3.amazonaws.com")
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
            .create(FetchHiringApi::class.java)
    }
}