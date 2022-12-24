package com.dreamsoftware.artcollectibles.data.auth.mapper

import com.dreamsoftware.artcollectibles.domain.models.AuthUser
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper
import com.google.firebase.auth.FirebaseUser

class AuthUserMapper : IOneSideMapper<FirebaseUser, AuthUser> {

    override fun mapInToOut(input: FirebaseUser): AuthUser = with(input) {
        AuthUser(
            uid = uid,
            displayName = displayName.orEmpty(),
            email = email.orEmpty()
        )
    }

    override fun mapInListToOutList(input: Iterable<FirebaseUser>): Iterable<AuthUser> =
        input.map(::mapInToOut)
}