package com.dreamsoftware.artcollectibles.ui.di

import android.content.Context
import com.dreamsoftware.artcollectibles.ui.screens.discovery.DiscoveryScreenErrorMapper
import com.dreamsoftware.artcollectibles.ui.screens.mytokens.MyTokensScreenErrorMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class UiModule {

    @Provides
    @ViewModelScoped
    fun provideMyTokensScreenErrorMapper(@ApplicationContext context: Context) = MyTokensScreenErrorMapper(context)

    @Provides
    @ViewModelScoped
    fun provideDiscoveryScreenErrorMapper(@ApplicationContext context: Context) = DiscoveryScreenErrorMapper(context)


}