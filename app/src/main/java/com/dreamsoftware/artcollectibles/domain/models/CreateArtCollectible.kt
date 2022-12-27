package com.dreamsoftware.artcollectibles.domain.models

import java.io.File

class CreateArtCollectible(
    val name: String,
    val description: String? = null,
    val royalty: Long,
    val file: File,
    val mediaType: String
)