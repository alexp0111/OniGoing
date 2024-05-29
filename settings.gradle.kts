pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://jitpack.io")
        }
    }

    versionCatalogs {
        create("localDeps") {
            library("kotlin", "androidx.core:core-ktx:1.12.0")
            library("lifecycleRuntime", "androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
            library("lifecycleViewModel", "androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
            library("appCompat", "androidx.appcompat:appcompat:1.6.1")
            library("materialDesign", "com.google.android.material:material:1.11.0")
            library("constraintLayout", "androidx.constraintlayout:constraintlayout:2.1.4")
            library("swipeRefreshLayout", "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

            library("dagger", "com.google.dagger:dagger:2.51")
            library("daggerCompiler", "com.google.dagger:dagger-compiler:2.51")

            library("glide", "com.github.bumptech.glide:glide:3.7.0")
            library("gson", "com.google.code.gson:gson:2.10.1")
            library("apollo", "com.apollographql.apollo3:apollo-runtime:3.8.3")
            library("cicerone", "com.github.terrakok:cicerone:7.1")
            library("room", "androidx.room:room-ktx:2.6.1")
            library("roomCompiler", "androidx.room:room-compiler:2.6.1")
            library("roundChart", "com.github.PhilJay:MPAndroidChart:v3.1.0")
            library("htmlParser", "com.github.NightWhistler:HtmlSpanner:0.4")
        }
        create("unitTestDeps") {
            library("jUnit", "junit:junit:4.13.2")
            library("robolectric", "org.robolectric:robolectric:4.4")
            library("mockito", "org.mockito:mockito-core:5.1.1")
            library("core", "androidx.test:core-ktx:1.5.0")
        }
        create("uiTestDeps") {
            library("extJUnit", "androidx.test.ext:junit:1.1.5")
            library("espresso", "androidx.test.espresso:espresso-core:3.5.1")
            library("rules", "androidx.test:rules:1.5.0")
            library("runner", "androidx.test:runner:1.5.2")
        }
    }
}

rootProject.name = "OniGoing"
include(":app")
 