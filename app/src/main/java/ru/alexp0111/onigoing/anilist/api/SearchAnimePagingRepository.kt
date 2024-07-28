package ru.alexp0111.onigoing.anilist.api

import androidx.paging.Pager
import androidx.paging.PagingConfig
import ru.alexp0111.onigoing.anilist.type.MediaType
import javax.inject.Inject

class SearchAnimePagingRepository @Inject constructor(
    private val repository: AnilistRepository,
) {
    private var pagingConfig: PagingConfig

    init {
        pagingConfig = PagingConfig(
            pageSize = SearchAnimeDataSource.PER_PAGE,
            maxSize = SearchAnimeDataSource.MAX_PAGE_COUNT,

        )
    }

    fun fetchSearch(type: MediaType, search: String?) = Pager(
        config = pagingConfig,
        pagingSourceFactory = { SearchAnimeDataSource(type, search, repository) }).flow
}