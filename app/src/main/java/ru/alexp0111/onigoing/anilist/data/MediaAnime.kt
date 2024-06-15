package ru.alexp0111.onigoing.anilist.data

import ru.alexp0111.onigoing.anilist.AnimeMediaQuery
import ru.alexp0111.onigoing.utils.asFormattedTimeString

data class AnimeMedia(
    val id: Int,
    val coverImage: String?,
    val thumbnail: String?,
    val title: String?,
    val episodes: Int?,
    val averageScore: Int?,
    val description: String?,
    val nextAiringEpisodeSchedule: String?,
) {

    internal constructor(query: AnimeMediaQuery.Medium) : this(
        id = query.id,
        coverImage = query.coverImage?.extraLarge,
        thumbnail = query.coverImage?.medium,
        title = query.title?.english,
        episodes = query.episodes,
        averageScore = query.averageScore,
        description = query.description,
        nextAiringEpisodeSchedule = query.nextAiringEpisode?.timeUntilAiring?.toLong()
            ?.asFormattedTimeString()
    )
}