plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.android.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.androidx.navigation.safeargs.kotlin)
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.stable.scoi"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.stable.scoi"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        dataBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.identity.jvm)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // HILT
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation("com.auth0:java-jwt:4.5.0")

    // RETROFIT
    implementation(libs.retrofit.converter.gson)
    implementation(libs.retrofit.android)
    implementation(libs.retrofit.converter.moshi)

    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)

    // OKHTTP
    implementation(libs.okhttp.android)
    implementation(libs.okhttp.log)

    // COROUTINE
    implementation(libs.kotlinx.coroutines.android)

    // NAVIGATION
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    implementation("androidx.biometric:biometric-ktx:1.4.0-alpha02")
    implementation(libs.tbuonomo.dotsindicator)
    implementation(libs.airbnb.lottie)

    implementation(platform(libs.firebase.bom))

    implementation(libs.firebase.messaging)
}