# GitMate
# GitMate

GitMate is an Android app that lets you search for GitHub users, view their profiles, and browse their public repositories. Built as a technical recruitment project for the Android Club.

## Features
- 🔐 Firebase Email/Password Authentication (Login & Register)
- 🔍 Search any GitHub user by username
- 👤 View user profile (avatar, bio, followers, following)
- 📁 Browse public repositories with language and star count
- 📱 Clean and modern UI built with Jetpack Compose
- 🧩 MVVM architecture with Coroutines and StateFlow

## Tech Stack
- **Language:** Kotlin
- **UI:** Jetpack Compose (Material 3)
- **Architecture:** MVVM
- **Authentication:** Firebase Auth
- **Networking:** Retrofit2 + Gson (GitHub REST API)
- **Image Loading:** Coil
- **Asynchronous:** Coroutines + Flow

## Setup Instructions
1. Clone the repository
2. Add your `google-services.json` file to the `app/` folder (get it from Firebase Console)
3. Open the project in Android Studio
4. Sync Gradle and run on emulator/device

## Screenshots
(Insert your screenshots here)

## Demo Video
[Link to your video or GIF]

## APK
Download the APK from the [Releases](https://github.com/yourusername/GitMate/releases) section.

## What I Learned
- How to integrate Firebase Authentication
- How to consume REST APIs with Retrofit
- How to manage UI state with Compose + StateFlow
- How to handle navigation in a single-activity app
- How to use Room for local persistence (future feature)
