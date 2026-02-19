package dev.etino.fcshared.navigation

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Serializable
data object Iksica : NavKey

@Serializable
data object Studomat : NavKey

@Serializable
data object Home : NavKey

@Serializable
data object Attendance : NavKey

@Serializable
data object TimeTable : NavKey

@Serializable
data object Login : NavKey

@Serializable
data object Settings : NavKey
val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(Iksica::class, Iksica.serializer())
            subclass(Home::class, Home.serializer())
            subclass(Login::class, Login.serializer())
            subclass(TimeTable::class, TimeTable.serializer())
            subclass(Attendance::class, Attendance.serializer())
            subclass(Studomat::class, Studomat.serializer())
            subclass(Settings::class, Settings.serializer())
        }
    }
}