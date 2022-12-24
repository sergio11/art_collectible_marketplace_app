package com.dreamsoftware.artcollectibles.data.auth.di

import com.dreamsoftware.artcollectibles.data.auth.datasource.IAuthDataSource
import com.dreamsoftware.artcollectibles.data.auth.datasource.impl.AuthDataSourceImpl
import com.dreamsoftware.artcollectibles.data.auth.mapper.AuthUserMapper
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AuthModule {

    /**
     * Provide Auth User Mapper
     */
    @Provides
    @Singleton
    fun provideAuthUserMapper(): AuthUserMapper = AuthUserMapper()

    /**
     * Provide Firebase Auth
     */
    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

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

}