plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.jbgcomposer.newshub"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.jbgcomposer.newshub"
        minSdk = 28
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
    viewBinding {
        enable = true
    }
}

dependencies {

    val roomVersion = "2.6.1"
    val hiltVersion = "2.50"

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    //Glide
    implementation("com.github.bumptech.glide:glide:4.14.2")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

    //Room
    implementation("androidx.room:room-runtime:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")

    ksp("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    //Test Room
    androidTestImplementation ("androidx.room:room-testing:2.6.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")
    //Test Coroutines
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")

    // Coroutine Lifecycle Scopes
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")

    //Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.6")

    //Hilt-KSP
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    ksp("com.google.dagger:dagger-compiler:$hiltVersion") // Dagger compiler
    ksp ("com.google.dagger:hilt-compiler:$hiltVersion")   // Hilt compiler

    //Paging
    implementation("androidx.paging:paging-runtime-ktx:3.2.1")
    testImplementation("androidx.paging:paging-common-ktx:3.2.1")

    // Dependências de Teste Unitário
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:4.0.0") // Use a versão mais recente compatível
    testImplementation("org.mockito:mockito-inline:4.0.0") // Para suporte a mock de funções finais
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.4.2")
    //Test MockK
    testImplementation ("io.mockk:mockk:1.12.0")
    testImplementation("com.google.truth:truth:1.1.3")

    // For local unit tests HILT
    testImplementation("com.google.dagger:hilt-android-testing:$hiltVersion")
    testAnnotationProcessor("com.google.dagger:hilt-compiler:$hiltVersion")

    // Dependências de Teste do Android (Instrumentação)
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // For instrumentation tests HILT
    androidTestImplementation("com.google.dagger:hilt-android-testing:$hiltVersion")
    androidTestAnnotationProcessor("com.google.dagger:hilt-compiler:$hiltVersion")

    // Dependência do MockWebServer para testes de API
    testImplementation("com.squareup.okhttp3:mockwebserver:5.0.0-alpha.12")
}