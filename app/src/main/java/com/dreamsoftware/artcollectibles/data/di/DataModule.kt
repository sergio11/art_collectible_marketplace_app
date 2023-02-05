package com.dreamsoftware.artcollectibles.data.di

import com.dreamsoftware.artcollectibles.data.api.mapper.*
import com.dreamsoftware.artcollectibles.data.api.repository.*
import com.dreamsoftware.artcollectibles.data.api.repository.impl.*
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.*
import com.dreamsoftware.artcollectibles.data.blockchain.di.BlockchainModule
import com.dreamsoftware.artcollectibles.data.firebase.datasource.*
import com.dreamsoftware.artcollectibles.data.firebase.di.FirebaseModule
import com.dreamsoftware.artcollectibles.data.ipfs.datasource.IpfsDataSource
import com.dreamsoftware.artcollectibles.data.ipfs.di.IPFSModule
import com.dreamsoftware.artcollectibles.data.preferences.datasource.IPreferencesDataSource
import com.dreamsoftware.artcollectibles.data.preferences.di.PreferencesModule
import com.dreamsoftware.artcollectibles.utils.PasswordUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(includes = [FirebaseModule::class, BlockchainModule::class, IPFSModule::class, PreferencesModule::class])
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
    fun provideArtCollectibleMapper(userInfoMapper: UserInfoMapper): ArtCollectibleMapper =
        ArtCollectibleMapper(userInfoMapper)

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
     * Provide Art Collectibles Repository
     * @param artCollectibleDataSource
     * @param ipfsDataSource
     * @param userCredentialsMapper
     */
    @Provides
    @Singleton
    fun provideArtCollectiblesRepository(
        artCollectibleDataSource: IArtCollectibleBlockchainDataSource,
        ipfsDataSource: IpfsDataSource,
        userDataSource: IUsersDataSource,
        artCollectibleMapper: ArtCollectibleMapper,
        walletRepository: IWalletRepository,
        userCredentialsMapper: UserCredentialsMapper
    ): IArtCollectibleRepository =
        ArtCollectibleRepositoryImpl(
            artCollectibleDataSource,
            ipfsDataSource,
            userDataSource,
            artCollectibleMapper,
            walletRepository,
            userCredentialsMapper
        )


    /**
     * Provide Art Marketplace Repository
     * @param artMarketplaceBlockchainDataSource
     * @param artCollectibleRepository
     * @param userDataSource
     * @param userInfoMapper
     * @param walletRepository
     * @param userCredentialsMapper
     * @param marketplaceStatisticsMapper
     */
    @Provides
    @Singleton
    fun provideArtMarketplaceRepository(
        artMarketplaceBlockchainDataSource: IArtMarketplaceBlockchainDataSource,
        artCollectibleRepository: IArtCollectibleRepository,
        userDataSource: IUsersDataSource,
        userInfoMapper: UserInfoMapper,
        walletRepository: IWalletRepository,
        userCredentialsMapper: UserCredentialsMapper,
        marketplaceStatisticsMapper: MarketplaceStatisticsMapper
    ): IArtMarketplaceRepository =
        ArtMarketplaceRepositoryImpl(
            artMarketplaceBlockchainDataSource,
            artCollectibleRepository,
            userDataSource,
            userInfoMapper,
            walletRepository,
            userCredentialsMapper,
            marketplaceStatisticsMapper
        )

    /**
     * Provide User Repository
     * @param authDataSource
     * @param userDataSource
     * @param userInfoMapper
     */
    @Provides
    @Singleton
    fun provideUserRepository(
        authDataSource: IAuthDataSource,
        userDataSource: IUsersDataSource,
        storageDataSource: IStorageDataSource,
        userInfoMapper: UserInfoMapper,
        saveUserInfoMapper: SaveUserInfoMapper,
        authUserMapper: AuthUserMapper
    ): IUserRepository =
        UserRepositoryImpl(
            authDataSource,
            userDataSource,
            storageDataSource,
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
        faucetBlockchainDataSource: IFaucetBlockchainDataSource
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
            faucetBlockchainDataSource
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
}
