# KompanionCareTest

This is a sample Android application that demonstrates a modern, modular, and scalable architecture. The app fetches and displays weather data for a given location and also displays a story view with images related to the weather.

## Architecture

The app is built using a modular, multi-layered architecture that promotes separation of concerns, scalability, and maintainability. It follows the principles of **Clean Architecture**.

The architecture is composed of the following layers:

*   **Data Layer:** The `data` layer is responsible for fetching data from the network and providing it to the `domain` layer. It uses Retrofit for networking and Moshi for JSON parsing.
*   **Domain Layer:** The `domain` layer contains the business logic of the app. It defines use cases that are used by the `feature` layers to interact with the data layer.
*   **Feature Layers:** The `feature` layers are responsible for the UI and user interaction. Each feature is a self-contained module that has its own UI, ViewModel, and dependency injection setup.
*   **UI Layer:** The `ui` layer contains common UI components that are shared across the feature layers.

## Weather Data

The weather data is fetched from the OpenWeather API. The app uses the `FusedLocationProviderClient` to get the user's current location and then uses that location to fetch the weather data.

The weather data is displayed on the `WeatherScreen`, which is a composable function that displays the current weather conditions, as well as a forecast for the next few days.

## Story View

The story view is implemented using a `NavHost` and a `NavGraphBuilder`. The story view displays a series of images related to the weather. The images are fetched from the Unsplash API.

The story view is implemented as a separate feature module, which makes it easy to reuse in other apps.

## Getting Started

To get started with the project, you will need to do the following:

1.  Clone the repository.
2.  Open the project in Android Studio.
3.  Add your OpenWeather API key to the `local.properties` file.
4.  Build and run the app.
