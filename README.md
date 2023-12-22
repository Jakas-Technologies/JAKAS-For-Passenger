<div align="justify">

# JAKAS (Jaringan Angkutan Kota Cerdas)

This application is a Bangkit 2023 batch 2 capstone project, developed by group CH2-PS413, built using Kotlin in Android Studio for Jakas Passenger.

## Overview

Jakas is a mobile application developed as a solution to the severe traffic congestion issues in Indonesian cities, particularly focusing on the case study of Bandung. The app aims to address the overreliance on private vehicles by optimizing the Angkot system, a form of public transport, to improve urban transportation efficiency.

## Main Features
* Find Nearest Angkot
* Digital Payment
* Interactive Map for Location Display

## System Requirements

* Android Studio Hedgehog
* Gradle version 8.0
* Compile SDK 34
* Android gradle plugin 8.1.1

## How to Use

1. Clone this repository into Android Studio.
2. Ensure that the project configuration has been set up correctly.
3. Run the project on an emulator or physical device.

## Contribution

We warmly welcome contributions from the community. If you would like to contribute, please follow these steps:

1. Create a fork of this repository.
2. Implement your changes.
3. Submit a pull request to the development branch. ? atau branch apa yang ada buat development

## Development Guidelines

### Dependencies

This project uses several main dependencies, including:

### Barcode Scanner ML Kit
```kotlin
implementation "com.google.mlkit:barcode-scanning:17.2.0"
```

### CameraX
```kotlin
implementation "androidx.camera:camera-core:1.3.1"
implementation "androidx.camera:camera-camera2:1.3.1"
implementation "androidx.camera:camera-view:1.3.1"
implementation "androidx.camera:camera-lifecycle:1.3.1"
```

### Serialization
```kotlin
implementation "org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0"
```

### Socket
```kotlin
implementation("io.socket:socket.io-client:2.0.0") {
    exclude(group = "org.json", module = "json")
}
```

### WorkManager
```kotlin
implementation "androidx.work:work-runtime-ktx:2.9.0"
```

### Service
```kotlin
implementation "androidx.lifecycle:lifecycle-service:2.6.2"
```

### EasyPermission
```kotlin
implementation "com.vmadalin:easypermissions-ktx:1.0.0"
```

### Maps
```kotlin
implementation "com.google.android.gms:play-services-maps:18.2.0"
implementation "com.google.android.libraries.places:places:3.3.0"
implementation "com.google.maps.android:android-maps-utils:2.3.0"
implementation "com.google.maps:google-maps-services:2.2.0"
implementation "com.google.maps.android:places-ktx:3.0.0"
```

### Dagger - Hilt
```kotlin
implementation "com.google.dagger:hilt-android:2.48.1"
ksp("com.google.dagger:dagger-compiler:2.48.1")
ksp("com.google.dagger:hilt-compiler:2.48.1")

implementation "androidx.hilt:hilt-work:1.1.0"
ksp("androidx.hilt:hilt-compiler:1.1.0")
```

### Support
```kotlin
implementation "androidx.legacy:legacy-support-v4:1.0.0"
implementation "androidx.annotation:annotation:1.7.0"
implementation "org.slf4j:slf4j-simple:1.7.25"
coreLibraryDesugaring "com.android.tools:desugar_jdk_libs:2.0.4"
```

### Data Preference
```kotlin
implementation "androidx.datastore:datastore-preferences:1.0.0"
```

### Room/KSP
```kotlin
ksp("androidx.room:room-compiler:2.6.0")
```

### Logging Interceptor
```kotlin
implementation "com.squareup.okhttp3:logging-interceptor:4.11.0"
```

### Timber
```kotlin
implementation "com.jakewharton.timber:timber:4.7.1"
```

### Retrofit
```kotlin
implementation "com.squareup.retrofit2:retrofit:2.9.0"
implementation "com.squareup.retrofit2:converter-gson:2.9.0"
implementation "com.google.code.gson:gson:2.10.1"
```

### Livedata + ViewModel
```kotlin
implementation "androidx.lifecycle:lifecycle-livedata-ktx:2.6.2"
implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2"
```

### Core
```kotlin
implementation "androidx.core:core-ktx:1.12.0"
implementation "androidx.appcompat:appcompat:1.6.1"
implementation "androidx.activity:activity-ktx:1.8.2"
```

### Material
```kotlin
implementation "androidx.constraintlayout:constraintlayout:2.1.4"
implementation "com.google.android.material:material:1.10.0"
```

### Fragment + Navigation
```kotlin
implementation "androidx.fragment:fragment-ktx:1.6.2"
implementation "androidx.navigation:navigation-ui-ktx:2.7.5"
implementation "androidx.navigation:navigation-fragment-ktx:2.7.5"
```

### Testing
```kotlin
testImplementation "junit:junit:4.13.2"
androidTestImplementation "androidx.test.ext:junit:1.1.5"
androidTestImplementation "androidx.test.espresso:espresso-core:3.5.1"
```

### Coding Conventions

We adhere to Kotlin standard coding conventions and follow a specific style guide. Make sure to comply with these guidelines when contributing.

<div/>
