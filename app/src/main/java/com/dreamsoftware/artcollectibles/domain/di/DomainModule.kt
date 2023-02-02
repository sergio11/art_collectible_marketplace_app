package com.dreamsoftware.artcollectibles.domain.di

import com.dreamsoftware.artcollectibles.data.api.repository.*
import com.dreamsoftware.artcollectibles.domain.usecase.impl.*
import com.dreamsoftware.artcollectibles.utils.IApplicationAware
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
        GetMyTokensCreatedUseCase(artCollectibleRepository)

    /**
     * @param artCollectibleRepository
     */
    @Provides
    @ViewModelScoped
    fun provideGetTokensOwnedUseCase(
        artCollectibleRepository: IArtCollectibleRepository
    ) =
        GetMyTokensOwnedUseCase(artCollectibleRepository)

    /**
     * Provide Sign In Use case
     * @param userRepository
     * @param secretRepository
     * @param preferenceRepository
     * @param applicationAware
     */
    @Provides
    @ViewModelScoped
    fun provideSignInUseCase(
        userRepository: IUserRepository,
        secretRepository: ISecretRepository,
        preferenceRepository: IPreferenceRepository,
        applicationAware: IApplicationAware
    ) = SignInUseCase(
        userRepository,
        secretRepository,
        preferenceRepository,
        applicationAware
    )

    /**
     * Provide Social Sign In Use Case
     * @param userRepository
     * @param walletRepository
     * @param secretRepository
     * @param preferenceRepository
     * @param applicationAware
     */
    @Provides
    @ViewModelScoped
    fun provideSocialSignInUseCase(
        userRepository: IUserRepository,
        walletRepository: IWalletRepository,
        secretRepository: ISecretRepository,
        preferenceRepository: IPreferenceRepository,
        applicationAware: IApplicationAware
    ) = SocialSignInUseCase(
        userRepository,
        walletRepository,
        secretRepository,
        preferenceRepository,
        applicationAware
    )

    /**
     * Provide Sign Up Use case
     * @param userRepository
     * @param walletRepository
     * @param secretRepository
     * @param preferenceRepository
     * @param applicationAware
     */
    @Provides
    @ViewModelScoped
    fun provideSignUpUseCase(
        userRepository: IUserRepository,
        walletRepository: IWalletRepository,
        secretRepository: ISecretRepository,
        preferenceRepository: IPreferenceRepository,
        applicationAware: IApplicationAware
    ) = SignUpUseCase(
        userRepository,
        walletRepository,
        secretRepository,
        preferenceRepository,
        applicationAware
    )

    /**
     * Provide Close session use case
     * @param userRepository
     * @param preferenceRepository
     * @param applicationAware
     */
    @Provides
    @ViewModelScoped
    fun provideCloseSessionUseCase(
        userRepository: IUserRepository,
        preferenceRepository: IPreferenceRepository,
        applicationAware: IApplicationAware
    ) = CloseSessionUseCase(
        userRepository,
        preferenceRepository,
        applicationAware
    )

    /**
     * Provide get user profile use case
     * @param userRepository
     * @param preferenceRepository
     */
    @Provides
    @ViewModelScoped
    fun provideGetUserProfileUseCase(
        userRepository: IUserRepository,
        preferenceRepository: IPreferenceRepository
    ) =
        GetUserProfileUseCase(userRepository, preferenceRepository)

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
     * @param preferenceRepository
     */
    @Provides
    @ViewModelScoped
    fun provideVerifyUserAuthenticatedUseCase(
        userRepository: IUserRepository,
        preferenceRepository: IPreferenceRepository
    ) = VerifyUserAuthenticatedUseCase(userRepository, preferenceRepository)

    /**
     * Provide Get Current Balance Use case
     * @param walletRepository
     */
    @Provides
    @ViewModelScoped
    fun provideGetCurrentBalanceUseCase(
        walletRepository: IWalletRepository
    ) = GetCurrentBalanceUseCase(walletRepository)

    /**
     * Provide Find All users use case
     * @param userRepository
     */
    @Provides
    @ViewModelScoped
    fun provideFindAllUsersUseCase(
        userRepository: IUserRepository
    ) = SearchUsersUseCase(userRepository)

    /**
     * Provide Create Art Collectible Use case
     * @param artCollectibleRepository
     */
    @Provides
    @ViewModelScoped
    fun provideCreateArtCollectibleUseCase(
        artCollectibleRepository: IArtCollectibleRepository
    ) = CreateArtCollectibleUseCase(artCollectibleRepository)

    /**
     * Provide Get token detail use case
     * @param artCollectibleRepository
     */
    @Provides
    @ViewModelScoped
    fun provideGetTokenDetailUseCase(
        artCollectibleRepository: IArtCollectibleRepository
    ) = GetTokenDetailUseCase(artCollectibleRepository)

    /**
     * Provide Burn Token Use Case
     * @param artCollectibleRepository
     */
    @Provides
    @ViewModelScoped
    fun provideBurnTokenUseCase(
        artCollectibleRepository: IArtCollectibleRepository
    ) = BurnTokenUseCase(artCollectibleRepository)

    /**
     * Provide Put item for sale use case
     * @param artMarketplaceRepository
     */
    @Provides
    @ViewModelScoped
    fun providePutItemForSaleUseCase(
        artMarketplaceRepository: IArtMarketplaceRepository
    ) = PutItemForSaleUseCase(artMarketplaceRepository)

    /**
     * Provide With draw from sale use case
     * @param artMarketplaceRepository
     */
    @Provides
    @ViewModelScoped
    fun provideWithdrawFromSaleUseCase(
        artMarketplaceRepository: IArtMarketplaceRepository
    ) = WithdrawFromSaleUseCase(artMarketplaceRepository)

    /**
     * Provide is token added for sale use case
     * @param artMarketplaceRepository
     */
    @Provides
    @ViewModelScoped
    fun provideIsTokenAddedForSaleUseCase(
        artMarketplaceRepository: IArtMarketplaceRepository
    ) = IsTokenAddedForSaleUseCase(artMarketplaceRepository)
}