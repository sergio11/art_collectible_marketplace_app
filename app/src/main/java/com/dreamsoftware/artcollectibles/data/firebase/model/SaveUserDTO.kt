package com.dreamsoftware.artcollectibles.data.firebase.model

data class SaveUserDTO(
    val uid: String,
    val name: String,
    val walletAddress: String,
    val professionalTitle: String? = null,
    val info: String? = null,
    val tags: List<String>? = null,
    val contact: String? = null,
    val photoUrl: String? = null,
    val birthdate: String? = null,
    val externalAuthProvider: String? = null,
    val location: String? = null,
    val country: String? = null,
    val instagramNick: String? = null,
    val isPublicProfile: Boolean? = null,
    val showSellingTokensRow: Boolean? = null,
    val showLastTransactionsOfTokens: Boolean? = null,
    val allowPublishComments: Boolean? = null,
    val showAccountBalance: Boolean? = null
)
