package com.dreamsoftware.artcollectibles.data.firebase.di

import com.dreamsoftware.artcollectibles.data.firebase.datasource.IAuthDataSource
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IWalletSecretsDataSource
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IUsersDataSource
import com.dreamsoftware.artcollectibles.data.firebase.datasource.impl.AuthDataSourceImpl
import com.dreamsoftware.artcollectibles.data.firebase.datasource.impl.WalletSecretsDataSourceImpl
import com.dreamsoftware.artcollectibles.data.firebase.datasource.impl.UsersDataSourceImpl
import com.dreamsoftware.artcollectibles.data.firebase.mapper.FirebaseUserMapper
import com.dreamsoftware.artcollectibles.data.firebase.mapper.WalletSecretMapper
import com.dreamsoftware.artcollectibles.data.firebase.mapper.UserMapper
import com.dreamsoftware.artcollectibles.utils.CryptoUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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
    fun provideSecretMapper(cryptoUtils: CryptoUtils): WalletSecretMapper = WalletSecretMapper(cryptoUtils)

    /**
     * Provide User Mapper
     */
    @Provides
    @Singleton
    fun provideUserMapper(): UserMapper = UserMapper()

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
        firebaseStore: FirebaseFirestore
    ): IUsersDataSource = UsersDataSourceImpl(userMapper, firebaseStore)

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
}