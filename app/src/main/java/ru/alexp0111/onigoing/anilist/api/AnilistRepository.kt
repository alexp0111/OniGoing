package ru.alexp0111.onigoing.anilist.api

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import kotlinx.coroutines.flow.Flow
import ru.alexp0111.onigoing.anilist.SearchQuery
import ru.alexp0111.onigoing.anilist.data.Search
import ru.alexp0111.onigoing.anilist.type.MediaType
import ru.alexp0111.onigoing.anilist.utils.asResult
import javax.inject.Inject

class AnilistRepository @Inject constructor(
    private val apolloClient: ApolloClient,
) {
    fun fetchSearch(
        type: MediaType,
        perPage: Int,
        search: String?
    ): Flow<Result<List<Search>>> {
        return apolloClient
            .query(
                SearchQuery(
                    type = Optional.presentIfNotNull(type),
                    perPage = Optional.presentIfNotNull(perPage),
                    search = Optional.presentIfNotNull(search)
                )
            )
            .toFlow()
            .asResult { it.page!!.media.orEmpty().filterNotNull().map { query -> Search(query) } }
    }
}