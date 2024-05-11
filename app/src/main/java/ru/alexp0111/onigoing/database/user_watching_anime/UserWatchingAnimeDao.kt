package ru.alexp0111.onigoing.database.user_watching_anime

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.alexp0111.onigoing.database.user_watching_anime.data.UserWatchingAnime

@Dao
interface UserWatchingAnimeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewAnime(userWatchingAnime: UserWatchingAnime): Long

    @Update
    suspend fun updateAnimeState(userWatchingAnime: UserWatchingAnime)

    @Query("UPDATE user_lists SET currentSeries = :curSeries WHERE id = :animeId")
    suspend fun updateAnimeSeriesById(animeId: Int, curSeries: Int)

    @Query("UPDATE user_lists SET watchingState = :status WHERE id = :animeId")
    suspend fun updateAnimeStateById(animeId: Int, status: Int)

    @Delete
    suspend fun deleteAnime(userWatchingAnime: UserWatchingAnime): Int

    @Query("SELECT * FROM user_lists WHERE watchingState=:watchingState")
    fun getAllAnimeWithState(watchingState: Int): Flow<List<UserWatchingAnime>>

    @Query("SELECT * FROM user_lists WHERE id=:animeId")
    suspend fun getAnimeWithId(animeId: Int): UserWatchingAnime?

    @Query("DELETE FROM user_lists")
    suspend fun deleteAllAnime()
}