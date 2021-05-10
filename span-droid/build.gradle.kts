plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("maven-publish")
}
group = "com.github.giacomoparisi"

android {

    compileSdk = AndroidConfig.compile_sdk

    defaultConfig {
        minSdk = AndroidConfig.min_sdk
        targetSdk = AndroidConfig.target_sdk
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

    implementation(Libs.kotlin_stdlib_jdk7)
    implementation(Libs.core_ktx)

    /* --- android --- */

    implementation(Libs.appcompat)

    /* --- test --- */

    testImplementation(Libs.junit_junit)
    androidTestImplementation(Libs.androidx_test_ext_junit)
    androidTestImplementation(Libs.espresso_core)

}

tasks {

    register("androidSourcesJar", Jar::class) {
        archiveClassifier.set("sources")
        from(project.android.sourceSets.getByName("main").java.name)
    }

}

afterEvaluate {
    publishing {
        publications {
            // Creates a Maven publication called "release".
            create<MavenPublication>("release") {

                artifact("$buildDir/outputs/aar/${project.name}-release.aar")
                artifact(tasks.getByName("androidSourcesJar"))
                // You can then customize attributes of the publication as shown below.
                group = "com.github.giacomoparisi"
                artifactId = "span-droid"
                version = "2.2"
            }
        }
    }
}