plugins {

    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    id("io.gitlab.arturbosch.detekt") version "1.23.7"
    kotlin("plugin.serialization") version "1.9.0" apply false
}