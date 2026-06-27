package com.example.tanuhaiassessment.core.storage.version

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore by preferencesDataStore(
    name = "edge_ai_preferences"
)