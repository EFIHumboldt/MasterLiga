package com.efihumboldt.appligas.Varios

import android.content.Context
import android.content.SharedPreferences
import com.efihumboldt.appligas.entidades.EquipoSimple
import com.google.gson.Gson

class SharedPreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("favorites_teams", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun addFavorite(equipo: EquipoSimple) {
        val favorites = getFavorites().toMutableSet()
        favorites.add(gson.toJson(equipo))
        saveFavorites(favorites)
    }

    fun removeFavorite(equipo: EquipoSimple) {
        val favorites = getFavorites().toMutableSet()
        favorites.remove(gson.toJson(equipo))
        saveFavorites(favorites)
    }

    fun isFavorite(equipo: EquipoSimple): Boolean {
        val equipoJson = gson.toJson(equipo)
        return equipoJson in getFavorites()
    }

    fun getFavorites(): Set<String> {
        val favoritesJson = sharedPreferences.getStringSet("favorites_teams", HashSet()) ?: HashSet()
        return favoritesJson
    }

    private fun saveFavorites(favorites: Set<String>) {
        val editor = sharedPreferences.edit()
        editor.putStringSet("favorites_teams", favorites)
        editor.apply()
    }

    fun getFavoritesCount(): Int {
        return getFavorites().size
    }
}
