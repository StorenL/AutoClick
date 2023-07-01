package com.storen.autoclick.di

import android.content.Context
import android.view.WindowManager
import android.view.accessibility.AccessibilityManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AutoClickHiltModule {

    @Provides
    @Singleton
    fun provideAccessibilityManager(@ApplicationContext context: Context) = run {
        context.getSystemService(AccessibilityManager::class.java)
    }

//    @Provides
//    @Singleton
//    fun provideWindowManager(@ApplicationContext context: Context) = run {
//        context.getSystemService(WindowManager::class.java)
//    }

}