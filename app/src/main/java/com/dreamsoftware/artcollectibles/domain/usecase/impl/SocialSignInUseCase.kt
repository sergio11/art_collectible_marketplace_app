package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.exception.UserDataException
import com.dreamsoftware.artcollectibles.data.api.repository.IPreferenceRepository
import com.dreamsoftware.artcollectibles.data.api.repository.ISecretRepository
import com.dreamsoftware.artcollectibles.data.api.repository.IUserRepository
import com.dreamsoftware.artcollectibles.data.api.repository.IWalletRepository
import com.dreamsoftware.artcollectibles.data.firebase.exception.UserNotFoundException
import com.dreamsoftware.artcollectibles.domain.models.AuthUser
import com.dreamsoftware.artcollectibles.domain.models.ExternalProviderAuthRequest
import com.dreamsoftware.artcollectibles.domain.models.ExternalProviderAuthTypeEnum
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams
import com.dreamsoftware.artcollectibles.utils.AppEventBus
import com.dreamsoftware.artcollectibles.utils.IApplicationAware

/**
 * Social SignIn Use Case
 * @param userRepository
 * @param walletRepository
 * @param secretRepository
 * @param preferenceRepository
 * @param applicationAware
 * @param appEventBus
 */
class SocialSignInUseCase(
    private val userRepository: IUserRepository,
    private val walletRepository: IWalletRepository,
    private val secretRepository: ISecretRepository,
    private val preferenceRepository: IPreferenceRepository,
    private val applicationAware: IApplicationAware,
    private val appEventBus: AppEventBus
) : BaseUseCaseWithParams<SocialSignInUseCase.Params, UserInfo>() {

    override suspend fun onExecuted(params: Params): UserInfo = with(params) {
        val authUser = userRepository.signIn(ExternalProviderAuthRequest(accessToken, externalProviderAuthTypeEnum))
        configureUserSecretKey(uid = authUser.uid)
        preferenceRepository.saveAuthUserUid(uid = authUser.uid)
        try {
            finishSignIn(authUser.uid)
        } catch (ex: UserDataException) {
            if (ex.cause is UserNotFoundException) {
                createUser(authUser, params.externalProviderAuthTypeEnum)
            } else {
                throw ex
            }
        }
    }

    override suspend fun onReverted(params: Params) {
        userRepository.closeSession()
        preferenceRepository.clearData()
        applicationAware.setUserSecret(null)
    }

    /**
     * Private Methods
     */

    private suspend fun createUser(authUser: AuthUser, externalProviderAuth: ExternalProviderAuthTypeEnum): UserInfo = with(authUser) {
        val wallet = walletRepository.generate(userUid = uid)
        userRepository.save(
            UserInfo(
                uid = uid,
                name = displayName,
                contact = email,
                photoUrl = photoUrl,
                walletAddress = wallet.address,
                externalProviderAuthType = externalProviderAuth
            )
        )
        finishSignIn(uid)
    }

    private suspend fun configureUserSecretKey(uid: String) {
        val secret = runCatching { secretRepository.get(uid) }
            .getOrElse { secretRepository.generate(uid) }
        applicationAware.setUserSecret(secret)
    }

    private suspend fun finishSignIn(uid: String): UserInfo =
        userRepository.get(uid).also {
            appEventBus.invokeEvent(AppEventBus.AppEvent.SIGN_IN)
        }

    data class Params(
        val accessToken: String,
        val externalProviderAuthTypeEnum: ExternalProviderAuthTypeEnum
    )
}