package ru.alexp0111.onigoing.database.user_watching_anime

import kotlinx.coroutines.flow.Flow
import ru.alexp0111.onigoing.database.user_watching_anime.data.UserWatchingAnime
import javax.inject.Inject

class UserWatchingAnimeRepository @Inject constructor(
    private val databaseUserWatchingAnime: UserWatchingAnimeDatabase,
) {
    private val userWatchingAnimeDao = databaseUserWatchingAnime.userWatchingAnimeDao()

    fun getAllAnimeWithStatus(animeWatchingStatus: Int): Flow<List<UserWatchingAnime>> {
        return userWatchingAnimeDao.getAllAnimeWithState(animeWatchingStatus)
    }

    suspend fun getAnimeByID(animeId: Int): UserWatchingAnime? {
        return userWatchingAnimeDao.getAnimeWithId(animeId)
    }

    suspend fun insertNewAnime(userWatchingAnime: UserWatchingAnime) {
        userWatchingAnimeDao.insertNewAnime(userWatchingAnime)
    }

    suspend fun updateAnimeState(userWatchingAnime: UserWatchingAnime) {
        userWatchingAnimeDao.updateAnimeState(userWatchingAnime)
    }

    suspend fun updateAnimeStateById(animeId: Int, status: Int) {
        userWatchingAnimeDao.updateAnimeStateById(animeId, status)
    }

    suspend fun deleteAnime(anime: UserWatchingAnime) {
        userWatchingAnimeDao.deleteAnime(anime)
    }

    suspend fun deleteAllAnime() {
        userWatchingAnimeDao.deleteAllAnime()
    }

}