package ru.alexp0111.onigoing.anilist.data

import ru.alexp0111.onigoing.anilist.SearchQuery

data class Search(
    val id: Int,
    val coverImage: String?,
    val title: String?,
    val season: Season,
    val seasonYear: Int?,
    val studios: List<String>,
    val format: Format,
    val episodes: Int?,
    val averageScore: Int?,
) {
    enum class Season(val season: String) {
        WINTER("Winter"),
        SPRING("Spring"),
        SUMMER("Summer"),
        FALL("Fall"),
        UNKNOWN("")
    }

    enum class Format(val string: String) {
        TV("TV"),
        TV_SHORT("TV SHORT"),
        MOVIE("MOVIE"),
        SPECIAL("SPECIAL"),
        OVA("OVA"),
        ONA("ONA"),
        MUSIC("MUSIC"),
        MANGA("MANGA"),
        NOVEL("NOVEL"),
        ONE_SHOT("ONE SHOT"),
        UNKNOWN("")
    }

    internal constructor(query: SearchQuery.Medium) : this(
        id = query.id,
        coverImage = query.coverImage?.extraLarge,
        title = query.title?.english ?: query.title?.native,
        season = query.season?.let { Season.valueOf(it.name) } ?: Season.UNKNOWN,
        seasonYear = query.seasonYear,
        studios = if (query.studios?.nodes == null) { emptyList() } else {
            query.studios.nodes.filter { it?.name != null }.map { it!!.name }
        },
        format = query.format?.let { Format.valueOf(it.name) } ?: Format.UNKNOWN,
        episodes = query.episodes,
        averageScore = query.averageScore,
    )
}