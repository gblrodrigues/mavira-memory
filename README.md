# Mavira
A modern memory matching game built with Kotlin and Jetpack Compose.

- [Features](#features)
- [Preview](#preview)
- [Architecture](#architecture)
- [Biggest Challenge](#biggest-challenge)
- [Technologies Used](#technologies-used)
- [Contact](#contact)

## Features
* 4×4 memory matching game
* Match and mismatch card interactions
* Move counter
* Game timer
* Restart with confirmation
* Game completion results
* Share completed game results
* Statistics and recent games history
* Persistent game history with Room
* Theme selection (System, Dark, Light)
* Language selection (English, Spanish, Portuguese)
* Configurable haptic feedback
* Persistent user preferences with DataStore

## Preview
### Screenshots
<table>
  <tr>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/97cedfa6-7d6b-40ec-b257-34f5b300b6dd" width="250" alt="Home Screen"/>
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/e54ecbb7-8116-42d5-8fd0-75096e82d010" width="250" alt="Gameplay"/>
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/75ce95ed-9fb9-4e07-8218-b788bdf4e998" width="250" alt="Statistics Screen"/>
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
      <img src="https://github.com/user-attachments/assets/31099bc8-3d7a-4f4b-8957-9dab6384bfcf" width="250" alt="Settings Screen"/>
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/31902031-a239-4c8d-94a8-a1b9193db875" width="250" alt="Game Result"/>
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/4bc34cae-dd65-4757-8dbd-27ba1fa9f4c6" width="250" alt="Share Sheet"/>
    </td>
  </tr>
  <tr>
    <td align="center"><b>Settings Screen</b></td>
    <td align="center"><b>Game Result</b></td>
    <td align="center"><b>Share Sheet</b></td>
  </tr>
</table>

### Demo
https://github.com/user-attachments/assets/15d49997-7bf3-438e-812a-7f45a309ba23

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
                             ▼
                    ┌──────────────────┐
                    │   Repositories   │
                    └────────┬─────────┘
                             │
                  ┌──────────┴──────────┐
                  ▼                     ▼
             ┌─────────┐          ┌──────────┐
             │  Room   │          │ DataStore│
             └─────────┘          └──────────┘
```

## Biggest Challenge
Adopting **Hilt** was initially challenging, as I had more experience with Koin. Learning how Hilt integrates dependency 
injection into an Android application helped me better understand dependency management and build a more 
structured architecture.

> This experience also made Hilt my preferred dependency injection framework for future Android projects.

## Technologies Used
| Category | Technology | Why |
|----------|------------|-----|
| Language | [![Kotlin](https://img.shields.io/badge/Kotlin-2.4.0-7F52FF?style=flat&logo=kotlin&logoColor=white)](https://kotlinlang.org/) | Main development language (my favorite 💜)
| UI | [![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=flat&logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose) | Declarative UI toolkit for building modern Android interfaces
| Design System | [![Material 3](https://img.shields.io/badge/Material%203-6750A4?style=flat&logo=materialdesign&logoColor=white)](https://m3.material.io/) | Used to maintain visual consistency and modern UI practices
| DI | [![Hilt](https://img.shields.io/badge/Hilt-34A853?style=flat&logo=android&logoColor=white)](https://dagger.dev/hilt/) | Dependency injection for dependency management
| Persistence | [![Room](https://img.shields.io/badge/Room-4285F4?style=flat&logo=android&logoColor=white)](https://developer.android.com/training/data-storage/room) | Local persistence for completed game results and statistics
| Preferences | [![DataStore](https://img.shields.io/badge/DataStore-3DDC84?style=flat&logo=android&logoColor=white)](https://developer.android.com/topic/libraries/architecture/datastore) | Persistent storage for user preferences such as theme, language, and vibration
| Navigation | [![Navigation Compose](https://img.shields.io/badge/Navigation%20Compose-4285F4?style=flat&logo=android&logoColor=white)](https://developer.android.com/jetpack/compose/navigation) | Type-safe navigation between screens

## Contact
<a href="https://www.linkedin.com/in/gblrodrigues">
  <img src="https://img.shields.io/badge/LinkedIn-100%2B-0077B5?style=flat&logo=linkedin&logoColor=white"/>
</a>
