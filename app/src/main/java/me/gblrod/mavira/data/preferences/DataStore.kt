package me.gblrod.mavira.data.preferences

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.maviraDataStore by preferencesDataStore(name = "mavira_preferences")