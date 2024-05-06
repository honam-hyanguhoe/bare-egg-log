plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    id("kotlin-kapt")
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.org.egglog.presentation"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        jvmTarget = "17"
    }

    // 설정해줌
    buildFeatures {
        compose = true
    }

    // 설정해줌
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    kapt {
        correctErrorTypes = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    // material & material3
    implementation(libs.material3)
    implementation(libs.material)
    implementation(libs.material.icons.extended)
    implementation(libs.material.icons.core)
    implementation(libs.compose.material)

    // hilt
    implementation(libs.hilt)
    kapt(libs.hilt.compiler)

    // paging3
    implementation(libs.paging.runtime)
    implementation(libs.paging.compose)

    // navigation
    implementation(libs.navigation.compose)
    implementation(libs.hilt.navigation.compose)

    // coil
    implementation(libs.coil)
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)
    implementation(libs.coil.svg)

    // snapper (wheel-picker)
    implementation(libs.snapper)

    // orbit
    implementation(libs.orbit.core)
    implementation(libs.orbit.compose)
    implementation(libs.orbit.viewmodel)

    // splash
    implementation(libs.splash)

    // kakao
    implementation(libs.kakao.user)
    implementation(libs.kakao.share)
    implementation(libs.kakao.auth)

    // google
    implementation(libs.firebase.auth)
    implementation(platform(libs.firebase.bom))
    implementation(libs.play.services.auth)

    // naver
    implementation(libs.naver.oauth)

    implementation(project(":domain"))
}