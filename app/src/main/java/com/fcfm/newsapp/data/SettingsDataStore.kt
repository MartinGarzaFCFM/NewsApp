package com.fcfm.newsapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val LOGGED_USER = "logged_user"

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = LOGGED_USER
)

class SettingsDataStore(context: Context) {
    private val IS_USER_LOGGED_IN = booleanPreferencesKey("is_user_logged_in")
    private val USER_ID = stringPreferencesKey("user_id")
    private val USER_NAMES = stringPreferencesKey("user_names")
    private val USER_LASTNAMES = stringPreferencesKey("user_lastnames")
    private val USER_EMAIL = stringPreferencesKey("user_email")
    private val USER_USERNAME = stringPreferencesKey("user_username")
    private val USER_ROLE = stringPreferencesKey("user_role")

    val preferenceFlow: Flow<Boolean> = context.dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[IS_USER_LOGGED_IN] ?: false
        }


    suspend fun saveUserToPreferencesStore(
        isUserLoggedIn: Boolean,
        id: String,
        names: String,
        lastNames: String,
        email: String,
        username: String,
        role: String,
        context: Context
    ){
        context.dataStore.edit { preferences ->
            preferences[IS_USER_LOGGED_IN] = isUserLoggedIn
            preferences[USER_ID] = id
            preferences[USER_NAMES] = names
            preferences[USER_LASTNAMES] = lastNames
            preferences[USER_EMAIL] = email
            preferences[USER_USERNAME] = username
            preferences[USER_ROLE] = role
        }
    }

    suspend fun clearUserToPreferencesStore(context: Context){
        context.dataStore.edit {
            it.remove(IS_USER_LOGGED_IN)
            it.remove(USER_ID)
            it.remove(USER_NAMES)
            it.remove(USER_LASTNAMES)
            it.remove(USER_EMAIL)
            it.remove(USER_USERNAME)
            it.remove(USER_ROLE)
        }
    }
}