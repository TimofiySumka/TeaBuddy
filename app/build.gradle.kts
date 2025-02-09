plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.gms)
}

android {
    namespace = "com.example.teabuddy"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.teabuddy"
        minSdk = 24
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        viewBinding=true

    }
}

dependencies {
    implementation(libs.firebase.storage)
    implementation(libs.play.services.location)
    implementation(libs.places)
    val fragment_version = "1.8.5"
    implementation ("nl.bryanderidder:themed-toggle-button-group:1.4.1")
    implementation ("com.google.android.flexbox:flexbox:3.0.0")
    implementation("com.google.android.gms:play-services-maps:18.2.0")//Гугл мапа
    implementation ("com.squareup.picasso:picasso:2.8")//Завантаження фото
    implementation ("com.google.firebase:firebase-storage:20.2.0")
    implementation ("com.github.markushi:circlebutton:1.1") //Кругла кнопка
    implementation ("de.hdodenhof:circleimageview:3.1.0")//Кругла рамка фото
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore)
    implementation("androidx.fragment:fragment-ktx:$fragment_version")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}