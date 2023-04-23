package com.dreamsoftware.artcollectibles.data.firebase.di

import com.dreamsoftware.artcollectibles.data.firebase.datasource.*
import com.dreamsoftware.artcollectibles.data.firebase.datasource.impl.*
import com.dreamsoftware.artcollectibles.data.firebase.datasource.impl.AuthDataSourceImpl
import com.dreamsoftware.artcollectibles.data.firebase.datasource.impl.StorageDataSourceImpl
import com.dreamsoftware.artcollectibles.data.firebase.datasource.impl.UsersDataSourceImpl
import com.dreamsoftware.artcollectibles.data.firebase.datasource.impl.WalletMetadataDataSourceImpl
import com.dreamsoftware.artcollectibles.data.firebase.mapper.*
import com.dreamsoftware.artcollectibles.utils.CryptoUtils
import com.dreamsoftware.artcollectibles.utils.IApplicationAware
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
    fun provideExternalUserAuthenticated(): ExternalUserAuthenticatedMapper =
        ExternalUserAuthenticatedMapper()

    /**
     * Provide User Authenticated Mapper
     */
    @Provides
    @Singleton
    fun provideUserAuthenticatedMapper(): UserAuthenticatedMapper = UserAuthenticatedMapper()

    /**
     * Provide Wallet Metadata Mapper
     * @param cryptoUtils
     * @param applicationAware
     */
    @Provides
    @Singleton
    fun provideWalletMetadataMapper(
        cryptoUtils: CryptoUtils,
        applicationAware: IApplicationAware
    ): WalletMetadataMapper =
        WalletMetadataMapper(cryptoUtils, applicationAware)

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
     * Provide Secret Mapper
     * @param cryptoUtils
     * @param applicationAware
     */
    @Provides
    @Singleton
    fun provideSecretMapper(
        cryptoUtils: CryptoUtils,
        applicationAware: IApplicationAware
    ): SecretMapper = SecretMapper(cryptoUtils, applicationAware)


    /**
     * Provide Categories Mapper
     */
    @Provides
    @Singleton
    fun provideCategoriesMapper(): CategoriesMapper = CategoriesMapper()

    /**
     * Provide Comment Mapper
     */
    @Provides
    @Singleton
    fun provideCommentMapper(): CommentMapper = CommentMapper()

    /**
     * Provide Save Comment Mapper
     */
    @Provides
    @Singleton
    fun provideSaveCommentMapper(): SaveCommentMapper = SaveCommentMapper()

    /**
     * Provide Save notification mapper
     */
    @Provides
    @Singleton
    fun provideSaveNotificationMapper(): SaveNotificationMapper = SaveNotificationMapper()

    /**
     * Provide Notification mapper
     */
    @Provides
    @Singleton
    fun provideNotificationMapper(): NotificationMapper = NotificationMapper()

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
     * Provide Wallet Metadata Data Source
     * @param firebaseStore
     * @param walletMetadataMapper
     */
    @Provides
    @Singleton
    fun provideWalletMetadataDataSource(
        firebaseStore: FirebaseFirestore,
        walletMetadataMapper: WalletMetadataMapper
    ): IWalletMetadataDataSource = WalletMetadataDataSourceImpl(firebaseStore, walletMetadataMapper)

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

    /**
     * Provider Secret Data Source
     * @param firebaseStore
     * @param secretMapper
     */
    @Provides
    @Singleton
    fun provideSecretDataSource(
        firebaseStore: FirebaseFirestore,
        secretMapper: SecretMapper
    ): ISecretDataSource =
        SecretDataSourceImpl(firebaseStore, secretMapper)

    /**
     * Provide Favorites Data Source
     * @param firebaseStore
     */
    @Provides
    @Singleton
    fun provideFavoritesDataSource(
        firebaseStore: FirebaseFirestore
    ): IFavoritesDataSource = FavoritesDataSourceImpl(firebaseStore)

    /**
     * Provide Visitors Data Source
     * @param firebaseStore
     */
    @Provides
    @Singleton
    fun provideVisitorsDataSource(
        firebaseStore: FirebaseFirestore
    ): IVisitorsDataSource = VisitorsDataSourceImpl(firebaseStore)

    /**
     * Provide Followers Data Source
     * @param firebaseStore
     */
    @Provides
    @Singleton
    fun provideFollowersDataSource(
        firebaseStore: FirebaseFirestore
    ): IFollowersDataSource = FollowersDataSourceImpl(firebaseStore)

    /**
     * Provide Categories Data Source
     * @param categoriesMapper
     * @param firebaseStore
     */
    @Provides
    @Singleton
    fun provideCategoriesDataSource(
        categoriesMapper: CategoriesMapper,
        firebaseStore: FirebaseFirestore
    ): ICategoriesDataSource = CategoriesDataSourceImpl(categoriesMapper, firebaseStore)

    /**
     * Provide Comments Data Source
     * @param firebaseStore
     * @param commentMapper
     * @param saveCommentMapper
     */
    @Provides
    @Singleton
    fun provideCommentsDataSource(
        firebaseStore: FirebaseFirestore,
        commentMapper: CommentMapper,
        saveCommentMapper: SaveCommentMapper
    ): ICommentsDataSource = CommentsDataSourceImpl(firebaseStore, commentMapper, saveCommentMapper)

    /**
     * Provide Notifications Data Source
     * @param firebaseStore
     * @param saveNotificationMapper
     * @param notificationMapper
     */
    @Provides
    @Singleton
    fun provideNotificationsDataSource(
        firebaseStore: FirebaseFirestore,
        saveNotificationMapper: SaveNotificationMapper,
        notificationMapper: NotificationMapper
    ): INotificationsDataSource = NotificationsDataSourceImpl(firebaseStore, saveNotificationMapper, notificationMapper)
}