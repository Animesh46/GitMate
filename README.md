# GitMate - AI-Powered GitHub Code Explorer

GitMate is a modern Android app that lets you browse GitHub repositories and **understand any code file** using AI. Built for the Android Club Technical Recruitment 2026.

## Features

- 🔐 **Firebase Authentication** – Email/Password login and registration
- 🔍 **GitHub User Search** – Find any GitHub user instantly
- 👤 **Profile & Repos** – View user bios, followers, and public repositories
- 📁 **Repo File Browser** – Browse the complete file tree of any public repository
- 🧠 **AI Code Explainer** – Tap any file and get an instant, beginner-friendly explanation powered by DeepSeek V4 Pro (NVIDIA NIM)
- 📱 **Modern UI** – Built entirely with Jetpack Compose (Material 3)
- 🏗️ **Clean Architecture** – MVVM with StateFlow and Coroutines

## Tech Stack

- **Language:** Kotlin
- **UI Toolkit:** Jetpack Compose (Material 3)
- **Architecture:** MVVM (Model-View-ViewModel)
- **Authentication:** Firebase Auth
- **Networking:** Retrofit2 + OkHttp (GitHub REST API)
- **AI Backend:** NVIDIA NIM (DeepSeek V4 Pro)
- **Asynchronous:** Kotlin Coroutines + StateFlow
- **Image Loading:** Coil

## Setup Instructions

1. **Clone the repository**:
   ```
   git clone https://github.com/Animesh46/GitMate.git
   ```

2. **Add your `google-services.json`**:
   - Download it from your Firebase Console and place it in the `app/` folder.

3. **Add your NVIDIA API Key**:
   - Create a free account at [build.nvidia.com](https://build.nvidia.com).
   - Generate an API key.
   - Open `local.properties` and add:
     ```
     NVIDIA_API_KEY=your_key_here
     ```

4. **Open in Android Studio** and sync Gradle.

5. **Run** the app on an emulator or physical device.

## Screenshots

| Login | Home / Search | File Browser | AI Explainer |
|-------|---------------|--------------|--------------|
| ![Login](login.jpg) | ![Home](home.jpg) | ![Browser](browser.jpg) | ![Explainer](explainer.jpg) |

*Replace the filenames (login.jpg, home.jpg, etc.) with your actual screenshot filenames.*

## Demo Video

Watch the demo: [GitMate Demo](GitMateDemo.gif)


## Download APK

Download the latest release APK from the [Releases](https://github.com/Animesh46/GitMate/releases) section.

## What I Learned

- Integrating Firebase Authentication with Jetpack Compose
- Consuming REST APIs with Retrofit and handling JSON responses
- Managing UI state with `StateFlow` and background threads with Coroutines
- Navigating between composable screens using Jetpack Navigation
- Integrating third-party AI APIs (NVIDIA NIM) for real-time code analysis
- Solving network security and timeout issues
