# Quizzy - Android Application

## ✨ Features

- **User Authentication:** Secure login flow with Login, Register, Forgot password features.
- **Impressive Dashboard:** Displays user-specific information and summaries.
- **Networking Layer:** Efficiently communicates with a backend API using Retrofit.
- **Modern UI:** Clean and intuitive user interface built with Jetpack Compose.

## 🛠️ Tech Stack & Architecture

- **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) for a fully declarative UI.
- **Architecture:** MVVM (Model-View-ViewModel) to separate UI from business logic.
- **Dependency Injection:** [Koin](https://insert-koin.io/) for managing dependencies and decoupling components.
- **Asynchronous Operations:** [Kotlin Coroutines & Flow](https://developer.android.com/kotlin/coroutines) for managing background threads and handling asynchronous data streams.
- **Networking:** [Retrofit2](https://square.github.io/retrofit/) & [OkHttp3](https://square.github.io/okhttp/) for making API calls, with a logging interceptor for easy debugging.
- **Navigation:** [Jetpack Navigation for Compose](https://developer.android.com/jetpack/compose/navigation) to handle in-app navigation.
- **Authentication:** [Firebase Authentication](https://firebase.google.com/docs/auth) for user management.

## ⚙️ Setup and Installation

Follow these steps to get the project up and running on your local machine.

### 1. Prerequisites

- Android Studio (latest stable version recommended)
- A configured Android emulator or a physical device

### 2. Clone the Repository

```bash
git clone https://github.com/your-username/Quizzy2.git
cd Quizzy2
```

### 3. Firebase Configuration

This project uses Firebase for authentication.

1.  Go to the [Firebase Console](https://console.firebase.google.com/) and create a new project.
2.  Add a new Android app to your Firebase project with the package name `com.example.quizzy`.
3.  Download the `google-services.json` file provided during the setup process.
4.  Place the downloaded `google-services.json` file inside the **`/app`** directory of the project.
5.  In the Firebase Console, navigate to the **Authentication** section and enable the sign-in methods you need (e.g., Email/Password).

### 4. API Base URL

The project requires a backend service to function correctly. The base URL for this service is configured in the `app/build.gradle.kts` file.

Open `app/build.gradle.kts` and replace the placeholder URL with your actual backend URL:

```kotlin
android {
    // ...
    buildTypes {
        release {
            // ...
            buildConfigField("String", "BASE_URL", "\"https://your.actual.api.url/\"")
        }
        debug {
            buildConfigField("String", "BASE_URL", "\"https://your.actual.api.url/\"")
        }
    }
    // ...
}
```

### 5. Build and Run

1.  Open the project in Android Studio.
2.  Let Android Studio sync the Gradle files.
3.  Run the app on your emulator or physical device.

