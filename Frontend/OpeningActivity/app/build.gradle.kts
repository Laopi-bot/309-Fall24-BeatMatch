plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.openingactivity"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.openingactivity"
        minSdk = 30
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.espresso.intents)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.android.volley:volley:1.2.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.google.android.material:material:1.0.0")
    implementation ("com.google.android.material:material:1.9.0")
    implementation ("androidx.recyclerview:recyclerview:1.3.0")
    implementation ("org.java-websocket:Java-WebSocket:1.5.2")


    // GSON Converter for parsing JSON data
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Swipe card Implementation
    implementation("com.lorentzos.swipecards:library:1.0.9")

}