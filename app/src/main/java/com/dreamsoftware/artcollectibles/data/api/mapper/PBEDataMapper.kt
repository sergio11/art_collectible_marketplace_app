package com.dreamsoftware.artcollectibles.data.api.mapper

import com.dreamsoftware.artcollectibles.data.firebase.model.SecretDTO
import com.dreamsoftware.artcollectibles.domain.models.PBEData
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class PBEDataMapper: IOneSideMapper<SecretDTO, PBEData> {

    override fun mapInToOut(input: SecretDTO): PBEData = with(input) {
        PBEData(secret, salt)
    }

    override fun mapInListToOutList(input: Iterable<SecretDTO>): Iterable<PBEData> =
        input.map(::mapInToOut)
}