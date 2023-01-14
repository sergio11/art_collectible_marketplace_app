package com.dreamsoftware.artcollectibles.data.firebase.mapper

import com.dreamsoftware.artcollectibles.data.firebase.model.AuthUserDTO
import com.dreamsoftware.artcollectibles.domain.models.ExternalProviderAuthTypeEnum
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper
import com.google.firebase.auth.FirebaseUser

class ExternalUserAuthenticatedMapper : IOneSideMapper<Triple<FirebaseUser, String, ExternalProviderAuthTypeEnum>, AuthUserDTO> {

    override fun mapInToOut(input: Triple<FirebaseUser, String, ExternalProviderAuthTypeEnum>): AuthUserDTO = with(input) {
        AuthUserDTO(
            uid = first.uid,
            displayName = first.displayName,
            email = first.email,
            photoUrl = if(third == ExternalProviderAuthTypeEnum.FACEBOOK) {
                "${first.photoUrl}?width=400&height=400&access_token=${second}"
            } else {
                first.photoUrl.toString()
            }
        )
    }

    override fun mapInListToOutList(input: Iterable<Triple<FirebaseUser, String, ExternalProviderAuthTypeEnum>>): Iterable<AuthUserDTO> =
        input.map(::mapInToOut)
}