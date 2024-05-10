package ru.alexp0111.onigoing.database.user_watching_anime

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.alexp0111.onigoing.database.user_watching_anime.data.UserWatchingAnime

@Database(entities = [UserWatchingAnime::class], version = 1)
abstract class UserWatchingAnimeDatabase : RoomDatabase() {
    abstract fun userWatchingAnimeDao(): UserWatchingAnimeDao
}