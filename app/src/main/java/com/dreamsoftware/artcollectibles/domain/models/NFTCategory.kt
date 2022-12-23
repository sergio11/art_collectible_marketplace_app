package com.dreamsoftware.artcollectibles.domain.models

import com.dreamsoftware.artcollectibles.R
import java.util.*

data class NFTCategory(
    val title: String,
    val image: Int,
    val id: UUID = UUID.randomUUID()
)

val categories = listOf<NFTCategory>(
    NFTCategory("Music", R.drawable.card_music),
    NFTCategory("Art", R.drawable.card_art),
    NFTCategory("Virtual Worlds", R.drawable.card_vw)
)