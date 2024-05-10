package ru.alexp0111.onigoing.database.user_watching_anime.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_lists")
data class UserWatchingAnime(
    @PrimaryKey
    var id: Int,
    var title: String,
    var imageUriString: String,
    var mark: Int,
    var currentSeries: Int,
    var watchingState: Int,
    var addingDate: String,
)