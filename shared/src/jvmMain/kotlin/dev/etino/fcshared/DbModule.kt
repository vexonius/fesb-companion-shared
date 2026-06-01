package dev.etino.fcshared

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import dev.etino.fcshared.featuresKotlin.database.AppDatabase
import kotlinx.coroutines.InternalCoroutinesApi
import okio.Path.Companion.toPath
import org.koin.dsl.module
import java.io.File

@OptIn(InternalCoroutinesApi::class)
val dbModule = module {
    factory<AppDatabase> { getRoomDatabase() }
    single<DataStore<Preferences>> {
        createDataStore()
    }
}

fun getRoomDatabase(): AppDatabase {
    return getDatabaseBuilder().build()
}

fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), "my_room.db")
    return Room.databaseBuilder<AppDatabase>(
        name = dbFile.absolutePath,
    ).setDriver(BundledSQLiteDriver())
}

const val DATABASE_NAME = "fesbCompanionIosDatabase"


/**
 *   Gets the singleton DataStore instance, creating it if necessary.
 */
fun createDataStore(): DataStore<Preferences> = createDataStore(
    producePath = {
        val file = File(System.getProperty("java.io.tmpdir"), dataStoreFileName)
        file.absolutePath
    }
)

fun createDataStore(producePath: () -> String): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        produceFile = { producePath().toPath() }
    )

internal const val dataStoreFileName = "FesbCompanion.preferences_pb"