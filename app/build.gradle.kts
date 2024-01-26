import org.jetbrains.kotlin.gradle.utils.loadPropertyFromResources
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("org.jetbrains.kotlin.kapt")
    id("com.google.dagger.hilt.android")
    id ("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")

}

android {
    namespace = "com.example.okeyscores"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.okeyscores"
        minSdk = 24
        targetSdk = 33
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
            buildConfigField("String", "API_KEY", "\"${project.findProperty("API_KEY")}\"")

        }
        debug{
            buildConfigField("String", "API_KEY", "\"${project.findProperty("API_KEY")}\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }
    kotlinOptions {
        jvmTarget = "18"
    }
    buildFeatures{
        android.buildFeatures.buildConfig = true
        viewBinding = true
    }

}
dependencies {
    implementation("com.google.firebase:firebase-auth:22.3.0")
    implementation("com.google.firebase:firebase-database-ktx:20.3.0")
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("com.google.firebase:firebase-firestore:24.10.0")
    implementation("com.google.firebase:firebase-messaging:23.4.0")
    val lifeCycleExtensionVersion = "2.2.0"
    val retrofitVersion = "2.3.0"
    val supportVersion = "28.0.0"

    val rxJavaVersion = "2.1.1"
    val roomVersion = "2.4.0"
    val navVersion = "2.2.1"
    val preferencesVersion = "1.1.0"
    implementation("androidx.core:core-ktx:1.10.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("androidx.room:room-ktx:$roomVersion")
    implementation ("androidx.room:room-testing:$roomVersion")
    implementation("androidx.lifecycle:lifecycle-extensions:$lifeCycleExtensionVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifeCycleExtensionVersion")
    implementation ("androidx.room:room-runtime:$roomVersion")
    implementation ("androidx.legacy:legacy-support-v4:1.0.0")
    implementation ("androidx.room:room-ktx:$roomVersion")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.0")
    implementation ("androidx.fragment:fragment-ktx:1.5.2")

    implementation ("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation ("androidx.navigation:navigation-ui-ktx:$navVersion")
    kapt ("androidx.room:room-compiler:$roomVersion") // Bu satırı ekleyin

    implementation ("com.google.android.material:material:1.1.0")

    implementation ("io.reactivex.rxjava2:rxjava:$rxJavaVersion")
    implementation ("io.reactivex.rxjava2:rxandroid:$rxJavaVersion")

    //noinspection GradleCompatible
    //noinspection GradleCompatible
    implementation ("androidx.preference:preference:$preferencesVersion")
    implementation("com.github.leandroborgesferreira:loading-button-android:2.3.0")


    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")


    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation ("de.hdodenhof:circleimageview:3.1.0")

}
kapt {
    correctErrorTypes = true
}
