package dev.etino.fcshared

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import dev.etino.fcshared.database.AppDatabase
import kotlinx.coroutines.InternalCoroutinesApi
import okio.Path.Companion.toPath
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

@OptIn(InternalCoroutinesApi::class)
val dbModule = module {
    factory<AppDatabase> { getRoomDatabase(get()) }
    single<DataStore<Preferences>> {
        createDataStore {
            androidContext().filesDir.resolve(dataStoreFileName).absolutePath
        }
    }
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

/**
 *   Gets the singleton DataStore instance, creating it if necessary.
 */
fun createDataStore(producePath: () -> String): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        produceFile = { producePath().toPath() }
    )

internal const val dataStoreFileName = "FesbCompanion.preferences_pb"