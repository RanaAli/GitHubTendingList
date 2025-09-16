@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.com.android.library)
    id("kotlin-kapt")
    alias(libs.plugins.org.jetbrains.kotlin.android)
}

android {
    namespace = "com.rana.data"
    compileSdk = Versions.compileAndTargetSDK

    defaultConfig {
        minSdk = Versions.minSDK

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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlin {
        jvmToolchain(21) // This tells Kotlin to use JDK 17 for compilation
        // which then makes its default jvmTarget compatible.
    }

    // Enable Room annotation processing for tests
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    api(project(mapOf("path" to ":domain")))

    implementation(libs.androidx.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)

    implementation(libs.retrofit)
    implementation(libs.retrofit.okhttp3)
    implementation(libs.retrofit.gson)
    implementation(libs.retrofit.okhttp3.logging.interceptor)

    implementation(libs.hilt.android)

    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    testImplementation(libs.testing.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.mockito.core) // Added
    testImplementation(libs.mockito.kotlin) // Added

    // Added for Robolectric tests
    testImplementation(libs.core.ktx)
    testImplementation(libs.robolectric)

    // Room testing dependencies
    testImplementation(libs.androidx.room.testing)
    kaptTest(libs.androidx.room.compiler)
    testImplementation(libs.androidx.core)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.robolectric)

    androidTestImplementation(libs.testing.testRunner)
    androidTestImplementation(libs.testing.espresso)

}