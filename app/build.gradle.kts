import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@Suppress("DSL_SCOPE_VIOLATION") // Remove when fixed https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt.gradle)
    alias(libs.plugins.ksp)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.dreamsoftware.artcollectibles"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.dreamsoftware.artcollectibles"
        minSdk = 23
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.dreamsoftware.artcollectibles.HiltTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        // Enable room auto-migrations
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }

        // Configure Social Auth resources
        with(gradleLocalProperties(rootDir)) {
            resValue("string", "facebook_app_id", getProperty("facebook.applicationId"))
            resValue("string", "facebook_client_token", getProperty("facebook.clientToken"))
            resValue("string", "facebook_login_protocol_scheme", getProperty("facebook.loginProtocolScheme"))
            resValue("string", "google_client_id", getProperty("googleClientId"))
        }
        // Pinata configuration
        buildConfigField(type = "String", name = "PINATA_BASE_URL", value = "\"https://api.pinata.cloud/\"")
        buildConfigField(type = "String", name = "PINATA_GATEWAY_BASE_URL", value = "\"https://gateway.pinata.cloud/ipfs/\"")
        // Blockchain Configuration
        buildConfigField(type = "String", name = "ALCHEMY_URL", value = "\"https://polygon-mumbai.g.alchemy.com/v2/${gradleLocalProperties(rootDir).getProperty("alchemyAccountPrivateKey")}\"")
        buildConfigField(type = "Long", name = "CHAIN_ID", value = "80001L")
        buildConfigField(type = "Long", name = "GAS_PRICE", value = "1000000000L")
        buildConfigField(type = "Long", name = "GAS_LIMIT", value = "993613L")
        // ArtCollectible contract deployed to 0xc341CC01DB1cA6A2Eb1C864EBC8a2AADe725D55e
        buildConfigField(type = "String", name = "ART_COLLECTIBLE_CONTRACT_ADDRESS", value = "\"0xc341CC01DB1cA6A2Eb1C864EBC8a2AADe725D55e\"")
        // ArtMarketplace contract deployed to 0xc9F57EA9418190f14bbF2d00E776462105ffe677
        buildConfigField(type = "String", name = "ART_MARKETPLACE_CONTRACT_ADDRESS", value = "\"0xc9F57EA9418190f14bbF2d00E776462105ffe677\"")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
        aidl = false
        buildConfig = true
        renderScript = false
        shaders = false
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidxComposeCompiler.get()
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}


dependencies {
    // Core Android dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Hilt Dependency Injection
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    // Hilt and instrumented tests.
    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.android.compiler)
    // Hilt and Robolectric tests.
    testImplementation(libs.hilt.android.testing)
    kaptTest(libs.hilt.android.compiler)

    // Arch Components
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Compose
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    // Tooling
    debugImplementation(libs.androidx.compose.ui.tooling)

    // Web3j: library to interact with smart contracts and integrate applications with Ethereum blockchain.
    implementation("org.web3j:core:4.9.4")
    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:31.1.1"))
    //Firebase Authentication library
    implementation("com.google.firebase:firebase-auth-ktx")
    //Cloud Firestore library
    implementation("com.google.firebase:firebase-firestore-ktx")
    // Google Play services library
    implementation("com.google.android.gms:play-services-auth:20.4.0")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")

    // Facebook Login SDK
    implementation("com.facebook.android:facebook-login:latest.release")
    // Instrumented tests
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Local tests: jUnit, coroutines, Android runner
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)

    // Instrumented tests: jUnit rules and runners

    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.runner)
}
