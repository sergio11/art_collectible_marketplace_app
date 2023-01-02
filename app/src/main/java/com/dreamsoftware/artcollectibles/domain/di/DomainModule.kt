package com.dreamsoftware.artcollectibles.domain.di

import com.dreamsoftware.artcollectibles.data.api.repository.*
import com.dreamsoftware.artcollectibles.domain.usecase.impl.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    /**
     * @param artMarketplaceRepository
     */
    @Provides
    @ViewModelScoped
    fun provideFetchAvailableMarketItemsUseCase(
        artMarketplaceRepository: IArtMarketplaceRepository
    ) =
        FetchAvailableMarketItemsUseCase(artMarketplaceRepository)

    /**
     * @param artMarketplaceRepository
     */
    @Provides
    @ViewModelScoped
    fun provideFetchSellingMarketItemsUseCase(
        artMarketplaceRepository: IArtMarketplaceRepository
    ) =
        FetchSellingMarketItemsUseCase(artMarketplaceRepository)

    /**
     * @param artCollectibleRepository
     */
    @Provides
    @ViewModelScoped
    fun provideGetTokensCreatedUseCase(
        artCollectibleRepository: IArtCollectibleRepository
    ) =
        GetTokensCreatedUseCase(artCollectibleRepository)

    /**
     * @param artCollectibleRepository
     */
    @Provides
    @ViewModelScoped
    fun provideGetTokensOwnedUseCase(
        artCollectibleRepository: IArtCollectibleRepository
    ) =
        GetTokensOwnedUseCase(artCollectibleRepository)

    /**
     * Provide Sign In Use case
     * @param userRepository
     */
    @Provides
    @ViewModelScoped
    fun provideSignInUseCase(
        userRepository: IUserRepository
    ) = SignInUseCase(userRepository)

    /**
     * Provide Social Sign In Use Case
     * @param userRepository
     */
    @Provides
    @ViewModelScoped
    fun provideSocialSignInUseCase(
        userRepository: IUserRepository,
        walletRepository: IWalletRepository
    ) = SocialSignInUseCase(userRepository, walletRepository)

    /**
     * Provide Sign Up Use case
     * @param userRepository
     * @param walletRepository
     */
    @Provides
    @ViewModelScoped
    fun provideSignUpUseCase(
        userRepository: IUserRepository,
        walletRepository: IWalletRepository
    ) = SignUpUseCase(userRepository, walletRepository)

    /**
     * Provide Close session use case
     * @param userRepository
     */
    @Provides
    @ViewModelScoped
    fun provideCloseSessionUseCase(
        userRepository: IUserRepository
    ) = CloseSessionUseCase(userRepository)

    /**
     * Provide get user profile use case
     * @param userRepository
     */
    @Provides
    @ViewModelScoped
    fun provideGetUserProfileUseCase(
        userRepository: IUserRepository
    ) =
        GetUserProfileUseCase(userRepository)

    /**
     * Provide Update User info use case
     * @param userRepository
     */
    @Provides
    @ViewModelScoped
    fun provideUpdateUserInfoUseCase(
        userRepository: IUserRepository
    ) =
        UpdateUserInfoUseCase(userRepository)

    /**
     * Provide Verify User Authenticated Use case
     * @param userRepository
     */
    @Provides
    @ViewModelScoped
    fun provideVerifyUserAuthenticatedUseCase(
        userRepository: IUserRepository
    ) = VerifyUserAuthenticatedUseCase(userRepository)
}