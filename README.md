# Nerde Yesem

<!-- TABLE OF CONTENTS -->
<details open="open">
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#screenshots">Screenshots</a></li>
        <li><a href="#application-architecture">Application Architecture</a></li>
        <li><a href="#features">Features</a></li>
        <li><a href="#libraries">Libraries</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#license">License</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

Nerde Yesem application is a sample project application that pulls and lists the restaurants nearby with Zomato API.

### Screenshots

![preview](https://user-images.githubusercontent.com/43733328/103468526-db891b80-4d6a-11eb-840f-e220d5c5d833.png)


### Application Architecture

This application is made with Model View View Model (MVVM) architecture in single activity structure.
With the benefit of the single activity structure, we got the most of the navigation components.


### Features

This application contains the following features.
* **Firebase Authentication integrated with Crashlytics.** Contains exception handling for possible situations
* **Location information.** Obtaining location information by getting location permission from the user and asking to activate GPS. Exeption handling for possible situations
* **Distance Calculation.** With the calculation of the travelled distance, a new request from the API is not required for movements below 1 km. Significant performance improvement has been achieved in UI recreation, returning to the restaurants screen from details, and movements over small distances.
* **Zomato API.** After obtaining the location information of the user, showing the information of the nearby restaurants in the RecyclerView from close to far.
* **Zomato Reviews.** When user selects a restaurant, pulling restaurant reviews from the Zomato API with restaurant ID and listing them in RecyclerView.
* **Dependency Injection with Hilt**
* **Animations.** in RecyclerViews and fragment transitions.
* **View Binding.**


I created the table below to help you to understand which classes in this project are used by which feature.
![Class Table](https://user-images.githubusercontent.com/43733328/103480011-c0f28900-4de2-11eb-8c76-dac4c734f8dd.png)


### Libraries

* **Material Design** 
```sh
    implementation 'com.google.android.material:material:1.3.0'
```
* **Hilt** 
```sh
    implementation "com.google.dagger:hilt-android:$hilt_version"
    annotationProcessor "com.google.dagger:hilt-compiler:$hilt_version"
```
* **Navigation Components**
```sh
    def nav_version = "2.3.2"
    implementation "androidx.navigation:navigation-fragment:$nav_version"
    implementation "androidx.navigation:navigation-ui:$nav_version"
    implementation "androidx.navigation:navigation-dynamic-features-fragment:$nav_version" 
    androidTestImplementation "androidx.navigation:navigation-testing:$nav_version" 
```
* **Firebase**
```sh
    implementation platform('com.google.firebase:firebase-bom:26.2.0') 
    implementation 'com.google.firebase:firebase-analytics'
    implementation 'com.google.firebase:firebase-auth'
    implementation 'com.google.firebase:firebase-crashlytics' 
```
* **Lifecycle**
```sh
    def lifecycle_version = "2.3.0"
    implementation "androidx.lifecycle:lifecycle-viewmodel:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-livedata:$lifecycle_version"  
    implementation "androidx.lifecycle:lifecycle-runtime:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version"
```
* **Retrofit**
```sh
    def retrofit_version = "2.9.0"
    implementation "com.squareup.retrofit2:retrofit:$retrofit_version"
    implementation "com.squareup.retrofit2:converter-gson:$retrofit_version"
```
* **Glide**
```sh
     def glide_version = "4.11.0"
    implementation "com.github.bumptech.glide:glide:$glide_version"
    annotationProcessor "com.github.bumptech.glide:compiler:$glide_version"
 ```
 
 * **Gsm Play Services**
 ```sh
    implementation 'com.google.android.gms:play-services-location:18.0.0'
  ```
<!-- GETTING STARTED -->
## Getting Started

To get a local copy up and running follow these simple example steps.
### Prerequisites

This application requires minimum Android 5.0 (API level 21).
### Installation

1. Get a free API Key from https://developers.zomato.com/api

2. Clone the repo
   ```sh
   git clone https://github.com/Veisy/Nerde-Yesem.git
   ```
3. Enter your API Key in ZomatoApiService.API_KEY field
   ```JS
    // Enter your valid Zomato Api Key here.
    // Delete BuildConfig.ApiKey, and replace "yourApiKey'
    String API_KEY = BuildConfig.ApiKey;
   ```
4. You need to delete the following lines from the build.gradle(Module) file. 
You added your API key in ZomatoApiService, there is no ZOMATO_APP_KEY in your property.grandle file as I have.
    ```JS
    debug {
              buildConfigField 'String' , "ApiKey" , ZOMATO_API_KEY
    }
    release {
              buildConfigField 'String' , "ApiKey" , ZOMATO_API_KEY
   }
   ```

<!-- USAGE EXAMPLES -->
## Usage
![Screenshot_20210102-141127_Nerde Yesem](https://user-images.githubusercontent.com/43733328/103456195-e5783380-4d04-11eb-88f5-b6bbae8c989d.jpg)

![Screenshot_20210103-164458_Nerde Yesem](https://user-images.githubusercontent.com/43733328/103480110-5aba3600-4de3-11eb-8e91-58fda074d5df.jpg)


![Screenshot_20210102-141219_Nerde Yesem](https://user-images.githubusercontent.com/43733328/103456205-fcb72100-4d04-11eb-9bad-59e71ecbb3b9.jpg)


<!-- LICENSE -->
## License

Distributed under the  Apache-2.0 License. See `LICENSE` for more information.
