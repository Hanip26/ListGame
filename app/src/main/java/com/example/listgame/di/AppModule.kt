package com.example.listgame.di

import android.content.Context
import com.example.listgame.data.AppDataStore
import com.example.listgame.data.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDataStore(
        @ApplicationContext context: Context
    ): AppDataStore = AppDataStore(context)

    @Provides
    @Singleton
    fun provideUserRepository(
        appDataStore: AppDataStore
    ): UserRepository = UserRepository(appDataStore)
}
