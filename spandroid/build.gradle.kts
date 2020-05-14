plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    kotlin("android.extensions")
}

androidExtensions { isExperimental = true }

android {

    compileSdkVersion(AndroidConfig.compile_sdk)

    defaultConfig {
        minSdkVersion(AndroidConfig.min_sdk)
        targetSdkVersion(AndroidConfig.target_sdk)
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
