package com.stable.scoi.di

import com.stable.scoi.domain.repository.DummyRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
//    @Singleton
//    @Binds
//    abstract fun providesRepository(repository: DummyRepository): DummyRepository
}