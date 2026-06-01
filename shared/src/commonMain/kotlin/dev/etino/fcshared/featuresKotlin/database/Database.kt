package dev.etino.fcshared.featuresKotlin.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import dev.etino.fcshared.featuresKotlin.attendance.dao.AttendanceDao
import dev.etino.fcshared.featuresKotlin.attendance.models.AttendanceEntry
import dev.etino.fcshared.featuresKotlin.home.dao.NoteDao
import dev.etino.fcshared.featuresKotlin.home.models.NoteRoom
import dev.etino.fcshared.featuresKotlin.iksica.dao.IksicaDao
import dev.etino.fcshared.featuresKotlin.iksica.models.ReceiptRoom
import dev.etino.fcshared.featuresKotlin.iksica.models.StudentDataRoom
import dev.etino.fcshared.featuresKotlin.login.dao.UserDao
import dev.etino.fcshared.featuresKotlin.login.user.models.UserRoom
import dev.etino.fcshared.featuresKotlin.studomat.dao.StudomatDao
import dev.etino.fcshared.featuresKotlin.studomat.models.StudomatSubject
import dev.etino.fcshared.featuresKotlin.studomat.models.StudomatYearInfo
import dev.etino.fcshared.featuresKotlin.timetable.EventRoom
import dev.etino.fcshared.featuresKotlin.timetable.dao.TimeTableDao

@Database(
    entities = [
        UserRoom::class,
        AttendanceEntry::class,
        EventRoom::class,
        NoteRoom::class,
        ReceiptRoom::class,
        StudentDataRoom::class,
        StudomatSubject::class,
        StudomatYearInfo::class
    ],
    version = 1
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun attendanceDao(): AttendanceDao
    abstract fun timetableDao(): TimeTableDao
    abstract fun noteDao(): NoteDao
    abstract fun iksicaDao(): IksicaDao
    abstract fun studomatDao(): StudomatDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
