package com.cinescope.movie

object TmdbImageUrlBuilder {

    fun poster(path: String?, size: String = "w500"): String? {
        if (path.isNullOrBlank()) return null
        return "${TmdbConstants.IMAGE_BASE_URL}$size$path"
    }

    fun backdrop(path: String?, size: String = "w780"): String? {
        if (path.isNullOrBlank()) return null
        return "${TmdbConstants.IMAGE_BASE_URL}$size$path"
    }
}