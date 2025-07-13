// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}

dependencies {
    // Other project-level dependencies
}
buildscript {
    repositories {
        google()
        mavenCentral() // Make sure this is included
    }

    dependencies {
        classpath(libs.hilt.android.gradle.plugin) // Use the correct Hilt version
    }
}