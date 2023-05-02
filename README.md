# ArtNiche - An art collectible NFT marketplace for digital artists

<p>
  <img src="https://img.shields.io/github/last-commit/sergio11/art_collectible_marketplace_app.svg" />
</p>

<img width="auto" align="left" src="./doc/screenshots/art_niche_icon.png" />


Explore exclusive art collectibles by the most in-demand creators, trade with other collectors in the Marketplace, and mint your own NFTs all in one place!



 ## Tech stack & Open-source libraries
 - Minimum SDK level 23
 - 100% [Kotlin](https://kotlinlang.org/)
 - UI Layer
   - LiveData - notify domain layer data to views.
   - Lifecycle - dispose observing data when lifecycle state changes.
   - ViewModel - UI related data holder, lifecycle aware.
   - [Coil](https://coil-kt.github.io/coil/) - An image loading library for Android backed by Kotlin Coroutines.
   - Jetpack Camera X - CameraX is a Jetpack support library, built to help you make camera app development easier.
   - Palette API - The Palette library provides a powerful and intuitive API for creating more engaging apps extracting prominent colors from images.
   - Jetpack Compose - Jetpack Compose is a modern toolkit for building native Android UI. Jetpack Compose simplifies and accelerates UI development on Android with less code,   powerful tools, and intuitive Kotlin APIs. 
  - Data Layer
    - [Retrofit2 + Moshi](https://github.com/square/retrofit) - constructing the REST API
    - [OkHttp3](https://github.com/square/okhttp) - implementing interceptor, logging and mocking web server
    - [Room](https://developer.android.com/jetpack/androidx/releases/room?hl=es-419) - The Room persistence library provides an abstraction layer over SQLite to allow for more robust database access while harnessing the full power of SQLite.
    - Cloud Firestore - To persist data related to market and users.
    - Firebase Auth (Social Media integrations) - To authenticate users.
    - Firebase Storage - To save files such us wallets and user profile images.
    - [Web3J](https://docs.web3j.io/4.9.8/) - Web3j is a highly modular, reactive, type safe Java and Android library for working with Smart Contracts and integrating with clients (nodes) on the Ethereum network
    
    
    
 ### Onboarding & Account management
 
 <img width="250px" align="left" src="./doc/screenshots/app_picture_1.png" />
 <img width="250px" align="left" src="./doc/screenshots/app_picture_2.png" />
 <img width="250px" align="left" src="./doc/screenshots/app_picture_3.png" />
 <img width="250px" src="./doc/screenshots/app_picture_4.png" />
    
