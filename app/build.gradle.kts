plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id("com.github.ben-manes.versions")
    alias(libs.plugins.hilt.android.gradle)
}

android {
    namespace = Versions.appID
    compileSdk = Versions.compileAndTargetSDK

    defaultConfig {
        applicationId = Versions.appID

        minSdk = Versions.minSDK
        targetSdk = Versions.compileAndTargetSDK
        versionCode = Versions.versionCode
        versionName = Versions.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = Versions.jvmTarget
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.kotlinCompilerExtVersion
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/gradle/incremental.annotation.processors"
        }
    }
}

dependencies {
    implementation(project(mapOf("path" to ":data")))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.runtime.ktx)

    implementation(libs.hilt.android)
    implementation(libs.androidx.lifecycle.runtime.compose)
    kapt(libs.hilt.android.compiler)

    implementation(libs.coil.compose)

    implementation(libs.lottie.compose)

    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    implementation(libs.retrofit)
    implementation(libs.retrofit.okhttp3)
    implementation(libs.retrofit.gson)
    implementation(libs.retrofit.okhttp3.logging.interceptor)

    testImplementation(libs.testing.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    
    androidTestImplementation(libs.testing.testRunner)
    androidTestImplementation(libs.testing.espresso)
    androidTestImplementation(libs.testing.ui.junit)
    androidTestImplementation(composeBom)

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

}