package com.rustan.data.model


data class PhotoDTO(
    val urls: Urls
) {
    data class Urls(
        val regular: String
    )
}