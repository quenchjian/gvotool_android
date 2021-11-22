package me.quenchjian.gvotool.dependencyInjection

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.quenchjian.gvotool.BuildConfig
import me.quenchjian.gvotool.data.GvoDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

  @Provides
  @Singleton
  fun provideDatabase(@ApplicationContext context: Context): GvoDatabase {
    val name = if (BuildConfig.DEBUG) "gvo_tool_debug.db" else "gvo_tool.db"
    return Room.databaseBuilder(context.applicationContext, GvoDatabase::class.java, name).build()
  }

  @Provides
  @Singleton
  fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
    val name = if (BuildConfig.DEBUG) "gvo.tool.pref.debug" else "gvo.tool.pref"
    return context.getSharedPreferences(name, Context.MODE_PRIVATE)
  }
}