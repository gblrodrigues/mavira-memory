package me.gblrod.mavira.data.datastore

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.maviraDataStore by preferencesDataStore(name = "mavira_preferences")