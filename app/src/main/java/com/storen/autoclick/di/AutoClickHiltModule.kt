package com.storen.autoclick.di

import android.app.NotificationManager
import android.content.Context
import android.view.WindowManager
import android.view.accessibility.AccessibilityManager
import androidx.core.content.getSystemService
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
    fun provideAccessibilityManager(@ApplicationContext context: Context): AccessibilityManager = run {
        context.getSystemService(AccessibilityManager::class.java)
    }

    @Provides
    @Singleton
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManager = kotlin.run {
        context.getSystemService(NotificationManager::class.java)
    }

    /*在Service和Activity中获取的WindowManager并不一样*/
    @Provides
    @Singleton
    fun provideAppWindowManager(@ApplicationContext context: Context): WindowManager = run {
        context.getSystemService(WindowManager::class.java)
    }

}