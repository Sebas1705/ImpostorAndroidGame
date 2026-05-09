# Android Modular Template

A starter template for Android Studio projects with a **mixed modularization architecture**. This repository provides a scalable, maintainable, and efficient way to structure your Android application using a combination of **feature, library, and core modules**.

## 🚀 Features
- **Modularized architecture** for better scalability and separation of concerns.
- **Mixed modularization approach** (feature scheme + layers scheme).
- **Multi-module Gradle setup** for optimized build times.
- **Dependency injection** ready (Dagger/Hilt).
- **MVVM architecture** with ViewModel and Stateflow.
- **MVI architecture** with ViewModel, State and Intent.
- **Navigation Component** for handling navigation between modules.
- **Jetpack Libraries** (Room, Retrofit, Coroutines, etc.).

## 📂 Project Structure

```
📦 android-modular-template
 ┣ 📂 app                # Application module (entry point)
 ┣ 📂 build-logic        # Gradle builder module
 │  ┗  📂 convention          # Plugin and libs module
 ┣ 📂 core               # Core module for shared functionalities
 │  ┣  📂 common              # Common logic 
 │  ┣  📂 designsystem        # Common Components
 │  ┣  📂 resources           # Common Resources
 │  ┗  📂 ui                  # Theme
 ┣ 📂 data               # Data layer module
 │  ┗  📂 data                # Some access data modules (room, firebase, retrofit, etc)
 ┣ 📂 domain             # Domain layer module
 │  ┣  📂 mappers             # Mappers from data models to domain models
 │  ┣  📂 models              # Models from domain layer
 │  ┣  📂 services            # App services (Messaging, background music)
 │  ┗  📂 usescases           # Usescases for viewmodel from features
 |      ┗  📂 usescases            # Some usescases modules (analytics, settings, etc)
 ┣ 📂 feature            # presentation using features scheme
 │  ┣  📂 main                # Main nav feature
 │  ┗  📂 features            # Some features modules (splash, mvvmsample, mvisample, etc)
 ┣ 📂 gradle             # Gradle configuration module
 │  ┣  📂 wrapper             # Gradle wrapper
 │  ┗  📜 libs.versions.toml  # Dependency version controller
 ┣ 📜 .gitignore         # Ignore settings for git
 ┣ 📜 build.gradle.kts   # Project build.gradle
 ┣ 📜 gradle.properties  # Gradle properties
 ┣ 📜 gradlew            
 ┣ 📜 gradlew.bat
 ┣ 📜 local.properties  
 ┗ 📜 settings.gradle.kts
```

## 🛠 Installation & Setup

1. **Clone the repository:**
   ```sh
   git clone https://github.com/YOUR_GITHUB/android-modular-template.git
   cd android-modular-template
   ```

2. **Open in Android Studio:**
   - Open Android Studio.
   - Select `Open an Existing Project`.
   - Navigate to the cloned folder and open it.

3. **Sync dependencies:**
   - Click `Sync Now` when prompted in Android Studio.
   - Alternatively, run:
     ```sh
     ./gradlew clean build
     ```

4. **Run the project:**
   - Select a device/emulator and click `Run` ▶️.

## 📌 Modularization Approach
- **Feature Modules** (`feature/main`, `feature/features`): Contain UI and logic specific to a particular feature.
- **Core Module** (`core`): Contains shared logic like database, authentication, and common utilities.
- **Data Module** (`data`): Contains access data modules such as Room, Firebase, and Retrofit.
- **Domain Module** (`domain`): Contains business logic, models, and use cases.
- **Gradle Module** (`gradle`): Manages Gradle-related settings and dependencies.
- **App Module (`app`)**: The main entry point that ties everything together.

## 📦 Dependencies
This template comes pre-configured with the following dependencies:

- **Dependency Injection:** Hilt/Dagger
- **Networking:** Retrofit, OkHttp
- **Database:** Room
- **UI Components:** Jetpack Compose / XML Views
- **Navigation:** Navigation Component
- **Concurrency:** Coroutines + Flow

## 🔗 Contributing
If you want to contribute, please follow these steps:

1. Fork the repository
2. Create a new feature branch (`git checkout -b feature-branch`)
3. Commit your changes (`git commit -m 'Add new feature'`)
4. Push to the branch (`git push origin feature-branch`)
5. Open a Pull Request

## 📜 License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Contact
For any issues, suggestions, or improvements, feel free to open an issue or contact me, your best programmer `Sebas1705`.

---
**Happy Coding! 🚀**
