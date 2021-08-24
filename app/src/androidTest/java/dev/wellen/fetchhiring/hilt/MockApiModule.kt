package dev.wellen.fetchhiring.hilt

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dev.wellen.fetchhiring.model.FetchHiringApi
import org.mockito.kotlin.mock
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [HiltApiModule::class]
)
class MockApiModule {

    @Singleton
    @Provides
    fun providesMockFetchHiringApi(): FetchHiringApi = mock()
}