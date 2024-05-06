package com.org.egglog.client

import android.app.Application
import android.content.Context
import com.org.egglog.data.datastore.UserDataStore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    abstract fun bindContext(application: Application): Context

//    @Provides
//    fun provideUserDataStore(@ApplicationContext context: Context) : UserDataStore {
//        return UserDataStore(context)
//    }
}