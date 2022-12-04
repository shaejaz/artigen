package com.shaejaz.artigen.di

import com.shaejaz.artigen.data.repositories.ConfigRepository
import com.shaejaz.artigen.data.repositories.DefaultConfigRepository
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@dagger.Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideConfigRepository(): ConfigRepository = DefaultConfigRepository()
}