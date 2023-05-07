# ArtNiche - A NFT Art collectibles marketplace for digital artists

<p>
  <img src="https://img.shields.io/github/last-commit/sergio11/art_collectible_marketplace_app.svg" />
</p>

<img width="auto" align="left" src="./doc/screenshots/art_niche_icon.png" />


Explore exclusive art collectibles by the most in-demand creators, trade with other collectors in the Marketplace, and mint your own NFTs all in one place!

**ArtNiche** try to eliminate some of the hurdles usually met by content creators who desire to monetize their art by serving it directly to the public. Not only is owning an NFT a trend nowadays, but it has also become a status symbol for those who get into it. In any case, it is an actual investment that has become profitable for both collectors and buyers.

</br>

<p align="center">
  <img src="https://img.shields.io/badge/Android%20Studio-3DDC84.svg?style=for-the-badge&logo=android-studio&logoColor=white" />
  <img src="https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white" />
  <img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" />
  <img src="https://img.shields.io/badge/web3j-F16822?style=for-the-badge&logo=web3.js&logoColor=white" />
  <img src="https://img.shields.io/badge/Blockchain.com-121D33?logo=blockchaindotcom&logoColor=fff&style=for-the-badge" />
</p>

## What are NFT Art Collectibles?

NFT stands for non-fungible token, a digital asset the ownership of which is recorded on a tamper-proof blockchain that cannot be forged. NFTs represent real-world objects and properties. Compared to cryptocurrencies and fiat currencies, each NFT is unique and cannot be exchanged for another or used to purchase items whether.

Art Collectibles NFTs may be memorable artworks,  the properties of which are be specified through smart contract rules that govern NFTs.

Factors like digital scarcity and unchangeable ownership generally affect the value of NFTs in ArtNiche. There is so much online content that people usually do not consider these virtual commodities to have real monetary value, so the rarer the digital item and the more it has historical and cultural relevance, the more valuable it will be.

While Art Collectible NFTs may be copied or downloaded, only the creators own it and may sell it. **Ownership may not be manipulated**, since NFTs live on the blockchain where records are immutable, and purchasers may always directly verify a product’s authenticity before a sale.

An ArtNiche creator or seller can search for an art collectible NFTs as weel as they also can mint new NFTs, which is essentially uploading the file to be transformed into an NFT and details pertaining to it. Minting is the online process where the files of the item or collectible is recorded on the blockchain.

ArtNiche provide you some free funds of MATIC cryptocurrency and a digital wallet in order to transact with NFT in the marketplace. You could also get more matic from Mumbai faucet from your artist profile.

## How ArtNiche works?

NFTs at ArtNiche are created in two clearly differentiated steps. The image file and the metadata linked to the NFT are formatted and registered in IPFS through the services of the Pinata platform. Once the metadata registration is completed, ArtNiche uses the CID (Content Identifier) to complete the Mint process in the ArtNiche DAPP deployed in a Polygon (Mumbai) tesnet through the wrappers of the smart contracts generated with the Web3J CLI.

Once this process is completed, the user will be informed of the success of the registration, he will be the original owner and creator of that NFT. As the original creator you will have the possibility to define a "royalty" to obtain benefits in future sales.

ArtNiche uses firestore to store complementary information to the NFTs and the use made of them.

<img  src="./doc/art_niche_diagram.drawio.svg" />

 ## Tech stack & Open-source libraries
 - Minimum SDK level 23
 - 100% [Kotlin](https://kotlinlang.org/)
 - Using [Android Architecture Starter Templates](https://github.com/android/architecture-templates).
 - Android Studio Flamingo.
 - Architecture
   - MVI, Immutability and UDF (Unidirectional Data Flow).
   - Clean architecture approach.
   - [Dagger Hilt](https://developer.android.com/training/dependency-injection/hilt-android) - dependency injection
 - UI Layer
   - LiveData - notify domain layer data to views.
   - Lifecycle - dispose observing data when lifecycle state changes.
   - ViewModel - UI related data holder, lifecycle aware.
   - [Coil](https://coil-kt.github.io/coil/) - An image loading library for Android backed by Kotlin Coroutines.
   - [Jetpack Camera X](https://developer.android.com/jetpack/androidx/releases/camera) - CameraX is a Jetpack support library, built to help you make camera app development easier.
   - Palette API - The Palette library provides a powerful and intuitive API for creating more engaging apps extracting prominent colors from images.
   - [Jetpack Compose](https://developer.android.com/jetpack/compose) - Jetpack Compose is a modern toolkit for building native Android UI. Jetpack Compose simplifies and accelerates UI development on Android with less code, powerful tools, and intuitive Kotlin APIs. 
   - [Jetpack Compose Material 3](https://developer.android.com/jetpack/androidx/releases/compose-material3) - Build Jetpack Compose UIs with components from Material Design 3, the next evolution of Material Design. Material 3 includes updated themes and components, and Material You customization features such as dynamic color.
   - [Accompanist FlowLayout](https://github.com/google/accompanist) - A collection of extension libraries for Jetpack Compose from Google.
   - [Vico](https://github.com/patrykandpatrick/vico) - Vico is a light and extensible chart library for Android. It’s compatible with both Jetpack Compose and the view system, but its two main modules—compose and views—are independent.
   - [SplashScreen API](https://developer.android.com/develop/ui/views/launch/splash-screen) - Starting in Android 12, the SplashScreen API lets apps launch with animation, including an into-app motion at launch, a splash screen showing your app icon, and a transition to your app itself. A SplashScreen is a Window and therefore covers an Activity.
  - Data Layer
    - [Retrofit2 + Moshi](https://github.com/square/retrofit) - constructing the REST API
    - [OkHttp3](https://github.com/square/okhttp) - implementing interceptor, logging and mocking web server
    - [Room](https://developer.android.com/jetpack/androidx/releases/room?hl=es-419) - The Room persistence library provides an abstraction layer over SQLite to allow for more robust database access while harnessing the full power of SQLite.
    - Cloud Firestore - To persist data related to market and users.
    - Firebase Auth (Social Media integrations) - To authenticate users.
    - Firebase Storage - To save files such us wallets and user profile images.
    - [Web3J](https://docs.web3j.io/4.9.8/) - Web3j is a highly modular, reactive, type safe Java and Android library for working with Smart Contracts and integrating with clients (nodes) on the Ethereum network
    - [Cache4k](https://github.com/ReactiveCircus/cache4k) cache4k provides a simple in-memory key-value cache, with support for time-based (expiration) and size-based evictions.
    - [Jetpack Datastore](https://developer.android.com/topic/libraries/architecture/datastore) - Jetpack DataStore is a data storage solution that allows you to store key-value pairs or typed objects with protocol buffers.
    
 ## App screenshots and demo.
    
 ### Onboarding & Account management.
 
 Digital creators and buyers will be able to register on ArtNiche using their *Facebook/Google* account or create a user with *email/password*.
 
 <img width="250px" align="left" src="./doc/screenshots/app_picture_1.png" />
 <img width="250px" align="left" src="./doc/screenshots/app_picture_2.png" />
 <img width="250px" src="./doc/screenshots/app_picture_3.png" /> 
 <img width="250px" align="left" src="./doc/screenshots/app_picture_4.png" />
 <img width="250px" src="./doc/screenshots/app_picture_5.png" />
 
 ## Home Market 
 
 <img width="250px" align="left" src="./doc/screenshots/app_picture_45.png" />
 <img width="250px" align="left" src="./doc/screenshots/app_picture_46.png" />
 <img width="250px" src="./doc/screenshots/app_picture_47.png" />
 
 <img width="250px" align="left" src="./doc/screenshots/app_picture_48.png" />
 <img width="250px" align="left" src="./doc/screenshots/app_picture_49.png" />
 <img width="250px" src="./doc/screenshots/app_picture_43.png" />
 
 <img width="250px" src="./doc/screenshots/app_picture_44.png" />
 
 
 # Art Collectibles Categories
 
 <img width="250px" align="left" src="./doc/screenshots/app_picture_37.png" />
 <img width="250px" align="left" src="./doc/screenshots/app_picture_40.png" />
 <img width="250px" src="./doc/screenshots/app_picture_38.png" />
 <img width="250px" align="left" src="./doc/screenshots/app_picture_39.png" />
 <img width="250px" align="left" src="./doc/screenshots/app_picture_24.png" />
 <img width="250px" src="./doc/screenshots/app_picture_25.png" />
 
 ## Mint an new art collectible NFT.
  
 <img width="250px" align="left" src="./doc/screenshots/app_picture_11.png" />
 <img width="250px" align="left" src="./doc/screenshots/app_picture_12.png" />
 <img width="250px" src="./doc/screenshots/app_picture_13.png" />
  
 <img width="250px" align="left" src="./doc/screenshots/app_picture_14.png" />
 <img width="250px" align="left" src="./doc/screenshots/app_picture_15.png" />
 <img width="250px" src="./doc/screenshots/app_picture_16.png" />
  
 <img width="250px" src="./doc/screenshots/app_picture_17.png" />
 
 ## Art Collectible NFTs
  
 <img width="250px" align="left" src="./doc/screenshots/app_picture_18.png" />
 <img width="250px" align="left" src="./doc/screenshots/app_picture_19.png" />
 <img width="250px" src="./doc/screenshots/app_picture_30.png" />
 
 <img width="250px" align="left" src="./doc/screenshots/app_picture_31.png" />
 <img width="250px" align="left" src="./doc/screenshots/app_picture_32.png" />
 <img width="250px" src="./doc/screenshots/app_picture_35.png" />
 
 <img width="250px" align="left" src="./doc/screenshots/app_picture_41.png" />
 <img width="250px" align="left" src="./doc/screenshots/app_picture_50.png" />
 <img width="250px" src="./doc/screenshots/app_picture_42.png" />
 
  # Artist Detail
  
 <img width="250px" align="left" src="./doc/screenshots/app_picture_20.png" />
 <img width="250px" align="left" src="./doc/screenshots/app_picture_21.png" />
 <img width="250px" src="./doc/screenshots/app_picture_22.png" />
 <img width="250px" align="left" src="./doc/screenshots/app_picture_26.png" />
 <img width="250px" align="left" src="./doc/screenshots/app_picture_23.png" />
 <img width="250px" src="./doc/screenshots/app_picture_33.png" />
 
 ### User profile and user preferences.
 
 ArtNiche users will be able to update their personal data to make themselves known better, inform about their interests, their instagram profile on social networks, information about their work.
 
 <img width="250px" align="left" src="./doc/screenshots/app_picture_7.png" />
 <img width="250px" align="left" src="./doc/screenshots/app_picture_8.png" />
 <img width="250px" src="./doc/screenshots/app_picture_9.png" />
 
 In addition to this, they will be able to edit some preferences related to privacy and the functionality of the application.

 <img width="250px" align="left" src="./doc/screenshots/app_picture_10.png" />
 <img width="250px" align="left" src="./doc/screenshots/app_picture_29.png" />
 <img width="250px" src="./doc/screenshots/app_picture_28.png" />
 
 <img width="250px" src="./doc/screenshots/app_picture_34.png" />
 
  
 
## Alchemy screenshots

<img src="./doc/alchemy/alchemy_picture_1.png" />
<img src="./doc/alchemy/alchemy_picture_2.png" />
<img src="./doc/alchemy/alchemy_picture_3.png" />
<img src="./doc/alchemy/alchemy_picture_4.png" />
 
 
 ## Please Share & Star the repository to keep me motivated.
  <a href = "https://github.com/sergio11/art_collectible_marketplace_app/stargazers">
     <img src = "https://img.shields.io/github/stars/sergio11/art_collectible_marketplace_app" />
  </a>
  <a href = "https://twitter.com/SergioReact418">
     <img src = "https://img.shields.io/twitter/url?label=follow&style=social&url=https%3A%2F%2Ftwitter.com%2FSergioReact418" />
  </a>
 
 ## Attributions
 
 <div> Icons made by <a href="https://www.flaticon.com/authors/freepik" title="Freepik"> Freepik </a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com'</a></div>
 <p><a href="https://www.freepik.com/free-photo/handsome-cheerful-young-man-with-stylish-haircut-dimpled-smile-posing-isolated-against-blank-yellow-wall-dressed-cozy-maroon-sweater-having-confident-look_11891916.htm#page=2&query=person&position=3&from_view=search&track=robertav1_2_sidr">Image by karlyukav</a> on Freepik</p>
 <p><a href="https://www.freepik.com/free-photo/handsome-cheerful-man-teenager-keeps-arms-folded-looks-pensively-aside-has-gentle-smile-wears-basic-jumper-poses_13844944.htm#page=4&query=person&position=39&from_view=search&track=robertav1_2_sidr">Image by wayhomestudio</a> on Freepik</p>
 <p><a href="https://www.freepik.com/free-photo/no-problem-concept-bearded-man-makes-okay-gesture-has-everything-control-all-fine-gesture-wears-spectacles-jumper-poses-against-pink-wall-says-i-got-this-guarantees-something_13580471.htm#query=person&position=24&from_view=search&track=robertav1_2_sidr">Image by wayhomestudio</a> on Freepik</p>
 <p>Image by <a href="https://www.freepik.com/free-photo/portrait-beautiful-blond-woman-with-trendy-hairstyle_27506439.htm#page=9&query=person&position=19&from_view=search&track=sph">Freepik</a></p>
 
    
