package com.example.tanuhaiassessment.core.storage.version

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class VersionStorageImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : VersionStorage {

    private fun versionKey(
        modelId: String
    ) = stringPreferencesKey(
        "${modelId}_version"
    )

    override suspend fun saveVersion(modelId: String, version: String) {
        dataStore.edit {

            it[versionKey(modelId)] =
                version
        }
    }

    override suspend fun getVersion(modelId: String): String {
        return "${dataStore.data.first()[versionKey(modelId)]}"
    }


}