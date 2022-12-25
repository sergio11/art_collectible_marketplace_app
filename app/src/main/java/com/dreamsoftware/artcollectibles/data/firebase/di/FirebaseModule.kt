package com.dreamsoftware.artcollectibles.data.firebase.di

import com.dreamsoftware.artcollectibles.data.firebase.datasource.IAuthDataSource
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IUsersDataSource
import com.dreamsoftware.artcollectibles.data.firebase.datasource.impl.AuthDataSourceImpl
import com.dreamsoftware.artcollectibles.data.firebase.datasource.impl.UsersDataSourceImpl
import com.dreamsoftware.artcollectibles.data.firebase.mapper.AuthUserMapper
import com.dreamsoftware.artcollectibles.data.firebase.mapper.UserMapper
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
    fun provideAuthUserMapper(): AuthUserMapper = AuthUserMapper()

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
     * @param authUserMapper
     * @param firebaseAuth
     */
    @Provides
    @Singleton
    fun provideAuthDataSource(
        authUserMapper: AuthUserMapper,
        firebaseAuth: FirebaseAuth
    ): IAuthDataSource = AuthDataSourceImpl(
        authUserMapper,
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
}