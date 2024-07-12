package com.dikamahard.petfeeder.helper

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "states")

class StatePreferences private constructor(private val datastore: DataStore<Preferences>){

    val ONBOARDING_STATE_KEY = booleanPreferencesKey("onboarding_state_key")
    val ROLE_STATE_KEY = booleanPreferencesKey("role_state_key")
    val ROLE_CHOSEN_KEY = stringPreferencesKey("role_chosen_key")

    suspend fun getState(key: String): Boolean {
        val dataStoreKey = booleanPreferencesKey(key)
        val states = datastore.data.first()
        return states[dataStoreKey] ?: false
    }

    suspend fun saveState(key: String, state: Boolean) {
        val dataStoreKey = booleanPreferencesKey(key)
        datastore.edit { states ->
            states[dataStoreKey] = state
        }
    }

    suspend fun getRole(key: String): String? {
        val dataStoreKey = stringPreferencesKey(key)
        val values = datastore.data.first()
        return values[dataStoreKey]
    }

    suspend fun saveRole(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        datastore.edit { values ->
            values[dataStoreKey] = value
        }
    }



    companion object {
        @Volatile
        private var INSTANCE: StatePreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): StatePreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = StatePreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

}