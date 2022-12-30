package com.dreamsoftware.artcollectibles.data.di

import com.dreamsoftware.artcollectibles.data.api.mapper.*
import com.dreamsoftware.artcollectibles.data.api.repository.*
import com.dreamsoftware.artcollectibles.data.api.repository.impl.*
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IArtCollectibleBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IArtMarketplaceBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IWalletDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.di.BlockchainModule
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IAuthDataSource
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IStorageDataSource
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IUsersDataSource
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IWalletSecretsDataSource
import com.dreamsoftware.artcollectibles.data.firebase.di.FirebaseModule
import com.dreamsoftware.artcollectibles.data.ipfs.datasource.IpfsDataSource
import com.dreamsoftware.artcollectibles.data.ipfs.di.IPFSModule
import com.dreamsoftware.artcollectibles.data.preferences.datasource.IPreferencesDataSource
import com.dreamsoftware.artcollectibles.data.preferences.di.PreferencesModule
import com.dreamsoftware.artcollectibles.utils.SecretUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(includes = [FirebaseModule::class, BlockchainModule::class, IPFSModule::class, PreferencesModule::class])
@InstallIn(SingletonComponent::class)
class DataModule {


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
    fun provideCreateUserInfoMapper(): CreateUserInfoMapper = CreateUserInfoMapper()

    /**
     * Provide Update User Info Mapper
     */
    @Provides
    @Singleton
    fun provideUpdateUserInfoMapper(): UpdateUserInfoMapper = UpdateUserInfoMapper()

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
     * @param userCredentialsMapper
     */
    @Provides
    @Singleton
    fun provideArtMarketplaceRepository(
        artMarketplaceBlockchainDataSource: IArtMarketplaceBlockchainDataSource,
        artCollectibleRepository: IArtCollectibleRepository,
        userDataSource: IUsersDataSource,
        userInfoMapper: UserInfoMapper,
        walletRepository: IWalletRepository,
        userCredentialsMapper: UserCredentialsMapper
    ): IArtMarketplaceRepository =
        ArtMarketplaceRepositoryImpl(
            artMarketplaceBlockchainDataSource,
            artCollectibleRepository,
            userDataSource,
            userInfoMapper,
            walletRepository,
            userCredentialsMapper
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
        preferencesDataSource: IPreferencesDataSource,
        userInfoMapper: UserInfoMapper,
        createUserInfoMapper: CreateUserInfoMapper,
        updateUserInfoMapper: UpdateUserInfoMapper,
        authUserMapper: AuthUserMapper
    ): IUserRepository =
        UserRepositoryImpl(
            authDataSource,
            userDataSource,
            storageDataSource,
            preferencesDataSource,
            userInfoMapper,
            createUserInfoMapper,
            updateUserInfoMapper,
            authUserMapper
        )

    /**
     * Provide Wallet Repository
     * @param userCredentialsMapper
     * @param preferencesDataSource
     * @param secretDataSource
     * @param secretUtils
     * @param walletDataSource
     */
    @Provides
    @Singleton
    fun provideWalletRepository(
        userCredentialsMapper: UserCredentialsMapper,
        preferencesDataSource: IPreferencesDataSource,
        secretDataSource: IWalletSecretsDataSource,
        secretUtils: SecretUtils,
        walletDataSource: IWalletDataSource
    ): IWalletRepository =
        WalletRepositoryImpl(
            userCredentialsMapper,
            preferencesDataSource,
            secretDataSource,
            secretUtils,
            walletDataSource
        )
}
