package dev.etino.fcshared.features.login

import dev.etino.fcshared.attendance.models.AttendanceEntry
import dev.etino.fcshared.iksica.models.ReceiptRoom
import dev.etino.fcshared.iksica.models.StudentDataRoom
import dev.etino.fcshared.studomat.models.StudomatSubject
import dev.etino.fcshared.studomat.models.StudomatYearInfo
import dev.etino.fcshared.timetable.Event
import dev.etino.fcshared.timetable.EventRoom
import dev.etino.fcshared.timetable.TimetableType
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock


val studomatSubjectTestData = listOf(
    StudomatSubject(
        id = "1",
        name = "Introduction to Programming",
        electiveGroup = "Core",
        semester = "1",
        lectures = "30",
        exercises = "15",
        ectsEnrolled = "5",
        isTaken = "Yes",
        status = "Passed",
        grade = "A",
        examDate = "2023-06-01",
        year = "2023",
        course = "Computer Science",
        isPassed = true
    ),
    StudomatSubject(
        id = "2",
        name = "Discrete Mathematics",
        electiveGroup = "Core",
        semester = "1",
        lectures = "45",
        exercises = "30",
        ectsEnrolled = "6",
        isTaken = "Yes",
        status = "Passed",
        grade = "B",
        examDate = "2023-06-15",
        year = "2023",
        course = "Computer Science",
        isPassed = true
    ),
    StudomatSubject(
        id = "3",
        name = "Digital Logic",
        electiveGroup = "Core",
        semester = "1",
        lectures = "30",
        exercises = "20",
        ectsEnrolled = "5",
        isTaken = "Yes",
        status = "Passed",
        grade = "C",
        examDate = "2023-07-01",
        year = "2023",
        course = "Computer Science",
        isPassed = true
    ),
    StudomatSubject(
        id = "4",
        name = "Data Structures",
        electiveGroup = "Core",
        semester = "3",
        lectures = "45",
        exercises = "30",
        ectsEnrolled = "6",
        isTaken = "Yes",
        status = "Passed",
        grade = "A",
        examDate = "2023-12-20",
        year = "2024",
        course = "Computer Science 2",
        isPassed = true
    ),
    StudomatSubject(
        id = "5",
        name = "Computer Architecture",
        electiveGroup = "Core",
        semester = "3",
        lectures = "45",
        exercises = "30",
        ectsEnrolled = "6",
        isTaken = "Yes",
        status = "Passed",
        grade = "B",
        examDate = "2024-01-10",
        year = "2024",
        course = "Computer Science 2",
        isPassed = true
    ),
    StudomatSubject(
        id = "6",
        name = "Operating Systems",
        electiveGroup = "Core",
        semester = "3",
        lectures = "45",
        exercises = "30",
        ectsEnrolled = "6",
        isTaken = "Yes",
        status = "Passed",
        grade = "A",
        examDate = "2024-02-05",
        year = "2024",
        course = "Computer Science 2",
        isPassed = true
    ),
)

val studomatYearInfoTestData = listOf(
    StudomatYearInfo().apply {
        id = "1"
        courseName = "Computer Science"
        studyProgram = "Undergraduate"
        parallelStudy = "Full-time"
        yearOfCourse = 1
        enrollmentIndicator = "Enrolled"
        payment = false
        fundingBasis = "State-funded"
        universityCenter = "Main Campus"
        studentRightsValidUntil = "2024-06-30"
        enrollmentDate = "2023-09-01"
        enrollmentCompleted = true
        academicYear = "2023"
        href = "http://example.com/enrollment/2023"
    },
    StudomatYearInfo().apply {
        id = "2"
        courseName = "Computer Science 2"
        studyProgram = "Undergraduate"
        parallelStudy = "Full-time"
        yearOfCourse = 2
        enrollmentIndicator = "Enrolled"
        payment = false
        fundingBasis = "State-funded"
        universityCenter = "Main Campus"
        studentRightsValidUntil = "2025-06-30"
        enrollmentDate = "2024-09-01"
        enrollmentCompleted = true
        academicYear = "2024"
        href = "http://example.com/enrollment/2024"
    }
)


private val today: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

// Monday of this week
private val thisWeekMonday: LocalDate = run {
    val todayDow = today.dayOfWeek.ordinal    // 0=Monday, 6=Sunday
    today.minus(todayDow, DateTimeUnit.DAY)
}

/**
 * Returns a LocalDateTime for the given day of this week, at the specified hour and minute.
 */
private fun thisWeekDate(dayOfWeek: DayOfWeek, hour: Int, minute: Int): LocalDateTime {
    val diff = dayOfWeek.ordinal - thisWeekMonday.dayOfWeek.ordinal
    val targetDate = thisWeekMonday.plus(diff, DateTimeUnit.DAY)
    return targetDate.atTime(hour, minute)
}

val eventsTestData = listOf(
    EventRoom(
        Event(
            id = "532059",
            name = "Kriptografija i mrežna sigurnost",
            shortName = "KIMS",
            colorId = -65536, // bright red
            professor = "Čagalj Mario",
            eventType = TimetableType.PREDAVANJE,
            groups = "",
            classroom = "C501",
            start = thisWeekDate(DayOfWeek.MONDAY, 10, 15),
            end = thisWeekDate(DayOfWeek.MONDAY, 12, 0),
            description = "C501"
        )
    ),
    EventRoom(
        Event(
            id = "534198",
            name = "Metode optimizacije",
            shortName = "MO",
            colorId = -16776961, // bright blue
            professor = "Bašić Martina",
            eventType = TimetableType.LABORATORIJSKA_VJEZBA,
            groups = "Grupa 1,",
            classroom = "B420",
            start = thisWeekDate(DayOfWeek.MONDAY, 18, 30),
            end = thisWeekDate(DayOfWeek.MONDAY, 20, 0),
            description = "B420"
        )
    ),
    EventRoom(
        Event(
            id = "532144",
            name = "Podržano strojno učenje",
            shortName = "PSU",
            colorId = -16711936, // bright green
            professor = "Vasilj Josip",
            eventType = TimetableType.PREDAVANJE,
            groups = "",
            classroom = "A243",
            start = thisWeekDate(DayOfWeek.TUESDAY, 8, 15),
            end = thisWeekDate(DayOfWeek.TUESDAY, 10, 0),
            description = "A243"
        )
    ),
    EventRoom(
        Event(
            id = "532084",
            name = "Metode optimizacije",
            shortName = "MO",
            colorId = -256, // bright yellow
            professor = "Marasović Jadranka",
            eventType = TimetableType.PREDAVANJE,
            groups = "",
            classroom = "C502",
            start = thisWeekDate(DayOfWeek.TUESDAY, 10, 15),
            end = thisWeekDate(DayOfWeek.TUESDAY, 12, 0),
            description = "C502"
        )
    ),
    EventRoom(
        Event(
            id = "532120",
            name = "IP komunikacije",
            shortName = "IK",
            colorId = -65536, // bright red
            professor = "Russo Mladen",
            eventType = TimetableType.PREDAVANJE,
            groups = "",
            classroom = "A105",
            start = thisWeekDate(DayOfWeek.TUESDAY, 12, 15),
            end = thisWeekDate(DayOfWeek.TUESDAY, 14, 0),
            description = "A105"
        )
    ),
    EventRoom(
        Event(
            id = "538989",
            name = "Podržano strojno učenje",
            shortName = "PSU",
            colorId = -16711936, // bright green
            professor = "Vasilj Josip",
            eventType = TimetableType.LABORATORIJSKA_VJEZBA,
            groups = "Grupa 1,",
            classroom = "A507",
            start = thisWeekDate(DayOfWeek.THURSDAY, 10, 0),
            end = thisWeekDate(DayOfWeek.THURSDAY, 12, 15),
            description = "A507"
        )
    ),
    EventRoom(
        Event(
            id = "535595",
            name = "Jezici i prevoditelji",
            shortName = "JIP",
            colorId = -65536, // bright red
            professor = "Sikora Marjan",
            eventType = TimetableType.LABORATORIJSKA_VJEZBA,
            groups = "Grupa 1,",
            classroom = "B526",
            start = thisWeekDate(DayOfWeek.THURSDAY, 8, 30),
            end = thisWeekDate(DayOfWeek.THURSDAY, 10, 0),
            description = "B526"
        )
    ),
    EventRoom(
        Event(
            id = "535336",
            name = "IP komunikacije",
            shortName = "IK",
            colorId = -16711936, // bright green
            professor = "Meter Davor",
            eventType = TimetableType.LABORATORIJSKA_VJEZBA,
            groups = "Grupa 1,",
            classroom = "B526",
            start = thisWeekDate(DayOfWeek.FRIDAY, 8, 0),
            end = thisWeekDate(DayOfWeek.FRIDAY, 9, 30),
            description = "B526"
        )
    ),
)

val attendanceTestData = listOf(
    AttendanceEntry(
        id = "ca073d3e-9a81-3e63-a698-a1a7d3813919",
        subject = "Grid računalni sustavi",
        type = "Predavanja",
        attended = 5,
        absent = 6,
        required = 8,
        semester = 1,
        total = 11
    ),
    AttendanceEntry(
        id = "43f52150-ad62-3947-bd6f-06afa02883dc",
        subject = "Grid računalni sustavi",
        type = "Auditorne vježbe",
        attended = 9,
        absent = 3,
        required = 9,
        semester = 1,
        total = 12
    ),
    AttendanceEntry(
        id = "c4a9507a-fde0-3f19-b35c-48174332c562",
        subject = "Multimedijski sustavi",
        type = "Predavanja",
        attended = 1,
        absent = 10,
        required = 8,
        semester = 1,
        total = 11
    ),
    AttendanceEntry(
        id = "7b91b12f-2fce-3b54-b130-84373e4bca89",
        subject = "Multimedijski sustavi",
        type = "Laboratorijske vježbe",
        attended = 10,
        absent = 1,
        required = 11,
        semester = 1,
        total = 11
    ),
    AttendanceEntry(
        id = "9dc417e6-8def-368b-a179-5b6385c2cfed",
        subject = "Paralelno programiranje",
        type = "Predavanja",
        attended = 2,
        absent = 8,
        required = 7,
        semester = 1,
        total = 10
    ),
    AttendanceEntry(
        id = "d9ea8f53-f4eb-3712-b775-91e8247353c1",
        subject = "Paralelno programiranje",
        type = "Laboratorijske vježbe",
        attended = 7,
        absent = 1,
        required = 8,
        semester = 1,
        total = 8
    ),
    AttendanceEntry(
        id = "fba29b79-ae06-331c-8396-bef6acc9de07",
        subject = "Poslovni informacijski sustavi",
        type = "Predavanja",
        attended = 3,
        absent = 10,
        required = 10,
        semester = 1,
        total = 13
    ),
    AttendanceEntry(
        id = "bed1a5df-3673-3f55-b085-7f881029ac67",
        subject = "Poslovni informacijski sustavi",
        type = "Laboratorijske vježbe",
        attended = 9,
        absent = 3,
        required = 12,
        semester = 1,
        total = 12
    ),
    AttendanceEntry(
        id = "7467a4d4-ceb0-3451-b09c-038caf8753b0",
        subject = "Sigurnost bežičnih mreža",
        type = "Predavanja",
        attended = 12,
        absent = 2,
        required = 10,
        semester = 1,
        total = 14
    ),
    AttendanceEntry(
        id = "140cf7a4-fc4e-3013-8c57-002e6d98c7bd",
        subject = "Sigurnost bežičnih mreža",
        type = "Laboratorijske vježbe",
        attended = 11,
        absent = 1,
        required = 12,
        semester = 1,
        total = 12
    ),
    AttendanceEntry(
        id = "72bf768b-b7c5-3f0d-9222-8e163678eaf0",
        subject = "Ugradbeni računalni sustavi",
        type = "Predavanja",
        attended = 10,
        absent = 2,
        required = 9,
        semester = 1,
        total = 12
    )
)

val studentDataTestData = StudentDataRoom(
    id = "1",
    imageUrl = "data:image/jpeg;base64,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",
    nameSurname = "Ivan Horvat",
    rightsLevel = "Redoviti student",
    oib = "12345678901",
    jmbag = "0246034829",
    cardNumber = "1002003001",
    rightsFrom = "01.10.2024",
    rightsTo = "30.09.2025",
    dailySupportInCents = 3000,
    balanceInCents = 15050,
    spentTodayInCents = 1000
)

val receiptsTestData = listOf(
    ReceiptRoom(
        restaurant = "Menza FESB",
        date = "2025-10-01",
        dateString = "1. listopada 2025.",
        time = "12:15",
        authorised = "Ivan Horvat",
        href = "https://example.com/receipts/1",
        id = "1",
        receiptAmountInCents = 2500,
        subsidizedAmountInCents = 1800,
        paidAmountInCents = 700
    ),
    ReceiptRoom(
        restaurant = "Menza Kampus",
        date = "2025-10-02",
        dateString = "2. listopada 2025.",
        time = "13:05",
        authorised = "Ana Kovač",
        href = "https://example.com/receipts/2",
        id = "2",
        receiptAmountInCents = 2850,
        subsidizedAmountInCents = 2000,
        paidAmountInCents = 850
    ),
    ReceiptRoom(
        restaurant = "Restoran Studentski Dom",
        date = "2025-10-03",
        dateString = "3. listopada 2025.",
        time = "18:40",
        authorised = "Petar Marić",
        href = "https://example.com/receipts/3",
        id = "3",
        receiptAmountInCents = 3200,
        subsidizedAmountInCents = 2200,
        paidAmountInCents = 1000
    ),
    ReceiptRoom(
        restaurant = "Menza FESB",
        date = "2025-10-04",
        dateString = "4. listopada 2025.",
        time = "11:55",
        authorised = "Luka Babić",
        href = "https://example.com/receipts/4",
        id = "4",
        receiptAmountInCents = 2200,
        subsidizedAmountInCents = 1500,
        paidAmountInCents = 700
    ),
    ReceiptRoom(
        restaurant = "Menza Kampus",
        date = "2025-10-05",
        dateString = "5. listopada 2025.",
        time = "14:20",
        authorised = "Marija Jurić",
        href = "https://example.com/receipts/5",
        id = "5",
        receiptAmountInCents = 2700,
        subsidizedAmountInCents = 1850,
        paidAmountInCents = 850
    ),
    ReceiptRoom(
        restaurant = "Restoran Studentski Dom",
        date = "2025-10-06",
        dateString = "6. listopada 2025.",
        time = "19:15",
        authorised = "Nikola Vuković",
        href = "https://example.com/receipts/6",
        id = "6",
        receiptAmountInCents = 3000,
        subsidizedAmountInCents = 2100,
        paidAmountInCents = 900
    ),
    ReceiptRoom(
        restaurant = "Menza FESB",
        date = "2025-10-07",
        dateString = "7. listopada 2025.",
        time = "12:45",
        authorised = "Karla Radić",
        href = "https://example.com/receipts/7",
        id = "7",
        receiptAmountInCents = 2400,
        subsidizedAmountInCents = 1600,
        paidAmountInCents = 800
    ),
    ReceiptRoom(
        restaurant = "Menza Kampus",
        date = "2025-10-08",
        dateString = "8. listopada 2025.",
        time = "13:30",
        authorised = "Mateo Novak",
        href = "https://example.com/receipts/8",
        id = "8",
        receiptAmountInCents = 2600,
        subsidizedAmountInCents = 1800,
        paidAmountInCents = 800
    ),
    ReceiptRoom(
        restaurant = "Restoran Studentski Dom",
        date = "2025-10-09",
        dateString = "9. listopada 2025.",
        time = "20:05",
        authorised = "Ema Perić",
        href = "https://example.com/receipts/9",
        id = "9",
        receiptAmountInCents = 3300,
        subsidizedAmountInCents = 2300,
        paidAmountInCents = 1000
    ),
    ReceiptRoom(
        restaurant = "Menza FESB",
        date = "2025-10-10",
        dateString = "10. listopada 2025.",
        time = "12:10",
        authorised = "Domagoj Živković",
        href = "https://example.com/receipts/10",
        id = "10",
        receiptAmountInCents = 2100,
        subsidizedAmountInCents = 1500,
        paidAmountInCents = 600
    )
)