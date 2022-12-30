package com.dreamsoftware.artcollectibles.data.firebase.mapper

import com.dreamsoftware.artcollectibles.data.firebase.model.AuthUserDTO
import com.dreamsoftware.artcollectibles.domain.models.AuthTypeEnum
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper
import com.google.firebase.auth.FirebaseUser

class FirebaseUserMapper : IOneSideMapper<Triple<FirebaseUser, String, AuthTypeEnum>, AuthUserDTO> {

    override fun mapInToOut(input: Triple<FirebaseUser, String, AuthTypeEnum>): AuthUserDTO = with(input) {
        AuthUserDTO(
            uid = first.uid,
            displayName = first.displayName,
            email = first.email,
            photoUrl = if(third == AuthTypeEnum.FACEBOOK) {
                "${first.photoUrl}?width=400&height=400&access_token=${second}"
            } else {
                first.photoUrl.toString()
            }
        )
    }

    override fun mapInListToOutList(input: Iterable<Triple<FirebaseUser, String, AuthTypeEnum>>): Iterable<AuthUserDTO> =
        input.map(::mapInToOut)
}