package ru.alexp0111.onigoing.di.modules

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.alexp0111.onigoing.database.user_watching_anime.UserWatchingAnimeDatabase
import ru.alexp0111.onigoing.utils.account.ProfileManager
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

    @Provides
    @Singleton
    fun provideProfileManager(sharedPreferences: SharedPreferences): ProfileManager {
        return ProfileManager(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideUserWatchingAnimeDatabase(context: Context): UserWatchingAnimeDatabase {
        return Room.databaseBuilder(
            context,
            UserWatchingAnimeDatabase::class.java,
            "user_watching_anime_database",
        ).fallbackToDestructiveMigration().build()
    }
}