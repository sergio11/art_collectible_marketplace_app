package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IUserRepository
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams

class UpdateUserInfoUseCase(
    private val userRepository: IUserRepository
): BaseUseCaseWithParams<UpdateUserInfoUseCase.Params, UserInfo>() {


    override suspend fun onExecuted(params: Params): UserInfo = with(params) {
        with(userInfo) {
            val profilePhotoUrl = if(isProfilePictureUpdated) {
                photoUrl?.let {
                    userRepository.updateProfilePicture(
                        uid = uid,
                        fileUri = it
                    )
                }
            } else {
                photoUrl
            }
            userRepository.save(copy(photoUrl = profilePhotoUrl))
            userRepository.get(uid = uid, fullDetail = false)
        }
    }

    data class Params(
        val userInfo: UserInfo,
        val isProfilePictureUpdated: Boolean
    )
}