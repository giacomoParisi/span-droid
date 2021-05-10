plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

android {

    compileSdk = AndroidConfig.compile_sdk

    defaultConfig {
        applicationId = "com.giacomoparisi.spandroid"
        minSdk = AndroidConfig.min_sdk
        targetSdk = AndroidConfig.target_sdk
        versionCode = AndroidConfig.version_code
        versionName = AndroidConfig.version_name
        testInstrumentationRunner = AndroidConfig.test_instrumentation_runner
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    /* --- kotlin --- */

    implementation (Libs.kotlin_stdlib_jdk7)
    implementation (Libs.core_ktx)

    /* --- android --- */

    implementation (Libs.appcompat)

    /* --- test --- */

    testImplementation (Libs.junit_junit)
    androidTestImplementation (Libs.androidx_test_ext_junit)
    androidTestImplementation (Libs.espresso_core)

}
