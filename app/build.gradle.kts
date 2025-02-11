plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.thangtien.rxandroidandretrofit"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.thangtien.rxandroidandretrofit"
        minSdk = 33
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
            signingConfig = signingConfigs.getByName("debug")
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.extJunit)
    androidTestImplementation(libs.espressoCore)
    implementation(libs.gson)
    implementation(libs.retrofit)
    implementation(libs.retrofitConverterGson)
    implementation(libs.rxjava)
    implementation(libs.rxandroid)
    implementation(libs.retrofitAdapterRxjava3)
    implementation("androidx.recyclerview:recyclerview:1.3.2")
}