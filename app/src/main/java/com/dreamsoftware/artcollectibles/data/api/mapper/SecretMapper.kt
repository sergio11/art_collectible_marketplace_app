package com.dreamsoftware.artcollectibles.data.api.mapper

import com.dreamsoftware.artcollectibles.data.firebase.model.SecretDTO
import com.dreamsoftware.artcollectibles.domain.models.Secret
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class SecretMapper: IOneSideMapper<SecretDTO, Secret> {

    override fun mapInToOut(input: SecretDTO): Secret = with(input) {
        Secret(
            userUid, secret, salt
        )
    }

    override fun mapInListToOutList(input: Iterable<SecretDTO>): Iterable<Secret> =
        input.map(::mapInToOut)
}