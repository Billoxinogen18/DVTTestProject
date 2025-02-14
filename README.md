# DVT Weather App 📱

A modern Android weather application built using Kotlin Multiplatform (KMP) that provides a 5-day weather forecast with dynamic backgrounds and location-based updates.

<img src="https://github.com/Billoxinogen18/DVTTestProject/blob/main/sample/sample.png" alt="DVT Weather App Screenshot" width="300"/>

## ✨ Features

*   **5-Day Forecast:** View a 5-day weather forecast with daily weather icons and average temperatures in Celsius
*   **Dynamic Backgrounds:** App background adapts to current weather conditions (Sunny, Cloudy, Rainy, or Forest)
*   **Location-Based:** Uses device location with fallback to Nairobi if unavailable
*   **Modern UI:** Built with Jetpack Compose for a sleek, responsive interface
*   **Clean Architecture:** Implements MVVM pattern for maintainable, testable code
*   **Robust Networking:** Powered by Ktor Client with kotlinx.serialization
*   **Comprehensive Error Handling:** User-friendly messages for network issues and API errors
*   **Runtime Permissions:** Smooth location permission handling with Accompanist
*   **Loading States:** Clear loading indicators during data fetching
*   **Unit Testing:** Comprehensive tests for WeatherViewModel
*   **Code Quality:** Integrated with Detekt for static analysis
*   **Secure:** HTTPS for all network requests

## 🏗️ Project Structure

```
dvt-weather-app/
├── androidApp/                      # Android application module
│   ├── src/
│   │   ├── main/
│   │   │   ├── AndroidManifest.xml # Application manifest
│   │   │   ├── kotlin/             # Kotlin source files
│   │   │   │   ├── com/dvt/dvtweatherapp/
│   │   │   │   │   ├── MainActivity.kt    # Main activity
│   │   │   │   │   ├── ui/               # UI related code
│   │   │   │   │   │   ├── theme/        # Compose theme
│   │   │   │   │   │   │   ├── Color.kt
│   │   │   │   │   │   │   ├── Type.kt
│   │   │   │   │   │   │   ├── Theme.kt
│   │   │   │   │   │   ├── WeatherScreen.kt
│   │   │   │   │   ├── viewmodel/        # ViewModel
│   │   │   │   │   │   ├── WeatherViewModel.kt
│   │   │   │   │   ├── model/           # Data models
│   │   │   │   │   │   ├── WeatherForecast.kt
│   │   │   │   │   ├── network/         # Networking
│   │   │   │   │   │   ├── WeatherApiService.kt
│   │   │   │   │   ├── utils/          # Utilities
│   │   │   │   │   │   ├── WeatherCondition.kt
│   │   │   │   │   │   ├── Utils.kt
│   │   │   ├── res/                    # Resources
│   │   │   │   ├── drawable/           # Images
│   │   │   │   ├── font/              # Fonts
│   │   │   │   ├── values/           # Strings etc.
│   │   ├── test/                     # Unit tests
├── build.gradle.kts                  # Project build file
├── settings.gradle.kts               # Project settings
├── config/                          # Detekt config
└── README.md
```

## 🏛️ Architecture & Best Practices

### Architecture (MVVM)
The app follows Model-View-ViewModel architecture:
- **View** (`WeatherScreen.kt`): Handles UI using Jetpack Compose
- **ViewModel** (`WeatherViewModel.kt`): Manages UI state and business logic
- **Model** (`WeatherForecast.kt`, `WeatherApiService.kt`): Handles data and API interactions

### Design Patterns
- **MVVM**: Core architectural pattern
- **Repository Pattern**: Abstracted network communication
- **Strategy Pattern**: Weather icon selection logic
- **Dependency Injection**: Used in ViewModel construction

### SOLID Principles
- **Single Responsibility**: Each class has one focused purpose
- **Open/Closed**: Extensible weather condition handling
- **Liskov Substitution**: Proper inheritance usage
- **Interface Segregation**: Focused interfaces
- **Dependency Inversion**: Abstract dependencies for testing

## 📚 Libraries

### Core Libraries
- **Ktor Client**: Network requests
- **Kotlinx Serialization**: JSON parsing
- **Accompanist Permissions**: Runtime permission handling
- **Coil**: Image loading
- **Google Fonts**: Typography

### Testing Libraries
- **MockK**: Mocking for tests
- **JUnit**: Unit testing
- **Kotlin Coroutine Test**: Coroutine testing

## 🚀 Getting Started

### Prerequisites
- Android Studio (latest stable version)
- Kotlin Multiplatform Mobile plugin
- OpenWeatherMap API key

### Installation

1. Clone the repository:
```bash
git clone https://github.com/Billoxinogen18/DVTTestProject.git
```

2. Open in Android Studio

3. Add your API key in `androidApp/build.gradle.kts`:
```kotlin
buildConfigField("String", "OPENWEATHERMAP_API_KEY", "\"your-api-key-here\"")
```

4. Build and run:
- Clean project (Build > Clean Project)
- Rebuild project (Build > Rebuild Project)
- Run the app

## 🔄 CI/CD Integration

The project includes a GitHub Actions workflow for automated building and testing:

```yaml
name: DVT Weather App CI

on:
  push:
    branches: [ "main" ]
  pull_request:
    branches: [ "main" ]

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
    - uses: actions/checkout@v3  
    - name: set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'
        cache: gradle

    - name: Grant execute permission for gradlew
      run: chmod +x gradlew

    - name: Run Detekt
      run: ./gradlew detekt

    - name: Run Tests
      run: ./gradlew test

    - name: Build Debug APK
      run: ./gradlew assembleDebug

    - name: Create Release
      if: github.event_name == 'push' && github.ref == 'refs/heads/main'
      uses: softprops/action-gh-release@v1 
      with:
        tag_name: release-${{ github.run_number }}
        name: Release ${{ github.run_number }}
        draft: false
        prerelease: false
        files: |
          androidApp/build/outputs/apk/debug/androidApp-debug.apk

      env:
        GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}

    - name: Upload Test Reports
      uses: actions/upload-artifact@v4 
      if: always()
      with:
        name: test-reports
        path: androidApp/build/reports/tests/testDebugUnitTest/ 

```

## 📝 Additional Notes

- Location permissions required for full functionality
- API key stored securely in build config
- Comprehensive error handling implemented
- Uses OpenWeatherMap 5-day forecast API
