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
<img width="323" height="700" alt="Screenshot 2026-06-16 193026" src="https://github.com/user-attachments/assets/8df7f6ea-106d-44ec-af13-ecc180f44e0d" />
<img width="371" height="708" alt="Screenshot 2026-06-16 193009" src="https://github.com/user-attachments/assets/3daac914-3508-4f0a-8c3c-2753495bc6c3" />
<img width="357" height="717" alt="Screenshot 2026-06-16 192911" src="https://github.com/user-attachments/assets/6c937f51-3540-4da1-8248-7408318a414b" />
<img width="335" height="697" alt="Screenshot 2026-06-16 193057" src="https://github.com/user-attachments/assets/2600317a-09d8-4cbb-b9d3-57804478cf1d" />
<img width="328" height="700" alt="Screenshot 2026-06-16 193042" src="https://github.com/user-attachments/assets/21f11f6d-3638-41f4-8e52-2d79f22eec52" />


## Demo Video
<img src="GitMateDemo.gif" width="400" height="auto" alt="App Demo">

## APK
Download the APK from the [Releases](https://github.com/Animesh46/GitMate/releases) section.

## What I Learned
- How to integrate Firebase Authentication
- How to consume REST APIs with Retrofit
- How to manage UI state with Compose + StateFlow
- How to handle navigation in a single-activity app
- How to use Room for local persistence (future feature)
