package me.quenchjian.gvotool.data

import android.content.SharedPreferences
import me.quenchjian.gvotool.data.entity.Character
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Settings @Inject constructor(private val pref: SharedPreferences) {

  var skipped: Boolean
    get() = pref.getBoolean("pref-skipped", false)
    set(value) = pref.edit().putBoolean("pref-skipped", value).apply()

  var characterInitialized: Boolean
    get() = pref.getBoolean("pref-character-initialized", false)
    set(value) = pref.edit().putBoolean("pref-character-initialized", value).apply()

  var discoveryImported: Boolean
    get() = pref.getBoolean("pref-discovery-imported", false)
    set(value) = pref.edit().putBoolean("pref-discovery-imported", value).apply()

  var selectedCharacter: Character?
    get() = pref.getString("pref-selected-character", "")!!.toCharacter()
    set(value) = pref.edit().putString("pref-selected-character", value.toJson()).apply()

  private fun Character?.toJson(): String {
    this ?: return ""
    return JSONObject()
      .put("id", id)
      .put("account", account)
      .put("server", server)
      .put("name", name)
      .toString()
  }

  private fun String.toCharacter(): Character? {
    val json = if (isNotEmpty()) this else return null
    return JSONObject(json).run {
      Character(getInt("id"), getString("account"), getString("server"), getString("name"))
    }
  }
}