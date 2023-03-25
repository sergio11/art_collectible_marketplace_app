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

    /**
     * Provide get followers use case
     * @param userRepository
     */
    @Provides
    @ViewModelScoped
    fun provideGetFollowersUseCase(
        userRepository: IUserRepository
    ) = GetFollowersUseCase(userRepository)

    /**
     * Provide get following use case
     * @param userRepository
     */
    @Provides
    @ViewModelScoped
    fun provideGetFollowingUseCase(
        userRepository: IUserRepository
    ) = GetFollowingUseCase(userRepository)

    /**
     * Provide Get tokens created by user
     * @param artCollectibleRepository
     */
    @Provides
    @ViewModelScoped
    fun provideGetTokensCreatedByUserUseCase(
        artCollectibleRepository: IArtCollectibleRepository
    ) = GetTokensCreatedByUserUseCase(artCollectibleRepository)

    /**
     * Provide Get tokens owned by user
     * @param artCollectibleRepository
     */
    @Provides
    @ViewModelScoped
    fun provideGetTokensOwnedByUserUseCase(
        artCollectibleRepository: IArtCollectibleRepository
    ) = GetTokensOwnedByUserUseCase(artCollectibleRepository)

    /**
     * Provide Get Art Collectible Categories Use case
     * @param artCollectibleCategoryRepository
     */
    @Provides
    @ViewModelScoped
    fun provideGetArtCollectibleCategoriesUseCase(
        artCollectibleCategoryRepository: IArtCollectibleCategoryRepository
    ) = GetArtCollectibleCategoriesUseCase(artCollectibleCategoryRepository)

    /**
     * Provide get category detail use case
     * @param artCollectibleCategoryRepository
     */
    @Provides
    @ViewModelScoped
    fun provideGetCategoryDetailUseCase(
        artCollectibleCategoryRepository: IArtCollectibleCategoryRepository
    ) = GetCategoryDetailUseCase(artCollectibleCategoryRepository)

    /**
     * Provide get available marker items by category use case
     */
    @Provides
    @ViewModelScoped
    fun provideGetAvailableMarketItemsByCategoryUseCase(
        artMarketplaceRepository: IArtMarketplaceRepository
    ) = GetAvailableMarketItemsByCategoryUseCase(artMarketplaceRepository)

    /**
     * Provide get more followed users
     */
    @Provides
    @ViewModelScoped
    fun provideGetMoreFollowedUsersUseCase(
        userRepository: IUserRepository
    ) = GetMoreFollowedUsersUseCase(userRepository)

    /**
     * Provide Get More liked tokens
     */
    @Provides
    @ViewModelScoped
    fun provideGetMoreLikedTokensUseCase(
        favoritesRepository: IFavoritesRepository
    ) = GetMoreLikedTokensUseCase(favoritesRepository)

    /**
     * Provide Get my favorite tokens use case
     */
    @Provides
    @ViewModelScoped
    fun provideGetMyFavoriteTokensUseCase(
        favoritesRepository: IFavoritesRepository
    ) = GetMyFavoriteTokensUseCase(favoritesRepository)

    /**
     * Provide Save Comments Use case
     * @param commentsRepository
     */
    @Provides
    @ViewModelScoped
    fun provideSaveCommentUseCase(commentsRepository: ICommentsRepository)
        = SaveCommentUseCase(commentsRepository)

    /**
     * Provide Get Comments by token use case
     * @param commentsRepository
     */
    @Provides
    @ViewModelScoped
    fun provideGetCommentsByTokenUseCase(commentsRepository: ICommentsRepository) =
        GetCommentsByTokenUseCase(commentsRepository)

    /**
     * Provide Delete Comment use case
     * @param commentsRepository
     */
    @Provides
    @ViewModelScoped
    fun provideDeleteCommentUseCase(commentsRepository: ICommentsRepository) =
        DeleteCommentUseCase(commentsRepository)

    /**
     * Provide Get user likes by token use case
     * @param favoritesRepository
     */
    @Provides
    @ViewModelScoped
    fun provideGetUserLikesByTokenUseCase(favoritesRepository: IFavoritesRepository) =
        GetUserLikesByTokenUseCase(favoritesRepository)

    /**
     * Provide Get Visitors By Token Use case
     * @param visitorsRepository
     */
    @Provides
    @ViewModelScoped
    fun provideGetVisitorsByTokenUseCase(visitorsRepository: IVisitorsRepository) =
        GetVisitorsByTokenUseCase(visitorsRepository)

    /**
     * Provide get last comments by token use case
     * @param commentsRepository
     */
    @Provides
    @ViewModelScoped
    fun provideGetLastCommentsByTokenUseCase(commentsRepository: ICommentsRepository) =
        GetLastCommentsByTokenUseCase(commentsRepository)


    /**
     * Provide get comment detail
     * @param commentsRepository
     */
    @Provides
    @ViewModelScoped
    fun provideGetCommentDetailUseCase(commentsRepository: ICommentsRepository) =
        GetCommentDetailUseCase(commentsRepository)

    /**
     * Provide Get token market history use case
     * @param artMarketplaceRepository
     */
    @Provides
    @ViewModelScoped
    fun provideGetTokenMarketHistoryUseCase(artMarketplaceRepository: IArtMarketplaceRepository) =
        GetTokenMarketHistoryUseCase(artMarketplaceRepository)

    /**
     * Provide Get last token market transactions use case
     * @param artMarketplaceRepository
     */
    @Provides
    @ViewModelScoped
    fun provideGetLastTokenMarketTransactionsUseCase(artMarketplaceRepository: IArtMarketplaceRepository) =
        GetLastTokenMarketTransactionsUseCase(artMarketplaceRepository)
}