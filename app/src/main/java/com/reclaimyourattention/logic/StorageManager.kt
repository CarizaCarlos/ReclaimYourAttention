package com.reclaimyourattention.logic

import android.content.Context
import com.reclaimyourattention.ReclaimYourAttention.Companion.appContext
import kotlinx.serialization.json.Json

object StorageManager {
    // Atributos
    private const val PREFS_NAME = "ReclaimYourAttentionPrefs"
    private val prefs by lazy {
        appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // Métodos
    // Tipos Primitivos
    fun saveString(key: String, value: String) {
        prefs.edit().apply { putString(key, value); apply() }
    }

    fun getString(key: String, default: String = ""): String {
        return prefs.getString(key, default) ?: default
    }

    fun saveInt(key: String, value: Int) {
        prefs.edit().apply { putInt(key, value); apply() }
    }

    fun getInt(key: String, default: Int = 0): Int {
        return prefs.getInt(key, default)
    }

    fun saveStringSet(key: String, set: Set<String>) {
        prefs.edit().apply { putStringSet(key, set); apply() }
    }

    fun saveBoolean(key: String, value: Boolean) {
        prefs.edit().apply { putBoolean(key, value); apply() }
    }

    fun getBoolean(key: String, default: Boolean = false): Boolean {
        return prefs.getBoolean(key, default)
    }

    // Sets de Tipos Primitivos
    fun getStringSet(key: String, default: Set<String>): Set<String> {
        return prefs.getStringSet(key, default) ?: default
    }

    inline fun <reified T> saveObject(key: String, obj: T) {
        val json = Json.encodeToString(obj)
        saveString(key, json)
    }

    inline fun <reified T> loadObject(key: String): T? {
        val json = getString(key)
        return try { Json.decodeFromString<T>(json) } catch (e: Exception) { null }
    }

    // Maps
    inline fun <reified T> saveMap(key: String, map: T) {
        val jsonString = Json.encodeToString(map)
        saveString(key, jsonString)
    }

    inline fun <reified T> getMap(key: String, default: T): T {
        val jsonString = getString(key, Json.encodeToString(default))
        return try {
            Json.decodeFromString<T>(jsonString)
        } catch (e: Exception) {
            default
        }
    }
}