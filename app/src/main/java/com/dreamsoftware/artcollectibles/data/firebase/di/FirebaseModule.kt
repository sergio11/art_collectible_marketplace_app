package com.dreamsoftware.artcollectibles.data.firebase.di

import com.dreamsoftware.artcollectibles.data.firebase.datasource.IAuthDataSource
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IStorageDataSource
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IUsersDataSource
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IWalletSecretsDataSource
import com.dreamsoftware.artcollectibles.data.firebase.datasource.impl.AuthDataSourceImpl
import com.dreamsoftware.artcollectibles.data.firebase.datasource.impl.StorageDataSourceImpl
import com.dreamsoftware.artcollectibles.data.firebase.datasource.impl.UsersDataSourceImpl
import com.dreamsoftware.artcollectibles.data.firebase.datasource.impl.WalletSecretsDataSourceImpl
import com.dreamsoftware.artcollectibles.data.firebase.mapper.*
import com.dreamsoftware.artcollectibles.utils.CryptoUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FirebaseModule {

    /**
     * Provide External User Authenticated
     */
    @Provides
    @Singleton
    fun provideExternalUserAuthenticated(): ExternalUserAuthenticatedMapper = ExternalUserAuthenticatedMapper()

    /**
     * Provide User Authenticated Mapper
     */
    @Provides
    @Singleton
    fun provideUserAuthenticatedMapper(): UserAuthenticatedMapper = UserAuthenticatedMapper()

    /**
     * Provide Secret Mapper
     * @param cryptoUtils
     */
    @Provides
    @Singleton
    fun provideSecretMapper(cryptoUtils: CryptoUtils): WalletSecretMapper =
        WalletSecretMapper(cryptoUtils)

    /**
     * Provide User Mapper
     */
    @Provides
    @Singleton
    fun provideUserMapper(): UserMapper = UserMapper()

    /**
     * Provide Create User Mapper
     */
    @Provides
    @Singleton
    fun provideCreateUserMapper(): SaveUserMapper = SaveUserMapper()

    /**
     * Provide Firebase Auth
     */
    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    /**
     * Provide Firebase Store
     */
    @Provides
    @Singleton
    fun provideFirebaseStore() = Firebase.firestore

    /**
     * Provide Firebase Storage
     */
    @Provides
    @Singleton
    fun provideFirebaseStorage() = Firebase.storage("gs://artcollectiblemarketplace.appspot.com")

    /**
     * Provide Auth Data Source
     * @param externalUserAuthenticatedMapper
     * @param firebaseAuth
     */
    @Provides
    @Singleton
    fun provideAuthDataSource(
        externalUserAuthenticatedMapper: ExternalUserAuthenticatedMapper,
        userAuthenticatedMapper: UserAuthenticatedMapper,
        firebaseAuth: FirebaseAuth
    ): IAuthDataSource = AuthDataSourceImpl(
        externalUserAuthenticatedMapper,
        userAuthenticatedMapper,
        firebaseAuth
    )

    /**
     * Provide User Data Source
     * @param userMapper
     * @param saveUserMapper
     * @param firebaseStore
     */
    @Provides
    @Singleton
    fun provideUserDataSource(
        userMapper: UserMapper,
        saveUserMapper: SaveUserMapper,
        firebaseStore: FirebaseFirestore
    ): IUsersDataSource =
        UsersDataSourceImpl(
            userMapper,
            saveUserMapper,
            firebaseStore
        )

    /**
     * Provide Wallet Secrets Data Source
     * @param firebaseStore
     * @param walletSecretMapper
     */
    @Provides
    @Singleton
    fun provideWalletSecretsDataSource(
        firebaseStore: FirebaseFirestore,
        walletSecretMapper: WalletSecretMapper
    ): IWalletSecretsDataSource = WalletSecretsDataSourceImpl(firebaseStore, walletSecretMapper)

    /**
     * Provide Storage Data Source
     * @param firebaseStorage
     */
    @Provides
    @Singleton
    fun provideStorageDataSource(
        firebaseStorage: FirebaseStorage
    ): IStorageDataSource =
        StorageDataSourceImpl(firebaseStorage)

}