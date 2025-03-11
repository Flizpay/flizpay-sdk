/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Kotlin application project to get you started.
 * For more details on building Java & JVM projects, please refer to https://docs.gradle.org/8.13/userguide/building_java_projects.html in the Gradle documentation.
 */

plugins {
    id("com.android.application") version "8.7.3"
    id("org.jetbrains.kotlin.android") version "2.1.10"
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"
}

repositories {
    // Use Maven Central for resolving dependencies.
    google()
    mavenCentral()
}

android {
    namespace = "org.example"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }

    kotlinOptions { jvmTarget = "18" }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // This dependency is used by the application.
    implementation(libs.guava)
    implementation(files("../../FlizpaySDK/flizpaysdk/build/libs/FlizpaySDK.jar"))
    implementation(libs.appcompat)
    implementation(libs.core.ktx)
    implementation(libs.activity.ktx)

    // ✅ OkHttp3
    implementation(libs.okhttp) // Use the latest version

    // ✅ Jetpack Compose (UI, Material, and runtime)
    implementation(libs.ui)
    implementation(libs.material)
    implementation(libs.material3)
    implementation(libs.ui.tooling.preview)
    implementation(libs.activity.compose)
    implementation(libs.gson)
    implementation(libs.security.crypto)

    // ✅ Compose Compiler (ensure compatibility)
    implementation(libs.compiler)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui.graphics)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
}
