package me.gblrod.mavira.navigation.routes

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    @Serializable
    data object Home : Route

    @Serializable
    data object Game : Route

    @Serializable
    data object Stats : Route

    @Serializable
    data object Config : Route
}