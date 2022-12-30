package com.dreamsoftware.artcollectibles.data.api.mapper

import com.dreamsoftware.artcollectibles.data.firebase.model.AuthUserDTO
import com.dreamsoftware.artcollectibles.domain.models.AuthUser
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class AuthUserMapper: IOneSideMapper<AuthUserDTO, AuthUser> {

    override fun mapInToOut(input: AuthUserDTO): AuthUser = with(input) {
        AuthUser(
            uid = uid,
            displayName = displayName.orEmpty(),
            email = email.orEmpty(),
            photoUrl = photoUrl.orEmpty()
        )
    }

    override fun mapInListToOutList(input: Iterable<AuthUserDTO>): Iterable<AuthUser> =
        input.map(::mapInToOut)
}