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
     * Provide Auth User Mapper
     */
    @Provides
    @Singleton
    fun provideFirebaseUserMapper(): FirebaseUserMapper = FirebaseUserMapper()

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
    fun provideCreateUserMapper(): CreateUserMapper = CreateUserMapper()

    /**
     * Provide Update User Mapper
     */
    @Provides
    @Singleton
    fun provideUpdateUserMapper(): UpdateUserMapper = UpdateUserMapper()

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
     * @param firebaseUserMapper
     * @param firebaseAuth
     */
    @Provides
    @Singleton
    fun provideAuthDataSource(
        firebaseUserMapper: FirebaseUserMapper,
        firebaseAuth: FirebaseAuth
    ): IAuthDataSource = AuthDataSourceImpl(
        firebaseUserMapper,
        firebaseAuth
    )

    /**
     * Provide User Data Source
     * @param userMapper
     * @param firebaseStore
     */
    @Provides
    @Singleton
    fun provideUserDataSource(
        userMapper: UserMapper,
        createUserMapper: CreateUserMapper,
        updateUserMapper: UpdateUserMapper,
        firebaseStore: FirebaseFirestore
    ): IUsersDataSource =
        UsersDataSourceImpl(
            userMapper,
            createUserMapper,
            updateUserMapper,
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