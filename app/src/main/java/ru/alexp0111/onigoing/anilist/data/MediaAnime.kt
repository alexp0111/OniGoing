package ru.alexp0111.onigoing.anilist.data

import ru.alexp0111.onigoing.anilist.AnimeMediaQuery
import ru.alexp0111.onigoing.anilist.type.MediaSeason
import ru.alexp0111.onigoing.anilist.type.MediaStatus
import ru.alexp0111.onigoing.utils.asFormattedTimeString

data class AnimeMedia(
    val id: Int,
    val coverImage: String?,
    val thumbnail: String?,
    val title: String?,
    val episodes: Int?,
    val status: MediaStatus?,
    val season: MediaSeason?,
    val seasonYear: Int?,
    val nextAiringEpisode: Int?,
    val averageScore: Int?,
    val description: String?,
    val nextAiringEpisodeSchedule: String?,
) {

    internal constructor(query: AnimeMediaQuery.Medium) : this(
        id = query.id,
        coverImage = query.coverImage?.extraLarge,
        thumbnail = query.coverImage?.medium,
        title = query.title?.english ?: query.title?.native,
        episodes = query.episodes,
        status = query.status,
        season = query.season,
        seasonYear = query.seasonYear,
        averageScore = query.averageScore,
        description = query.description,
        nextAiringEpisode = query.nextAiringEpisode?.episode,
        nextAiringEpisodeSchedule = query.nextAiringEpisode?.timeUntilAiring?.toLong()
            ?.asFormattedTimeString()
    )
}