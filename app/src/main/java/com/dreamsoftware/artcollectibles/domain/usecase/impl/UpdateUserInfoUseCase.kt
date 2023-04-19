package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IUserRepository
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams

class UpdateUserInfoUseCase(
    private val userRepository: IUserRepository
): BaseUseCaseWithParams<UpdateUserInfoUseCase.Params, UserInfo>() {


    override suspend fun onExecuted(params: Params): UserInfo = with(params) {
        with(userInfo) {
            with(userRepository) {
                val profilePhotoUrl = if(isProfilePictureUpdated) {
                    photoUrl?.let {
                        updateProfilePicture(uid = uid, fileUri = it)
                    }
                } else {
                    photoUrl
                }
                save(copy(photoUrl = profilePhotoUrl))
                get(uid = uid, fullDetail = false)
            }
        }
    }

    data class Params(
        val userInfo: UserInfo,
        val isProfilePictureUpdated: Boolean
    )
}