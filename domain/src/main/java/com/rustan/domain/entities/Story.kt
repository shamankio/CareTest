package com.rustan.domain.entities

import java.net.URL

data class Story(
    val imageURL: URL
)

data class UnsplashPhoto(
    val urls: Urls
) {
    data class Urls(
        val regular: String
    )
}