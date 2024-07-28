package ru.alexp0111.onigoing.anilist.api

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.first
import ru.alexp0111.onigoing.anilist.data.Search
import ru.alexp0111.onigoing.anilist.type.MediaType

class SearchAnimeDataSource(
    private val type: MediaType,
    private val search: String?,
    private val repository: AnilistRepository,
) : PagingSource<Int, Search>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Search> {
        val position = params.key ?: STARTING_PAGE_INDEX

        val result = repository.fetchSearch(type, PER_PAGE, position, search).first()
        return when {
            result.isFailure -> LoadResult.Error(Exception("NOT FOUND"))
            result.getOrDefault(emptyList()).isEmpty() -> LoadResult.Error(Exception("IS EMPTY"))
            else -> LoadResult.Page(
                data = result.getOrDefault(emptyList()),
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (result.getOrDefault(emptyList()).isEmpty()) null else position + 1
            )
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Search>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 1
        const val PER_PAGE = 20
        const val MAX_PAGE_COUNT = 1000
    }
}