import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kmp.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlin.compose)
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.example.x5tech.feature.cardform.resources"
}

kotlin {
    androidLibrary {
        namespace = "com.example.x5tech.feature.cardform"
        compileSdk = 36
        minSdk = 24

        withHostTestBuilder {}

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":model"))
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
            implementation("io.insert-koin:koin-core:4.0.0")
            implementation(compose.runtime)
            implementation(compose.components.resources)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
        }
    }
}
