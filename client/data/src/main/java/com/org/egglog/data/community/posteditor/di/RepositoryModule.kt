package com.org.egglog.data.community.posteditor.di

import com.org.egglog.data.community.posteditor.service.FirebaseStorageRepository
import com.org.egglog.data.community.posteditor.service.StorageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideFirebaseStorage() : FirebaseStorageRepository {
         return FirebaseStorageRepository()
    }
}