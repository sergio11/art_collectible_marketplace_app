package com.dreamsoftware.artcollectibles.domain.di

import com.dreamsoftware.artcollectibles.data.api.repository.*
import com.dreamsoftware.artcollectibles.domain.usecase.impl.*
import com.dreamsoftware.artcollectibles.utils.AppEventBus
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
     * @param appEventBus
     */
    @Provides
    @ViewModelScoped
    fun provideSignInUseCase(
        userRepository: IUserRepository,
        secretRepository: ISecretRepository,
        preferenceRepository: IPreferenceRepository,
        applicationAware: IApplicationAware,
        appEventBus: AppEventBus
    ) = SignInUseCase(
        userRepository,
        secretRepository,
        preferenceRepository,
        applicationAware,
        appEventBus
    )

    /**
     * Provide Social Sign In Use Case
     * @param userRepository
     * @param walletRepository
     * @param secretRepository
     * @param preferenceRepository
     * @param applicationAware
     * @param appEventBus
     */
    @Provides
    @ViewModelScoped
    fun provideSocialSignInUseCase(
        userRepository: IUserRepository,
        walletRepository: IWalletRepository,
        secretRepository: ISecretRepository,
        preferenceRepository: IPreferenceRepository,
        applicationAware: IApplicationAware,
        appEventBus: AppEventBus
    ) = SocialSignInUseCase(
        userRepository,
        walletRepository,
        secretRepository,
        preferenceRepository,
        applicationAware,
        appEventBus
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
        applicationAware: IApplicationAware,
        appEventBus: AppEventBus
    ) = CloseSessionUseCase(
        userRepository,
        preferenceRepository,
        applicationAware,
        appEventBus
    )

    /**
     * Provide get auth user profile use case
     * @param userRepository
     * @param preferenceRepository
     */
    @Provides
    @ViewModelScoped
    fun provideGetAuthUserProfileUseCase(
        userRepository: IUserRepository,
        preferenceRepository: IPreferenceRepository
    ) =
        GetAuthUserProfileUseCase(userRepository, preferenceRepository)

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
     * @param walletRepository
     */
    @Provides
    @ViewModelScoped
    fun provideCreateArtCollectibleUseCase(
        artCollectibleRepository: IArtCollectibleRepository,
        walletRepository: IWalletRepository
    ) = CreateArtCollectibleUseCase(artCollectibleRepository, walletRepository)

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

    /**
     * Provide Fetch Marketplace statistics use case
     * @param artMarketplaceRepository
     */
    @Provides
    @ViewModelScoped
    fun provideFetchMarketplaceStatisticsUseCase(
        artMarketplaceRepository: IArtMarketplaceRepository
    ) = FetchMarketplaceStatisticsUseCase(artMarketplaceRepository)

    /**
     * Provide Fetch Market History use case
     * @param artMarketplaceRepository
     */
    @Provides
    @ViewModelScoped
    fun provideFetchMarketHistoryUseCase(
        artMarketplaceRepository: IArtMarketplaceRepository
    ) = FetchMarketHistoryUseCase(artMarketplaceRepository)

    /**
     * Provide Fetch item for sale
     * @param artMarketplaceRepository
     */
    @Provides
    @ViewModelScoped
    fun provideFetchItemForSaleUseCase(
        artMarketplaceRepository: IArtMarketplaceRepository
    ) = FetchItemForSaleUseCase(artMarketplaceRepository)

    /**
     * Provide Buy Item use case
     * @param artMarketplaceRepository
     */
    @Provides
    @ViewModelScoped
    fun provideBuyItemUseCase(
        artMarketplaceRepository: IArtMarketplaceRepository
    ) = BuyItemUseCase(artMarketplaceRepository)

    /**
     * Provide Add Token to favorites Use case
     * @param favoritesRepository
     */
    @Provides
    @ViewModelScoped
    fun provideAddTokenToFavoritesUseCase(
        favoritesRepository: IFavoritesRepository
    ) = AddTokenToFavoritesUseCase(favoritesRepository)

    /**
     * Provide remove token from favorites use case
     * @param favoritesRepository
     */
    @Provides
    @ViewModelScoped
    fun provideRemoveTokenFromFavoritesUseCase(
        favoritesRepository: IFavoritesRepository
    ) = RemoveTokenFromFavoritesUseCase(favoritesRepository)

    /**
     * Provide Register Visitor Use case
     * @param visitorsRepository
     */
    @Provides
    @ViewModelScoped
    fun provideRegisterVisitorUseCase(
        visitorsRepository: IVisitorsRepository
    ) = RegisterVisitorUseCase(visitorsRepository)

    /**
     * Provide Follow use case
     * @param userRepository
     */
    @Provides
    @ViewModelScoped
    fun provideFollowUseCase(
        userRepository: IUserRepository
    ) = FollowUserUseCase(userRepository)

    /**
     * Provide Unfollow use case
     * @param userRepository
     */
    @Provides
    @ViewModelScoped
    fun provideUnfollowUseCase(
        userRepository: IUserRepository
    ) = UnfollowUserUseCase(userRepository)

    /**
     * Provide Check Auth user is following to use case
     * @param userRepository
     */
    @Provides
    @ViewModelScoped
    fun provideCheckAuthUserIsFollowingToUseCase(
        userRepository: IUserRepository
    ) = CheckAuthUserIsFollowingToUseCase(userRepository)
}