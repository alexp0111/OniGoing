package ru.alexp0111.onigoing.anilist.data

import ru.alexp0111.onigoing.anilist.SearchQuery

data class Search(
    val id: Int,
    val coverImage: String?,
    val title: String?,
    val episodes: Int?,
    val averageScore: Int?,
) {

    internal constructor(query: SearchQuery.Medium) : this(
        id = query.id,
        coverImage = query.coverImage?.extraLarge,
        title = query.title?.english,
        episodes = query.episodes,
        averageScore = query.averageScore,
    )
}