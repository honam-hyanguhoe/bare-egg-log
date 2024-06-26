plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    id("kotlin-kapt")
    alias(libs.plugins.hilt)
    alias(libs.plugins.com.google.gms)
}

android {
    namespace = "com.org.egglog.client"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.org.egglog.client"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file(findProperty("STORE_FILE").toString())
            storePassword = findProperty("STORE_PASSWORD").toString()
            keyAlias = findProperty("KEY_ALIAS").toString()
            keyPassword = findProperty("KEY_PASSWORD").toString()
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    kapt {
        correctErrorTypes = true
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)

    // material & material3
    implementation(libs.material3)
    implementation(libs.material)
    runtimeOnly(libs.material.icons.extended)
    implementation(libs.compose.material)

    // junit
    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    // datastore
    implementation(libs.datastore)

    // retrofit & okHttp
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.serialization)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.okhttp)

    // hilt
    implementation(libs.hilt)
    kapt(libs.hilt.compiler)

    // splash
    implementation(libs.splash)

    // ui controller
    implementation(libs.accompanist.insets)

    // workmanager
    implementation(libs.work.runtime.ktx)
    implementation(libs.hilt.workder)

    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":presentation"))
}