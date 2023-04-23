package com.dreamsoftware.artcollectibles.data.api.mapper

import com.dreamsoftware.artcollectibles.data.firebase.model.NotificationDTO
import com.dreamsoftware.artcollectibles.domain.models.Notification
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class NotificationMapper: IOneSideMapper<NotificationMapper.InputData, Notification> {

    override fun mapInToOut(input: InputData): Notification = with(input) {
        with(notificationDTO) {
            Notification(
                uid = uid,
                title = title,
                description = description,
                user = userInfoDTO,
                tokenId = tokenId,
                createdAt = createdAt
            )
        }
    }

    override fun mapInListToOutList(input: Iterable<InputData>): Iterable<Notification> =
        input.map(::mapInToOut)

    data class InputData(
        val notificationDTO: NotificationDTO,
        val userInfoDTO: UserInfo
    )
}