package com.example.myapp.di

import com.example.myapp.data.repository.CollectionRepositoryImpl
import com.example.myapp.data.repository.HomeItemDetailRepositoryImpl
import com.example.myapp.data.repository.HomeRepositoryImpl
import com.example.myapp.repository.CollectionRepository
import com.example.myapp.repository.HomeItemDetailRepository
import com.example.myapp.repository.HomeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindHomeRepository(
        homeRepositoryImpl: HomeRepositoryImpl
    ): HomeRepository

    @Binds
    @Singleton
    abstract fun bindCollectionRepository(
        collectionRepositoryImpl: CollectionRepositoryImpl
    ): CollectionRepository

    @Binds
    @Singleton
    abstract fun bindDetailRepository(
        homeItemDetailRepositoryImpl: HomeItemDetailRepositoryImpl
    ): HomeItemDetailRepository

}