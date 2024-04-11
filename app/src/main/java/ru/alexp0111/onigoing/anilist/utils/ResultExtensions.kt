package ru.alexp0111.onigoing.anilist.utils

import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.mapLatest
import java.io.IOException

fun <T: Operation.Data, R> Flow<ApolloResponse<T>>.asResult(transform: (T) -> R): Flow<Result<R>> = this
    .mapLatest { response ->
        if (response.data != null) {
            Result.success(transform(response.dataAssertNoErrors))
        } else if (response.hasErrors()) {
            throw IOException(response.errors!!.joinToString())
        } else {
            error("Unknown error occurred, no data or errors received.")
        }
    }
    .catch { exception ->
        emit(Result.failure(exception))
    }

fun <T: Operation.Data> Flow<ApolloResponse<T>>.asResult(): Flow<Result<T>> = this.asResult { it }