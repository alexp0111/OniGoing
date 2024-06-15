package ru.alexp0111.onigoing.anilist.data

import ru.alexp0111.onigoing.anilist.SearchQuery
import ru.alexp0111.onigoing.anilist.type.MediaStatus

data class Search(
    val id: Int,
    val coverImage: String?,
    val title: String?,
    val episodes: Int?,
    val status: MediaStatus?,
    val nextAiringEpisode: Int?,
    val averageScore: Int?,
) {

    internal constructor(query: SearchQuery.Medium) : this(
        id = query.id,
        coverImage = query.coverImage?.extraLarge,
        title = query.title?.english ?: query.title?.native,
        episodes = query.episodes,
        status = query.status,
        nextAiringEpisode = query.nextAiringEpisode?.episode,
        averageScore = query.averageScore,
    )
}