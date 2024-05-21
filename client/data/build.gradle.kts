plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.hilt)
    id("kotlin-kapt")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.org.egglog.data"
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
    kapt {
        correctErrorTypes = true
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // serialization
    implementation(libs.kotlinx.serialization.json)

    // retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.serialization)
    implementation(libs.okhttp)

    // hilt
    implementation(libs.hilt)
    kapt(libs.hilt.compiler)

    // datastore
    implementation(libs.datastore)

    // lifecycle service
    implementation(libs.androidx.lifecycle.service)

    // paging3
    implementation(libs.paging.runtime)

    // kakao
    implementation(libs.kakao.user)
    implementation(libs.kakao.share)
    implementation(libs.kakao.auth)

    // google
    implementation(libs.firebase.auth)
    implementation(platform(libs.firebase.bom))
    implementation(libs.play.services.auth)
    implementation("com.google.firebase:firebase-storage-ktx")

    // naver
    implementation(libs.naver.oauth)

    // room
    implementation(libs.room.runtime)
    implementation(libs.room.paging)
    kapt(libs.room.compiler)

    // workmanager
    implementation(libs.work.runtime.ktx)
    implementation(libs.hilt.workder)

    implementation(project(":presentation"))
    implementation(project(":domain"))
}
