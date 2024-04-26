package ru.alexp0111.onigoing.di.modules

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

private const val SHARED_PREFS_FILE_NAME = "onigoing_shared_prefs"

@Module
class AppModule(
    private val application: Application,
) {

    @Provides
    @Singleton
    fun provideContext(): Context = this.application

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFS_FILE_NAME, MODE_PRIVATE)
    }
}