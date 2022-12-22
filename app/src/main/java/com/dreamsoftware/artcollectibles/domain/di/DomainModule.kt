package com.dreamsoftware.artcollectibles.domain.di

import com.dreamsoftware.artcollectibles.data.api.IArtMarketplaceRepository
import com.dreamsoftware.artcollectibles.domain.usecase.impl.FetchAvailableMarketItemsUseCase
import com.dreamsoftware.artcollectibles.domain.usecase.impl.FetchSellingMarketItemsUseCase
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
}