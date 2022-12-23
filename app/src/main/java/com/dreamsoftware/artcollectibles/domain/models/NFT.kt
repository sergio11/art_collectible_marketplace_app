package com.dreamsoftware.artcollectibles.domain.models

import com.dreamsoftware.artcollectibles.R
import java.util.*

data class NFT(
    val title: String,
    val subTitle: String,
    val image: Int,
    var likes: Int = 0,
    var eth: Double = 0.0,
    val id: UUID = UUID.randomUUID()
)

val nfts = listOf<NFT>(
    NFT("Wave", "wav2 #5672", R.drawable.card_wave2, 5160, 0.018),
    NFT("Abstract Pink", "abstract #2538", R.drawable.card_pink, 1800, 0.906),
    NFT("Wave", "wavepi #5267", R.drawable.card_wave, 2030, 0.26),
    NFT("Abstract23", "abstract #2038", R.drawable.card_abs23, 2060, 0.246),
    NFT("Music", "mali #7890", R.drawable.card_musicnft, 690, 0.03),
    NFT("Ball", "baalli #4890", R.drawable.card_ball, 1690, 0.46),
    NFT("Ring", "Ring #7288", R.drawable.card_ring, 400, 0.106),
    NFT("Beer", "beer #1238", R.drawable.card_beer, 200, 0.26)
)