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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = Versions.jvmTarget
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

    androidTestImplementation(libs.testing.testRunner)
    androidTestImplementation(libs.testing.espresso)



}