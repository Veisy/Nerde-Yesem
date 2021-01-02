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

![Nerdeyesem-preview](https://user-images.githubusercontent.com/43733328/103456108-26bc1380-4d04-11eb-90a6-a9c699f2f74b.jpg)


### Application Architecture

This application is made with the architecture of Model View View Model (MVVM) in single activity structure.
With the benefit of the single activity structure, we got the most of the navigation components.


### Features

This application contains the following features.
* **Firebase Authentication integrated with Crashlytics.** Contains exception handling for possible situations
* **Location information.** Obtaining location information by getting location permission from the user and asking to activate GPS. Exeption handling for possible situations
* **Zomato API.** After obtaining the location information of the user, showing the information of the nearby restaurants in the RecyclerView from close to far.
* **Zomato Reviews.** When user selects a restaurant, pulling restaurant reviews from the Zomato API with restaurant ID and listing them in RecyclerView.
* **Animations.** in RecyclerViews and fragment transitions.
* **View Binding.**


I created the table below to help you to understand which classes in this project are used by which feature.
<br>

![Class Table](https://user-images.githubusercontent.com/43733328/103459529-2bdb8b80-4d21-11eb-8d18-8125d0e4848a.png)


### Libraries

* **Material Design** 
```sh
com.google.android.material:material:1.2.1
```
* **Navigation Components**
```sh
    def nav_version = "2.3.2"
    implementation "androidx.navigation:navigation-fragment:$nav_version"
    implementation "androidx.navigation:navigation-ui:$nav_version"
    implementation "androidx.navigation:navigation-dynamic-features-fragment:$nav_version" 
    androidTestImplementation "androidx.navigation:navigation-testing:$nav_version" 
    implementation "androidx.navigation:navigation-compose:1.0.0-alpha04"
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
    def lifecycle_version = "2.2.0"
    implementation "androidx.lifecycle:lifecycle-viewmodel:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-livedata:$lifecycle_version"  
    implementation "androidx.lifecycle:lifecycle-runtime:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version"
```
* **Retrofit**
```sh
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
```
* **Glide**
```sh
    implementation 'com.github.bumptech.glide:glide:4.11.0'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.11.0'
 ```
 
 * **Gsm Play Services**
 ```sh
    implementation 'com.google.android.gms:play-services-location:17.1.0'
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
3. Enter your API Key in MainActivity static String field
   ```JS
    // Enter your valid Zomato Api Key here.
    // Delete BuildConfig.ApiKey, and replace "yourApiKey'
    public static final String API_KEY = BuildConfig.ApiKey;
   ```
  
 

<!-- USAGE EXAMPLES -->
## Usage
![Screenshot_20210102-141127_Nerde Yesem](https://user-images.githubusercontent.com/43733328/103456195-e5783380-4d04-11eb-88f5-b6bbae8c989d.jpg)

![Screenshot_20210102-141204_Nerde Yesem](https://user-images.githubusercontent.com/43733328/103456203-f32db900-4d04-11eb-8d8e-858cfea6b247.jpg)

![Screenshot_20210102-141219_Nerde Yesem](https://user-images.githubusercontent.com/43733328/103456205-fcb72100-4d04-11eb-9bad-59e71ecbb3b9.jpg)


<!-- LICENSE -->
## License

Distributed under the  Apache-2.0 License. See `LICENSE` for more information.
