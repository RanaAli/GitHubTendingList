@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("java-library")
    alias(libs.plugins.org.jetbrains.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    jvmToolchain(21) // This tells Kotlin to use JDK 17 for compilation
    // which then makes its default jvmTarget compatible.
}

dependencies {
    implementation(libs.kotlinx.coroutines.test)
    implementation(libs.hilt.android.compiler)
}

