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
* Statistics and recent games history
* Persistent game history with Room

## Preview
https://github.com/user-attachments/assets/d6437a66-f13c-4b84-bdf2-f05e9d7f1ee7

## Architecture
```text
┌─────────────────────┐        ┌────────────────────────┐
│     GameScreen      │        │   StatisticsScreen     │
└──────────┬──────────┘        └────────────┬───────────┘
           │                                │
           ▼                                ▼
┌─────────────────────┐        ┌────────────────────────┐
│   GameViewModel     │        │ StatisticsViewModel    │
└──────────┬──────────┘        └────────────┬───────────┘
           │                                │
           └──────────────┬─────────────────┘
                          ▼
                ┌─────────────────────┐
                │   GameRepository    │
                └──────────┬──────────┘
                           │
                           ▼
                ┌─────────────────────┐
                │        Room         │
                └─────────────────────┘
```

## Biggest Challenge
Adopting **Hilt** was initially challenging, as I had more experience with Koin. Learning how Hilt integrates dependency injection 
into an Android application helped me better understand dependency management and build a more structured architecture.

> This experience also made Hilt my preferred dependency injection framework for future Android projects.

## Technologies Used
| Category | Technology | Why |
|----------|------------|-----|
| Language | [![Kotlin](https://img.shields.io/badge/Kotlin-2.4.0-7F52FF?style=flat&logo=kotlin&logoColor=white)](https://kotlinlang.org/) | Main development language (my favorite 💜)
| UI | [![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=flat&logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose) | Declarative UI toolkit for building modern Android interfaces
| Design System | [![Material 3](https://img.shields.io/badge/Material%203-6750A4?style=flat&logo=materialdesign&logoColor=white)](https://m3.material.io/) | Used to maintain visual consistency and modern UI practices
| DI | [![Hilt](https://img.shields.io/badge/Hilt-34A853?style=flat&logo=android&logoColor=white)](https://dagger.dev/hilt/) | Dependency injection for dependency management
| Persistence | [![Room](https://img.shields.io/badge/Room-4285F4?style=flat&logo=android&logoColor=white)](https://developer.android.com/training/data-storage/room) | Local persistence for completed game results and statistics
| Navigation | [![Navigation Compose](https://img.shields.io/badge/Navigation%20Compose-4285F4?style=flat&logo=android&logoColor=white)](https://developer.android.com/jetpack/compose/navigation) | Type-safe navigation between screens

## Contact
<a href="https://www.linkedin.com/in/gblrodrigues">
  <img src="https://img.shields.io/badge/LinkedIn-100%2B-0077B5?style=flat&logo=linkedin&logoColor=white"/>
</a>
