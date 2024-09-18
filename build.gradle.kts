// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // Apply plugin aliases for Android application and Kotlin Android plugins
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false

    // Apply Google services plugin version
    id("com.google.gms.google-services") version "4.4.2" apply false
}

buildscript {
    // Add repositories for dependency resolution
    repositories {
        google()
        mavenCentral()
        jcenter()
    }
    // Add dependencies for classpath configurations
    dependencies {
        classpath(libs.gradle) // Ensure this version matches your compileSdk version
        classpath(libs.kotlin.gradle.plugin) // Ensure this matches your Kotlin version
        classpath(libs.google.services)
    }
}


