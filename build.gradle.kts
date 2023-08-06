// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.github.ben-manes.versions") version "0.47.0"
    alias(libs.plugins.org.jetbrains.kotlin.jvm) apply false
}