# Mavira
Mavira is an Android memory matching game built with Kotlin and Jetpack Compose, featuring multiple difficulty levels, a hint system, a timed challenge, configurable background music, persistent statistics and preferences, responsive layouts, configurable haptic feedback, and native game-result sharing.

- [Preview](#preview)
- [Features](#features)
- [Architecture](#architecture)
- [Biggest Challenge](#biggest-challenge)
- [Technologies Used](#technologies-used)
- [Tests](#tests)
- [Contact](#contact)

## Preview
### Full App Demo
A walkthrough of Mavira, showcasing gameplay, game modes, background music controls, settings, statistics, and other features.
> Note: This recording captures the UI only; device audio is not included.

https://github.com/user-attachments/assets/6619e597-ca41-48c0-a772-a0dbaef7893f

### Screenshots
<table>
  <tr>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/e49e3048-5ed4-4a8c-9a0a-1ad337eaf013" width="250" alt="Home Screen"/>
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/afa55c74-647c-4c15-9263-4504f9275b44" width="250" alt="Gameplay"/>
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/c1d2f2b4-18b3-4a03-8a93-e69c91b14444" width="250" alt="Statistics Screen"/>
    </td>
  </tr>
  <tr>
    <td align="center"><b>Home Screen</b></td>
    <td align="center"><b>Gameplay</b></td>
    <td align="center"><b>Statistics Screen</b></td>
  </tr>
</table>

<table>
  <tr>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/d23a4082-4f1e-48a6-af6e-eef1e527f54a" width="250" alt="Settings Screen"/>
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/60783e04-0a66-4b15-8999-96e8dd8d0978" width="250" alt="Game Result"/>
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/364879f6-78ae-4672-8617-fdb72a1a6f8e" width="250" alt="Share Sheet"/>
    </td>
  </tr>
  <tr>
    <td align="center"><b>Settings Screen</b></td>
    <td align="center"><b>Game Result</b></td>
    <td align="center"><b>Share Sheet</b></td>
  </tr>
</table>

## Features
* Memory matching game with multiple difficulty levels
* Match and mismatch card interactions
* Move counter
* Game timer
* Restart with confirmation
* Game completion results
* Timed Challenge with a 2 minutes countdown and one attempt per day
* Configurable background music with playback controls in Game and Timed Challenge
* Hint system with limited uses per game
* Share completed game results
* Statistics and recent games history
* Persistent game results with Room
* Theme selection (System, Dark, Light)
* Language selection (English, Spanish, Portuguese)
* Configurable haptic feedback
* Persistent user preferences (theme, language, vibration, and background music) with DataStore

## Architecture
```text
                    ┌──────────────────┐
                    │        UI        │
                    │ Compose Screens  │
                    └────────┬─────────┘
                             │
                             ▼
                    ┌──────────────────┐
                    │    ViewModels    │
                    │ MVVM + StateFlow │
                    └────────┬─────────┘
                             │
                  ┌──────────┴────────────┐
                  ▼                       ▼
          ┌────────────────┐     ┌──────────────────┐
          │   GameEngine   │     │   Repositories   │
          │   Game Rules   │     └────────┬─────────┘
          └────────────────┘              │
                                   ┌──────┴───────┐
                                   ▼              ▼
                              ┌──────────┐   ┌───────────┐
                              │   Room   │   │ DataStore │
                              └──────────┘   └───────────┘
```

## Tests
Mavira includes unit tests covering game logic and ViewModel behavior, including:

* Game deck generation and difficulty levels
* Card selection and pair matching
* Game completion and restart
* Timer behavior
* Hint behavior
* Timed Challenge initialization, countdown, and daily attempt registration
* Challenge timeout, completion, and post game interaction handling
* ViewModel state and game effects

## Biggest Challenge
### Designing a Responsive Game Board
One of the biggest challenges was designing a game board that supports multiple difficulty levels with different card counts while remaining fully usable in portrait and 
landscape orientations. The solution dynamically adjusts the number of columns and calculates the largest card size that fits the available space without clipping or requiring scrolling.

## Technologies Used
| Category      | Technology                                                                                                                                                                                                 | Why                                                                            |
|---------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------|
| Language      | [![Kotlin](https://img.shields.io/badge/Kotlin-2.4.21-7F52FF?style=flat&logo=kotlin&logoColor=white)](https://kotlinlang.org/)                                                                              | Main development language (my favorite 💜)                                     |
| UI            | [![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=flat&logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)                                  | Declarative UI toolkit for building modern Android interfaces                  |
| Design System | [![Material 3](https://img.shields.io/badge/Material%203-6750A4?style=flat&logo=materialdesign&logoColor=white)](https://m3.material.io/)                                                                  | Used to maintain visual consistency and modern UI practices                    |
| Audio | [![AndroidX Media3](https://img.shields.io/badge/AndroidX%20Media3-3DDC84?style=flat&logo=android&logoColor=white)](https://developer.android.com/media/media3) [![ExoPlayer](https://img.shields.io/badge/ExoPlayer-4285F4?style=flat&logo=android&logoColor=white)](https://developer.android.com/media/media3/exoplayer) | Background music playback, looping, and audio focus management |
| DI            | [![Hilt](https://img.shields.io/badge/Hilt-34A853?style=flat&logo=android&logoColor=white)](https://dagger.dev/hilt/)                                                                                      | Dependency injection for dependency management                                 |
| Persistence   | [![Room](https://img.shields.io/badge/Room-4285F4?style=flat&logo=android&logoColor=white)](https://developer.android.com/training/data-storage/room)                                                      | Local persistence for completed game results and statistics                    |
| Preferences   | [![DataStore](https://img.shields.io/badge/DataStore-3DDC84?style=flat&logo=android&logoColor=white)](https://developer.android.com/topic/libraries/architecture/datastore)                                | Persistent storage for user preferences and Timed Challenge availability |
| Testing       | [![JUnit 4](https://img.shields.io/badge/JUnit%204-25A162?style=flat&logo=junit4&logoColor=white)](https://junit.org/) [![MockK](https://img.shields.io/badge/MockK-FF9800?style=flat)](https://mockk.io/) | Unit testing for game logic and ViewModel behavior                             |
| Navigation    | [![Navigation Compose](https://img.shields.io/badge/Navigation%20Compose-4285F4?style=flat&logo=android&logoColor=white)](https://developer.android.com/jetpack/compose/navigation)                        | Type-safe navigation between screens                                           |

## Contact
<a href="https://www.linkedin.com/in/gblrodrigues">
  <img src="https://img.shields.io/badge/LinkedIn-100%2B-0077B5?style=flat&logo=linkedin&logoColor=white" alt="My LinkedIn Profile"/>
</a>
