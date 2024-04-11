package ru.alexp0111.onigoing.di.modules

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.http.LoggingInterceptor
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

private const val API_URL = "https://graphql.anilist.co/"

@Module
class NetworkModule {
    @Provides
    @Singleton
    fun provideApolloClient(): ApolloClient {
        return ApolloClient.Builder()
            .dispatcher(Dispatchers.IO)
            .serverUrl(API_URL)
            .addHttpInterceptor(LoggingInterceptor(LoggingInterceptor.Level.BODY))
            .build()
    }
}