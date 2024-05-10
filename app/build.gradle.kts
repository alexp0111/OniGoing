plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.apollographql.apollo3")
    id("kotlin-kapt")
}

android {
    namespace = "ru.alexp0111.onigoing"
    compileSdk = 34

    defaultConfig {
        applicationId = "ru.alexp0111.onigoing"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures {
        viewBinding = true
    }
}

apollo {
    service("service") {
        packageName.set("ru.alexp0111.onigoing.anilist")
    }
}

dependencies {
    /** Base core libraries */
    implementation(localDeps.kotlin)
    implementation(localDeps.lifecycleRuntime)
    implementation(localDeps.lifecycleViewModel)
    implementation(localDeps.appCompat)
    implementation(localDeps.materialDesign)
    implementation(localDeps.constraintLayout)
    implementation(localDeps.swipeRefreshLayout)

    /** Dagger2 */
    implementation(localDeps.dagger)
    kapt(localDeps.daggerCompiler)

    /** Glide */
    implementation(localDeps.glide)

    /** Cicerone */
    implementation(localDeps.cicerone)

    /** Gson */
    implementation(localDeps.gson)

    /** Apollo */
    implementation(localDeps.apollo)

    /** Room */
    implementation(localDeps.room)
    kapt(localDeps.roomCompiler)

    /** Unit testing */
    testImplementation(unitTestDeps.jUnit)
    testImplementation(unitTestDeps.robolectric)
    testImplementation(unitTestDeps.mockito)

    /** Instrumented testing */
    androidTestImplementation(uiTestDeps.extJUnit)
    androidTestImplementation(uiTestDeps.espresso)
    androidTestImplementation(uiTestDeps.rules)
    androidTestImplementation(uiTestDeps.runner)
}