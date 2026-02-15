package dev.etino.fcshared

import android.content.Context
import androidx.room.Room
import dev.etino.fcshared.database.AppDatabase
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.dsl.module

@OptIn(InternalCoroutinesApi::class)
val dbModule = module {
    factory<AppDatabase> { getRoomDatabase(get()) }
}

fun getRoomDatabase(context: Context): AppDatabase {
    val appContext = context.applicationContext
    return Room.databaseBuilder(
        appContext,
        AppDatabase::class.java,
        DATABASE_NAME
    ).build()
}


const val DATABASE_NAME = "fesbCompanionIosDatabase"