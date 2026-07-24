# FESB Companion

<p align="center">
  <img src="iosApp/iosApp/Assets.xcassets/AppIcon.appiconset/Iconbackgroud.png" alt="FESB Companion" width="160"/>
</p>

<p align="center">
A modern Kotlin Multiplatform application for students of the
<strong>Faculty of Electrical Engineering, Mechanical Engineering and Naval Architecture (FESB)</strong>.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-Multiplatform-7F52FF?logo=kotlin&logoColor=white" />
  <img src="https://img.shields.io/badge/Compose-Multiplatform-4285F4" />
  <img src="https://img.shields.io/badge/Android-supported-success" />
  <img src="https://img.shields.io/badge/iOS-supported-lightgrey" />
  <img src="https://img.shields.io/badge/Desktop-supported-blue" />
  <img src="https://img.shields.io/badge/License-MIT-green" />
</p>

---

## About

FESB Companion is an unofficial student companion designed to make everyday university life easier.

The application is built using **Kotlin Multiplatform (KMP)** and **Compose Multiplatform (CMP)**, allowing the same UI, business logic, networking, and data layer to be shared across Android, iOS, and Desktop. This approach minimizes platform-specific code while delivering a consistent user experience on every platform.

## Features

- 📅 Weekly timetable
- 👨‍🏫 Course information
- ✅ Attendance overview
- 🍽️ Student canteen menus
- 📝 Personal notes
- 🌦️ Weather forecast
- 📷 Campus cameras
- 💾 Offline timetable support
- ⚡ Fast, modern and responsive UI

---

## Platforms

| Platform | Status |
|----------|--------|
| Android | ✅ |
| iOS | ✅ |
| Desktop | ✅ |

---

## Tech Stack

### Shared

- Kotlin Multiplatform
- Compose Multiplatform
- Kotlin Coroutines
- Kotlinx Serialization
- Ktor
- Compose Multiplatform
- Gradle Kotlin DSL

### Android

- Jetpack Compose
- AndroidX

### iOS

- Compose Multiplatform for iOS

### Desktop

- Compose Desktop

---

## Project Structure

```
.
├── androidApp/      # Android application
├── iosApp/          # iOS application
├── desktopApp/      # Desktop application
├── shared/          # Shared business logic
├── gradle/
└── docs/
```

---

## Getting Started

### Prerequisites

- JDK 17+
- Android Studio (latest stable)
- Xcode (for iOS)
- Kotlin Multiplatform plugin

### Clone

```bash
git clone https://github.com/FESB-Companion/FESB-Companion-KMP.git
cd FESB-Companion-KMP
```

### Android

```bash
./gradlew :androidApp:installDebug
```

or simply run the **androidApp** configuration from Android Studio.

### Desktop

```bash
./gradlew :desktopApp:run
```

### iOS

Open the `iosApp` project in Xcode and run it on a simulator or physical device.

---

## Architecture

The application uses a shared Compose UI and shared business logic across all supported platforms.

```
             Shared Module
      ┌──────────────────────────┐
      │ Compose UI (100% Shared) │
      │ UI Components            │
      │ Domain Logic             │
      │ Networking               │
      │ Data Layer               │
      └────────────┬─────────────┘
                   │
       ┌───────────┼───────────┐
       │           │           │
    Android       iOS        Desktop
```

---

## Screenshots

| Android | iOS | Desktop |
|---------|-----|---------|
| _Coming Soon_ | _Coming Soon_ | _Coming Soon_ |

---

## Contributing

Contributions are welcome.

1. Fork the repository
2. Create a feature branch

```bash
git checkout -b feature/my-feature
```

3. Commit your changes

```bash
git commit -m "Add awesome feature"
```

4. Push your branch

```bash
git push origin feature/my-feature
```

5. Open a Pull Request

---

## License

This project is licensed under the MIT License.

---

## Disclaimer

FESB Companion is an **unofficial** application and is **not affiliated with or endorsed by FESB or the University of Split**.

It is developed by students and contributors for the student community.
