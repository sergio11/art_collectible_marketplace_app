package com.dreamsoftware.artcollectibles.data.api.mapper

import com.dreamsoftware.artcollectibles.data.firebase.model.SaveNotificationDTO
import com.dreamsoftware.artcollectibles.domain.models.CreateNotification
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class SaveNotificationMapper: IOneSideMapper<CreateNotification, SaveNotificationDTO> {

    override fun mapInToOut(input: CreateNotification): SaveNotificationDTO = with(input) {
        SaveNotificationDTO(
            uid = uid,
            title = title,
            description = description,
            userUid = userUid,
            tokenId = tokenId
        )
    }

    override fun mapInListToOutList(input: Iterable<CreateNotification>): Iterable<SaveNotificationDTO> =
        input.map(::mapInToOut)
}