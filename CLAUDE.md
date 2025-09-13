# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview
Arbetstidskalkylator (Work Time Calculator) - An Android app for tracking work hours and calculating salary, built with Kotlin.

## Build Commands
- Build debug APK: `./gradlew assembleDebug`
- Build release APK: `./gradlew assembleRelease`
- Clean project: `./gradlew clean`
- Run tests: `./gradlew test`

## Architecture
- **MVVM Architecture** with Repository pattern
- **Room Database** for local data storage
- **ViewBinding** for UI interaction
- **LiveData/ViewModel** for reactive UI updates

### Key Components
- `TimeEntry` - Data model for work time entries
- `TimeEntryDao` - Database access object
- `TimeDatabase` - Room database instance
- `TimeEntryRepository` - Data repository layer
- `TimeEntryViewModel` - ViewModel for UI logic
- `MainActivity` - Main UI with time entry form

### Features
- Add work time entries with start/end times
- Set hourly rate per entry
- Automatic lunch break deduction (30 min for >6 hour shifts)
- Calculate total hours and earnings
- Swedish language interface

## Development Notes
- Uses Kotlin with ViewBinding
- Room database for persistence
- Material Design 3 components
- Target SDK 34, Min SDK 24
- do not build a new version everytime i start up