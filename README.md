# DVT Weather App - Assessment Test

This project is an Android weather application built using Kotlin Multiplatform (KMP) with a focus on the Android target. The app displays a 5-day weather forecast, fetching data from the OpenWeatherMap API and presenting it using Jetpack Compose.

## Features

*   **5-Day Forecast:** Displays a 5-day weather forecast, with each day showing the day of the week, an appropriate weather icon, and the average temperature in Celsius.
*   **Dynamic Backgrounds:** The app's background image changes dynamically based on the *current* weather condition (Sunny, Cloudy, Rainy, or a default Forest background).
*   **Location-Based:** Uses the device's current location (with permission) to determine the forecast location.  Includes a fallback to a default location (Nairobi) if location services are unavailable.
*   **Modern UI:** Built entirely with Jetpack Compose for a declarative and modern UI.
*   **Clean Architecture:** Follows the MVVM (Model-View-ViewModel) architecture for separation of concerns, testability, and maintainability.
*   **Networking:** Uses Ktor Client with kotlinx.serialization for efficient and robust network requests and JSON parsing.
*   **Error Handling:** Includes error handling for network issues, API key problems, and JSON parsing failures. User-friendly error messages are displayed.
*   **Permissions Handling:** Uses the Accompanist Permissions library to request location permissions at runtime.
*   **Loading State:** Displays a loading indicator while fetching data.
*   **Unit Tests:** Includes unit tests for the `WeatherViewModel`.
*   **Static Code Analysis:** Integrated with Detekt for static code analysis.
* **HTTPS Requests:** Uses HTTPS

## Project Structure

The project follows a standard KMP structure, with the Android-specific code residing in the `androidApp` module:

dvt-weather-app/
├── androidApp/                  # Android application module
│   ├── src/
│   │   ├── main/
│   │   │   ├── AndroidManifest.xml   # Application manifest
│   │   │   ├── kotlin/             # Kotlin source files
│   │   │   │   ├── com/dvt/dvtweatherapp/
│   │   │   │   │   ├── MainActivity.kt      # Main activity
│   │   │   │   │   ├── ui/                 # UI related code
│   │   │   │   │   │   ├── theme/          # Compose theme (colors, typography)
│   │   │   │   │   │   │   ├── Color.kt
│   │   │   │   │   │   │   ├── Type.kt
│   │   │   │   │   │   │   ├── Theme.kt
│   │   │   │   │   │   ├── WeatherScreen.kt  # Main screen composable
│   │   │   │   │   ├── viewmodel/       # ViewModel
│   │   │   │   │   │   ├── WeatherViewModel.kt
│   │   │   │   │   ├── model/            # Data models
│   │   │   │   │   │   ├── WeatherForecast.kt  # Weather data model
│   │   │   │   │   ├── network/           # Networking code
│   │   │   │   │   │          network/
    │   │   │   │   │                ├── WeatherApiService.kt # API service using Ktor
│   │   │   │   │   ├── utils/ # Utility classes
│   │   │   │       │       ├── WeatherCondition.kt
│   │   │   │       │       ├── Utils.kt
│   │   │   ├── res/                  # Resources
│   │   │   │   ├── drawable/           # Drawable resources (background images, icons)
│   │   │   │   ├── font/              # Font resources
│   │   │   │   │    ├── poppins_bold.ttf
│   │   │   │       ├── poppins_semibold.ttf
│   │   │   │   ├── values/             # Values (strings, etc.)
│   │   │   │       ├── strings.xml
│   │   ├── test/                # Unit tests
│   │   │   └── kotlin/
│   │   │       └── com/dvt/dvtweatherapp/
│   │   │           └── viewmodel/
│   │   │               └── WeatherViewModelTest.kt  # ViewModel tests
├── build.gradle.kts              # Project-level build file
├── settings.gradle.kts           # Project settings
├── config/                       # Detekt configuration
│   └── detekt/
│       └── detekt.yml
└── README.md                     # This file


## Conventions and Architecture

*   **Architecture:** The application follows the Model-View-ViewModel (MVVM) architectural pattern. This separates the UI (View - `WeatherScreen.kt`), data and business logic (ViewModel - `WeatherViewModel.kt`), and data sources (Model - `WeatherForecast.kt`, `WeatherApiService.kt`).  This promotes testability, maintainability, and a clean separation of concerns.  *(Demonstrates requirement 4a)*

*   **UI:** The user interface is built entirely using Jetpack Compose, a modern declarative UI toolkit for Android. Compose simplifies UI development and allows for a more reactive and concise way to build UIs.

*   **Networking:** Ktor Client, a Kotlin Multiplatform library, is used for making HTTP requests to the OpenWeatherMap API.  Ktor is lightweight, efficient, and well-suited for KMP projects.  `kotlinx.serialization` is used to parse the JSON responses into Kotlin data classes.

*   **Data Models:**  Data models (`WeatherForecast.kt` and related classes) were initially generated from a sample JSON response using a JSON-to-Kotlin-class tool (with kotlinx.serialization support) and then *significantly* refined and simplified to match the *actual* API response and the needs of the UI. A separate `ForecastdayUI` data class is used as a presentation model, containing only the data needed for the UI, demonstrating a separation between API data and UI data.

*   **Error Handling:**  The application handles potential errors, such as network connectivity issues, invalid API keys, and JSON parsing errors.  Error messages are displayed to the user.

*   **Dependency Management:** Dependencies are managed using Gradle. No unnecessary third-party UI libraries are used, as per the assessment requirements.  Only libraries for cross-cutting concerns (networking, serialization, permissions) are included.

*   **Coding Style:** The code follows Kotlin coding conventions and best practices, aiming for readability and maintainability.  This includes meaningful variable and function names, consistent indentation, and appropriate use of comments.

* **SOLID Principles**: SOLID principles were followed.

## Best Practices Demonstrated

The project demonstrates the following best practices, as requested in the assessment requirements:

*   **a. Architecture:**  MVVM (Model-View-ViewModel) is implemented, separating UI (`WeatherScreen.kt`), business logic (`WeatherViewModel.kt`), and data access (`WeatherApiService.kt` and model classes).

*   **b. Design Patterns:**
    *   **MVVM:** As mentioned above, the core architecture is a design pattern itself.
    *   **Repository Pattern (Simplified):** `WeatherApiService.kt` acts as a simplified repository, abstracting the details of network communication.
    *   **Strategy Pattern (Implicit):** The `WeatherIcon` composable uses a `when` statement to select the correct drawable based on the weather condition code, which is a form of the Strategy pattern.
    * **Dependency Injection:** The viewmodel uses dependency injection.

*   **c. Unit Testing (TDD Preferential):** Unit tests for `WeatherViewModel.kt` are included in `androidApp/src/test`. These tests use MockK to mock the `WeatherApiService` and verify the ViewModel's behavior in various scenarios (successful data fetching, network errors, location unavailable). While not strictly Test-Driven Development (TDD), the tests demonstrate an understanding of testing principles.

*   **d. Proper use of the SOLID principles:**
    *   **Single Responsibility Principle:** Each class and function has a clear, focused responsibility (e.g., `WeatherApiService` handles network requests, `WeatherViewModel` manages UI state and logic, `WeatherScreen` displays the UI).
    *   **Open/Closed Principle:** The `WeatherIcon` composable is open for extension (adding new weather conditions) without modifying its existing code (just add a new `case` to the `when` statement).  The `WeatherApiService` could be extended to support different API endpoints without modifying its core logic.
    *   **Liskov Substitution Principle:** Not directly applicable in this project due to the limited use of inheritance.
    *   **Interface Segregation Principle:** Not directly applicable, as there are no custom interfaces defined.  However, the use of small, focused classes and functions implicitly supports this principle.
    *   **Dependency Inversion Principle:** `WeatherViewModel` depends on an abstraction (`WeatherApiService`), rather than a concrete implementation. This allows for easy mocking during testing.  While a full dependency injection framework isn't used (due to project constraints), the principle is demonstrated.

*   **e. Integration into a CI/CD build pipeline:**  A conceptual example of a GitHub Actions workflow (`.github/workflows/android.yml`) is provided in the "CI/CD Integration" section below, outlining the steps for building, testing, and analyzing the project.

*   **f. Code coverage integration:** Although not directly implemented in the code, code coverage can easily be integrated. Tools like Jacoco can be used with the existing unit tests to generate code coverage reports. This can be added as a step in the CI/CD pipeline.

*   **g. Static code analysis:**  The project includes Detekt (`io.gitlab.arturbosch.detekt`) for static code analysis.  The configuration file (`config/detekt/detekt.yml`) defines rules for code style, complexity, and potential bugs.  Running `./gradlew detekt` will generate a report of any violations.

* **h. Additional Requirements**: The project includes things like dynamic backgrounds, permission handling, loading state, and error messages.

## Third-Party Libraries

The following *external* libraries are used, with justifications for each:

*   **Ktor Client (`io.ktor:ktor-client-android`, `io.ktor:ktor-client-content-negotiation`, `io.ktor:ktor-serialization-kotlinx-json`):**  Used for making network requests to the OpenWeatherMap API.  This is a cross-cutting concern (networking) and is permitted.
*   **Kotlinx Serialization (`org.jetbrains.kotlinx:kotlinx-serialization-json`):** Used for JSON serialization and deserialization.  This is a cross-cutting concern (data parsing) and is permitted.
*   **Accompanist Permissions (`com.google.accompanist:accompanist-permissions`):** Used for handling runtime permissions (location). This simplifies the process of requesting and handling permissions in a Compose application. This is considered a cross-cutting concern (system interaction).
*  **Coil:** For fetching images, for the weather icons in the API response.
   * `io.coil-kt:coil-compose`
*   **Google Fonts** For fetching Google fonts from the API.
    * `androidx.compose.ui:ui-text-google-fonts`
*   **MockK (`io.mockk:mockk`):**  Used for mocking dependencies in unit tests (specifically, mocking `WeatherApiService`).  This is a testing library and is only included in the `testImplementation` configuration.
*  **JUnit**: For Unit testing
* `junit:junit`
*  **Kotlin Coroutine Test**: Testing coroutines
   *`org.jetbrains.kotlinx:kotlinx-coroutines-test`

The following are *not* considered third-party libraries in the context of this assessment, as they are part of the AndroidX and Compose libraries provided by Google:

*   **Jetpack Compose Libraries:** All the `androidx.compose...` libraries are part of Jetpack Compose, Google's recommended UI toolkit.
*   **AndroidX Lifecycle Libraries:**  `androidx.lifecycle...` libraries are part of AndroidX and are used for managing lifecycle-aware components (ViewModel).
*  **Android Architecture Components**
    * `androidx.arch.core:core-testing`
*   **Core KTX:** `androidx.core:core-ktx` provides Kotlin extensions for common Android APIs.
* **Google play services location**: To get device location.

## Build Instructions

1.  **Prerequisites:**
    *   Android Studio (latest stable version recommended).
    *   Kotlin Multiplatform Mobile plugin installed in Android Studio.
    *   An OpenWeatherMap API key.

2.  **Clone the Repository:**

    ```bash
    git clone https://github.com/Billoxinogen18/DVTTestProject.git
    ```


3.  **Open the Project:** Open the project in Android Studio.

4.  **Add API Key:**
    *   Open the `androidApp/build.gradle.kts` file.
    *   Replace `"3b054cd8e7340d91fc2300d142e75ab7"` within the `buildConfigField` for both the `debug` and `release` buildTypes with your *actual* OpenWeatherMap API key.  Make sure to keep the escaped double quotes around the key:
        ```kotlin
        buildConfigField("String", "OPENWEATHERMAP_API_KEY", "\"3b054cd8e7340d91fc2300d142e75ab7\"")
        ```

5.  **Clean and Rebuild:**
    *   Go to **Build \> Clean Project**.
    *   Go to **Build \> Rebuild Project**. This is crucial after modifying `build.gradle.kts`.

6.  **Run the Application:**
    *   Select the `androidApp` configuration in the run/debug configurations dropdown.
    *   Click the "Run" button (green play icon).

## CI/CD Integration

This project can be integrated into a CI/CD pipeline using services like GitHub Actions, Bitbucket Pipelines, or GitLab CI. Here's a general outline of the steps for GitHub Actions:

1.  **Create a Workflow File:** Create a file named `.github/workflows/android.yml` (or similar) in your repository.

2.  **Configure the Workflow:** The workflow file will define the steps to be executed on each push or pull request.  A typical workflow would include:

    *   **Checkout Code:** Checkout the repository.
    *   **Set up JDK:** Set up the correct Java Development Kit (JDK) version.
    *   **Run Detekt (Static Analysis):** Run `./gradlew detekt` to perform static code analysis.
    *   **Run Tests:** Run `./gradlew test` to execute unit tests.
    *   **Build APK (Optional):**  Run `./gradlew assembleDebug` or `./gradlew assembleRelease` to build the Android application package.
    *   **Upload Artifacts (Optional):** Upload the APK or test reports as artifacts.

3.  **Example GitHub Actions Workflow (`.github/workflows/android.yml` - Conceptual):**

    ```yaml
    name: Android CI

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

        - name: Build with Gradle
          run: ./gradlew build
    ```

4.  **Push to GitHub:** The workflow is triggered on push, now the CI will run.

## Additional Notes

*   **Location Services:** The app requires location permissions to function correctly.  If location services are disabled or permissions are denied, a fallback location (Nairobi) is used.
*   **API Key:** The OpenWeatherMap API key is stored in the `build.gradle.kts` file using `buildConfigField`.  This prevents the key from being hardcoded in the source code, improving security.
*   **Error Handling:** The app includes basic error handling for network and API issues.  More robust error handling (e.g., retry mechanisms, more specific error messages) could be implemented.
*   **OpenWeatherMap API:** The app specifically uses the 5-day / 3-hour forecast API from OpenWeatherMap (`https://api.openweathermap.org/data/2.5/forecast`). It is *not* compatible with other OpenWeatherMap APIs (like the Current Weather API) without modification.

