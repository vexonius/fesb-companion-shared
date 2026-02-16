package dev.etino.fcshared.database

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.etino.fcshared.timetable.dao.TimeTableDao
import dev.etino.fcshared.attendance.dao.AttendanceDao
import dev.etino.fcshared.attendance.models.AttendanceEntry
import dev.etino.fcshared.login.dao.UserDao
import dev.etino.fcshared.login.user.models.UserRoom
import dev.etino.fcshared.timetable.EventRoom

@Database(
    entities = [
        UserRoom::class,
        AttendanceEntry::class,
        EventRoom::class,
        /*NoteRoom::class,
        StudentDataRoom::class,
        ReceiptRoom::class,
        StudomatSubject::class,
        StudomatYearInfo::class*/
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun attendanceDao(): AttendanceDao
    abstract fun timetableDao(): TimeTableDao
    /*abstract fun noteDao(): NoteDao
    abstract fun iksicaDao(): IksicaDao
    abstract fun studomatDao(): StudomatDao*/
}