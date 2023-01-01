package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IUserRepository
import com.dreamsoftware.artcollectibles.domain.models.AuthRequest
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams

/**
 * SignIn Use Case
 * @param userRepository
 */
class SignInUseCase(
    private val userRepository: IUserRepository
) : BaseUseCaseWithParams<SignInUseCase.Params, UserInfo>() {

    override suspend fun onExecuted(params: Params): UserInfo = with(params) {
        val authUser = userRepository.signIn(AuthRequest(email, password))
        userRepository.get(uid = authUser.uid)
    }

    data class Params(
        val email: String,
        val password: String
    )
}