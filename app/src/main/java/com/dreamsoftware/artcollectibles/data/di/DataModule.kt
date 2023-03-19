package com.dreamsoftware.artcollectibles.data.di

import com.dreamsoftware.artcollectibles.data.api.mapper.*
import com.dreamsoftware.artcollectibles.data.api.repository.*
import com.dreamsoftware.artcollectibles.data.api.repository.impl.*
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.*
import com.dreamsoftware.artcollectibles.data.blockchain.di.BlockchainModule
import com.dreamsoftware.artcollectibles.data.blockchain.mapper.WalletStatisticsMapper
import com.dreamsoftware.artcollectibles.data.database.datasource.metadata.ITokenMetadataDatabaseDataSource
import com.dreamsoftware.artcollectibles.data.firebase.datasource.*
import com.dreamsoftware.artcollectibles.data.firebase.di.FirebaseModule
import com.dreamsoftware.artcollectibles.data.ipfs.datasource.IpfsDataSource
import com.dreamsoftware.artcollectibles.data.ipfs.di.IPFSModule
import com.dreamsoftware.artcollectibles.data.memory.datasource.IArtCollectibleMemoryCacheDataSource
import com.dreamsoftware.artcollectibles.data.memory.datasource.IWalletMetadataMemoryDataSource
import com.dreamsoftware.artcollectibles.data.memory.di.MemoryModule
import com.dreamsoftware.artcollectibles.data.preferences.datasource.IPreferencesDataSource
import com.dreamsoftware.artcollectibles.data.preferences.di.PreferencesModule
import com.dreamsoftware.artcollectibles.utils.PasswordUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(includes = [FirebaseModule::class, BlockchainModule::class, IPFSModule::class, PreferencesModule::class, MemoryModule::class])
@InstallIn(SingletonComponent::class)
class DataModule {

    /**
     * Provide Account Balance Mapper
     */
    @Provides
    @Singleton
    fun provideAccountBalanceMapper(): AccountBalanceMapper = AccountBalanceMapper()

    /**
     * Provide Auth User Mapper
     */
    @Provides
    @Singleton
    fun provideAuthUserMapper(): AuthUserMapper = AuthUserMapper()

    /**
     * Provide User Info Mapper
     */
    @Provides
    @Singleton
    fun provideUserInfoMapper(): UserInfoMapper = UserInfoMapper()

    /**
     * Provide Create User Info Mapper
     */
    @Provides
    @Singleton
    fun provideCreateUserInfoMapper(): SaveUserInfoMapper = SaveUserInfoMapper()

    /**
     * Provide Art Collectible Mapper
     */
    @Provides
    @Singleton
    fun provideArtCollectibleMapper(): ArtCollectibleMapper =
        ArtCollectibleMapper()

    /**
     * Provide User Credentials Mapper
     */
    @Provides
    @Singleton
    fun provideUserCredentialsMapper(): UserCredentialsMapper = UserCredentialsMapper()

    /**
     * Provide Secret Mapper
     */
    @Provides
    @Singleton
    fun provideSecretMapper(): PBEDataMapper = PBEDataMapper()

    /**
     * Provide Marketplace Statistics Mapper
     */
    @Provides
    @Singleton
    fun provideMarketplaceStatisticsMapper(): MarketplaceStatisticsMapper = MarketplaceStatisticsMapper()

    /**
     * Provide Create Art Collectible metadata mapper
     */
    @Provides
    @Singleton
    fun provideCreateArtCollectibleMetadataMapper(): CreateArtCollectibleMetadataMapper = CreateArtCollectibleMetadataMapper()

    @Provides
    @Singleton
    fun provideTokenMetadata2Mapper(): TokenMetadataToEntityMapper = TokenMetadataToEntityMapper()

    @Provides
    @Singleton
    fun provideWalletStatisticsMapper(): WalletStatisticsMapper = WalletStatisticsMapper()

    @Provides
    @Singleton
    fun provideArtCollectibleCategoryMapper(): ArtCollectibleCategoryMapper = ArtCollectibleCategoryMapper()

    /**
     * Provide Token Metadata Mapper
     */
    @Provides
    @Singleton
    fun provideTokenMetadataMapper(artCollectibleCategoryMapper: ArtCollectibleCategoryMapper): TokenMetadataMapper = TokenMetadataMapper(artCollectibleCategoryMapper)

    @Provides
    @Singleton
    fun provideTokenMetadataEntityMapper(artCollectibleCategoryMapper: ArtCollectibleCategoryMapper): TokenMetadataEntityMapper = TokenMetadataEntityMapper(artCollectibleCategoryMapper)

    @Provides
    @Singleton
    fun provideCommentMapper(): CommentMapper = CommentMapper()

    @Provides
    @Singleton
    fun provideSaveCommentMapper(): SaveCommentMapper = SaveCommentMapper()

    /**
     * Provide Art Collectibles Repository
     * @param artCollectibleDataSource
     * @param userRepository
     * @param artCollectibleMapper
     * @param walletRepository
     * @param userCredentialsMapper
     * @param favoritesDataSource
     * @param visitorsDataSource
     * @param tokenMetadataRepository
     * @param artCollectibleMemoryCacheDataSource
     * @param commentsDataSource
     */
    @Provides
    @Singleton
    fun provideArtCollectiblesRepository(
        artCollectibleDataSource: IArtCollectibleBlockchainDataSource,
        userRepository: IUserRepository,
        artCollectibleMapper: ArtCollectibleMapper,
        walletRepository: IWalletRepository,
        userCredentialsMapper: UserCredentialsMapper,
        favoritesDataSource: IFavoritesDataSource,
        visitorsDataSource: IVisitorsDataSource,
        tokenMetadataRepository: ITokenMetadataRepository,
        artCollectibleMemoryCacheDataSource: IArtCollectibleMemoryCacheDataSource,
        commentsDataSource: ICommentsDataSource
    ): IArtCollectibleRepository =
        ArtCollectibleRepositoryImpl(
            artCollectibleDataSource,
            userRepository,
            artCollectibleMapper,
            walletRepository,
            userCredentialsMapper,
            favoritesDataSource,
            visitorsDataSource,
            tokenMetadataRepository,
            artCollectibleMemoryCacheDataSource,
            commentsDataSource
        )


    /**
     * Provide Art Marketplace Repository
     * @param artMarketplaceBlockchainDataSource
     * @param artCollectibleRepository
     * @param userRepository
     * @param walletRepository
     * @param userCredentialsMapper
     * @param marketplaceStatisticsMapper
     */
    @Provides
    @Singleton
    fun provideArtMarketplaceRepository(
        artMarketplaceBlockchainDataSource: IArtMarketplaceBlockchainDataSource,
        artCollectibleRepository: IArtCollectibleRepository,
        userRepository: IUserRepository,
        walletRepository: IWalletRepository,
        userCredentialsMapper: UserCredentialsMapper,
        marketplaceStatisticsMapper: MarketplaceStatisticsMapper
    ): IArtMarketplaceRepository =
        ArtMarketplaceRepositoryImpl(
            artMarketplaceBlockchainDataSource,
            artCollectibleRepository,
            userRepository,
            walletRepository,
            userCredentialsMapper,
            marketplaceStatisticsMapper
        )

    /**
     * Provide User Repository
     * @param authDataSource
     * @param userDataSource
     * @param storageDataSource
     * @param followerDataSource
     * @param userInfoMapper
     * @param saveUserInfoMapper
     * @param authUserMapper
     */
    @Provides
    @Singleton
    fun provideUserRepository(
        authDataSource: IAuthDataSource,
        userDataSource: IUsersDataSource,
        storageDataSource: IStorageDataSource,
        followerDataSource: IFollowersDataSource,
        preferencesDataSource: IPreferencesDataSource,
        userInfoMapper: UserInfoMapper,
        saveUserInfoMapper: SaveUserInfoMapper,
        authUserMapper: AuthUserMapper
    ): IUserRepository =
        UserRepositoryImpl(
            authDataSource,
            userDataSource,
            storageDataSource,
            followerDataSource,
            preferencesDataSource,
            userInfoMapper,
            saveUserInfoMapper,
            authUserMapper
        )

    /**
     * Provide Wallet Repository
     * @param accountBalanceMapper
     * @param accountBlockchainDataSource
     * @param userCredentialsMapper
     * @param preferencesDataSource
     * @param secretDataSource
     * @param passwordUtils
     * @param walletDataSource
     * @param faucetBlockchainDataSource
     * @param walletMetadataMemoryCache
     */
    @Provides
    @Singleton
    fun provideWalletRepository(
        accountBalanceMapper: AccountBalanceMapper,
        accountBlockchainDataSource: IAccountBlockchainDataSource,
        userCredentialsMapper: UserCredentialsMapper,
        preferencesDataSource: IPreferencesDataSource,
        storageDataSource: IStorageDataSource,
        secretDataSource: IWalletMetadataDataSource,
        passwordUtils: PasswordUtils,
        walletDataSource: IWalletDataSource,
        faucetBlockchainDataSource: IFaucetBlockchainDataSource,
        walletMetadataMemoryCache: IWalletMetadataMemoryDataSource
    ): IWalletRepository =
        WalletRepositoryImpl(
            accountBalanceMapper,
            accountBlockchainDataSource,
            userCredentialsMapper,
            preferencesDataSource,
            secretDataSource,
            storageDataSource,
            passwordUtils,
            walletDataSource,
            faucetBlockchainDataSource,
            walletMetadataMemoryCache
        )

    /**
     * Provide Secret Repository
     * @param passwordUtils
     * @param secretDataSource
     * @param PBEDataMapper
     */
    @Provides
    @Singleton
    fun provideSecretRepository(
        passwordUtils: PasswordUtils,
        secretDataSource: ISecretDataSource,
        PBEDataMapper: PBEDataMapper
    ): ISecretRepository =
        SecretRepositoryImpl(
            secretDataSource,
            passwordUtils,
            PBEDataMapper
        )

    /**
     * Provide Preference Repository
     * @param preferenceDataSource
     */
    @Provides
    @Singleton
    fun providePreferenceRepository(
        preferenceDataSource: IPreferencesDataSource
    ): IPreferenceRepository =
        PreferenceRepositoryImpl(preferenceDataSource)

    /**
     * Provide Token Metadata repository
     * @param ipfsDataSource
     * @param tokenMetadataDatabaseDataSource
     * @param createArtCollectibleMetadataMapper
     * @param tokenMetadataMapper
     * @param tokenMetadataToEntityMapper
     * @param tokenMetadataEntityMapper
     * @param categoriesDataSource
     */
    @Provides
    @Singleton
    fun provideTokenMetadataRepository(
        ipfsDataSource: IpfsDataSource,
        tokenMetadataDatabaseDataSource: ITokenMetadataDatabaseDataSource,
        createArtCollectibleMetadataMapper: CreateArtCollectibleMetadataMapper,
        tokenMetadataMapper: TokenMetadataMapper,
        tokenMetadataToEntityMapper: TokenMetadataToEntityMapper,
        tokenMetadataEntityMapper: TokenMetadataEntityMapper,
        categoriesDataSource: ICategoriesDataSource
    ): ITokenMetadataRepository = TokenMetadataRepositoryImpl(
        ipfsDataSource,
        tokenMetadataDatabaseDataSource,
        createArtCollectibleMetadataMapper,
        tokenMetadataMapper,
        tokenMetadataToEntityMapper,
        tokenMetadataEntityMapper,
        categoriesDataSource
    )

    /**
     * Provide Favorites Repository
     * @param favoritesDataSource
     * @param artCollectibleRepository
     * @param walletRepository
     * @param userRepository
     */
    @Provides
    @Singleton
    fun provideFavoritesRepository(
        favoritesDataSource: IFavoritesDataSource,
        artCollectibleRepository: IArtCollectibleRepository,
        walletRepository: IWalletRepository,
        userRepository: IUserRepository
    ): IFavoritesRepository = FavoritesRepositoryImpl(favoritesDataSource, artCollectibleRepository, walletRepository, userRepository)

    /**
     * Provide Visitors Repository
     * @param visitorsDataSource
     * @param userRepository
     */
    @Provides
    @Singleton
    fun provideVisitorsRepository(
        visitorsDataSource: IVisitorsDataSource,
        userRepository: IUserRepository
    ): IVisitorsRepository = VisitorsRepositoryImpl(visitorsDataSource, userRepository)

    /**
     * Provide Art Collectible Category Repository
     * @param categoriesDataSource
     * @param artCollectibleCategoryMapper
     */
    @Provides
    @Singleton
    fun provideArtCollectibleCategoryRepository(
        categoriesDataSource: ICategoriesDataSource,
        artCollectibleCategoryMapper: ArtCollectibleCategoryMapper
    ): IArtCollectibleCategoryRepository = ArtCollectibleCategoryRepositoryImpl(categoriesDataSource, artCollectibleCategoryMapper)

    /**
     * Provide Comments Repository
     * @param commentsDataSource
     * @param commentMapper
     * @param userRepository
     * @param saveCommentMapper
     */
    @Provides
    @Singleton
    fun provideCommentsRepository(
        commentsDataSource: ICommentsDataSource,
        commentMapper: CommentMapper,
        userRepository: IUserRepository,
        saveCommentMapper: SaveCommentMapper
    ): ICommentsRepository = CommentsRepositoryImpl(commentsDataSource, commentMapper, userRepository, saveCommentMapper)
}
