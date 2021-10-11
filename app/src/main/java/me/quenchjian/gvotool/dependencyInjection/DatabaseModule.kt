package me.quenchjian.gvotool.dependencyInjection

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.quenchjian.gvotool.BuildConfig
import me.quenchjian.gvotool.database.GvoDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

  @Provides
  @Singleton
  fun provideDatabase(@ApplicationContext context: Context): GvoDatabase {
    val name = if (BuildConfig.DEBUG) "gvo-debug.db" else "gvo.db"
    return Room.databaseBuilder(context.applicationContext, GvoDatabase::class.java, name).build()
  }
}