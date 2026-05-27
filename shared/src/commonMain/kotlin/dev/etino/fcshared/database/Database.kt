package dev.etino.fcshared.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import dev.etino.fcshared.studomat.dao.StudomatDao
import dev.etino.fcshared.studomat.models.StudomatSubject
import dev.etino.fcshared.studomat.models.StudomatYearInfo
import dev.etino.fcshared.home.dao.NoteDao
import dev.etino.fcshared.timetable.dao.TimeTableDao
import dev.etino.fcshared.attendance.dao.AttendanceDao
import dev.etino.fcshared.attendance.models.AttendanceEntry
import dev.etino.fcshared.home.models.NoteRoom
import dev.etino.fcshared.iksica.dao.IksicaDao
import dev.etino.fcshared.iksica.models.ReceiptRoom
import dev.etino.fcshared.iksica.models.StudentDataRoom
import dev.etino.fcshared.login.dao.UserDao
import dev.etino.fcshared.login.user.models.UserRoom
import dev.etino.fcshared.timetable.EventRoom

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
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
