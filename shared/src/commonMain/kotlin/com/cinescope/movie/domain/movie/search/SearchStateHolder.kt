package com.cinescope.movie.domain.movie.search

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException


class SearchStateHolder(
    private val searchMoviesUseCase: SearchMoviesUseCase
) {
    private val scopeJob = SupervisorJob()
    private val scope = CoroutineScope(scopeJob + Dispatchers.Main)

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null
    private var currentQuery: String = ""

    fun search(
        query: String,
        force: Boolean = false
    ) {
        val normalizedQuery = query.trim()
        val currentState = _uiState.value

        if (
            !force &&
            normalizedQuery == currentState.query &&
            currentState.hasSearched
        ) {
            return
        }
        currentQuery = normalizedQuery
        searchJob?.cancel()

        if (normalizedQuery.isBlank()) {
            _uiState.value = SearchUiState()
            return
        }

        _uiState.value = SearchUiState(
            query = normalizedQuery,
            isLoading = true,
            hasSearched = true
        )

        searchJob = scope.launch {
            try {
                val result = searchMoviesUseCase(
                    query = normalizedQuery,
                    page = 1
                )

                _uiState.value = SearchUiState(
                    query = normalizedQuery,
                    movies = result.movies,
                    currentPage = result.page,
                    totalPages = result.totalPages,
                    hasSearched = true
                )
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.value = SearchUiState(
                    query = normalizedQuery,
                    isLoading = false,
                    error = exception.message ?: "Something went wrong",
                    hasSearched = true
                )
            }
        }
    }

    fun loadNextPage() {
        val state = _uiState.value

        if (state.isLoading || state.isLoadingMore) return
        if (state.currentPage >= state.totalPages) return
        if (state.query.isBlank()) return

        searchJob = scope.launch {
            _uiState.update {
                it.copy(
                    isLoadingMore = true,
                    error = null
                )
            }

            try {
                val nextPage = state.currentPage + 1

                val result = searchMoviesUseCase(
                    query = state.query,
                    page = nextPage
                )

                _uiState.update {
                    it.copy(
                        movies = it.movies + result.movies,
                        currentPage = result.page,
                        totalPages = result.totalPages,
                        isLoadingMore = false
                    )
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        isLoadingMore = false,
                        error = exception.message ?: "Something went wrong"
                    )
                }
            }
        }
    }

    fun retry() {
        val query = _uiState.value.query
        if (query.isNotBlank()) {
            search(query, force = true)
        }
    }

    fun clear() {
        searchJob?.cancel()
        scope.cancel()
    }
}
