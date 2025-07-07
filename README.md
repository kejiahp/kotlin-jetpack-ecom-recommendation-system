# MOBILE APPLICATION INTERACTING WITH A RECOMMENDATION SYSTEM FOR E-COMMERCE APPS
*This was a Bsc dissertation project.*

#### Introduction

The frontend of the system is developed using Jetpack Compose, a modern declarative UI
toolkit for building native Android interfaces. The architecture follows the Model-View-
ViewModel (MVVM) design pattern, which is widely adopted in Android development for
promoting a clear separation of concerns and enabling scalable, testable, and
maintainable codebases.
The MVVM architecture organizes the application into three main components:
- **Model**: Represents the data and business logic layer. In this system, the Model
includes data classes, repositories, and API interfaces that handle data retrieval
from the backend using Retrofit and OkHttp.
- **View**: Refers to the UI components built with Jetpack Compose. The View is
responsible for displaying data to the user and capturing user input, but it contains
minimal logic. It observes state changes exposed by the ViewModel and updates,
accordingly, ensuring a reactive and declarative UI flow.
- **ViewModel**: Acts as the intermediary between the View and the Model. It holds and
manages UI-related data, handles user interactions, and communicates with
repositories to fetch or update data. The ViewModel exposes observable state using
StateFlow or LiveData, allowing the View to react automatically to data changes.

#### Dependencies

- Kotlin `2.0.0`
- Jetpack Compose: details in the `gradle/libs.versions.toml` file.
- Android Studio `Ladybug | 2024.2.1	3.2-8.7`
- Dagger Hilt `com.google.dagger:hilt-android:2.51.1`
- Retrofit and OkHttp3 `com.squareup.retrofit2:retrofit:2.11.0` and `com.squareup.okhttp3:okhttp:4.12.0`
- Coil `io.coil-kt:coil-compose:2.4.0`
- Kotlin Serialization `org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3`
- Navigation Compose `androidx.navigation:navigation-compose:2.8.9`
- *And others listed in the `app/build.gradle.kts` file*

#### Model-View-ViewModel Architecture Pattern
![mvvm drawio (1)](https://github.com/user-attachments/assets/c76f4806-50a9-4310-b7f0-2dca9b165bed)


#### Product Listing Page
<img width="247" alt="product_list_screen" src="https://github.com/user-attachments/assets/c37da388-2b12-4a61-a6c7-7660272dd079" />

#### Running Locally?
- Use Android studio, preferrably (`Ladybug | 2024.2.1	3.2-8.7`)
