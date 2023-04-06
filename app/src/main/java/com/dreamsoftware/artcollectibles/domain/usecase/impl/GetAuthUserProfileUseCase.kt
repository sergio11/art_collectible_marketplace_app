package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IPreferenceRepository
import com.dreamsoftware.artcollectibles.data.api.repository.IUserRepository
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCase

class GetAuthUserProfileUseCase(
    private val userRepository: IUserRepository,
    private val preferencesRepository: IPreferenceRepository
): BaseUseCase<UserInfo>() {

    override suspend fun onExecuted(): UserInfo =
        userRepository.get(uid = preferencesRepository.getAuthUserUid(), false)

}