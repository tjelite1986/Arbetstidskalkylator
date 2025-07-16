# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is **Arbetstidskalkylator** - a Swedish time reporting and wage calculation Android app for retail workers. Built with Kotlin and Jetpack Compose, it implements the Swedish Retail Agreement (Detaljhandelsavtalet) 2025-2026 wage calculations including OB-tillägg (overtime compensation).

## Common Development Commands

### Building the Project
```bash
# Navigate to Android project directory
cd TimeReportCalculator

# Build debug APK
./gradlew assembleDebug

# Build release APK  
./gradlew assembleRelease

# Clean build
./gradlew clean
```

### Testing
**Note**: This project currently has no test suite. Testing infrastructure needs to be added.

### APK Naming and Versioning
- Auto-increments version code on each build via `version.properties`
- APK output: `Tidsregistrerings-Kalkylator-v{version}.apk`
- Debug builds add `-debug-beta` suffix
- Version format: `Beta 1.1.{buildNumber}`

## Project Architecture

### Core Structure
```
TimeReportCalculator/app/src/main/java/com/example/timereportcalculator/
├── data/           # Business logic and data models
├── ui/             # Jetpack Compose UI components  
├── calculator/     # Wage calculation engine
├── export/         # File management and backup
├── navigation/     # App navigation
└── debug/          # Debug utilities
```

### Key Components

**Data Layer (`data/`)**:
- `TimeEntry.kt` - Core data model for work days with automatic wage calculations
- `Settings.kt` - App configuration including contract levels and OB rates
- `HolidayManager.kt` - Swedish holiday detection and company-specific holidays
- `WorkShiftTemplate.kt` + `WorkShiftTemplateManager.kt` - Shift templates with JSON persistence

**UI Layer (`ui/`)**:
- `components/AddDayDialog.kt` - Main work day entry dialog with time pickers and break settings
- `screens/` - Main app screens (Home, Settings, Statistics, etc.)
- `theme/` - Material Design 3 theming

**Business Logic**:
- `calculator/PayCalculator.kt` - Implements Swedish retail agreement wage calculations
- Different OB rates for retail ("Butik") vs warehouse ("Lager") work
- Age and experience-based contract levels
- Holiday, sick day, and overtime calculations

### Data Storage
- **Offline-first**: Local JSON files in app's private directory
- **Backup**: Optional Google Drive integration via `export/BackupManager.kt`
- **Export**: JSON and Excel-compatible formats
- **No Database**: Uses file-based persistence with Gson serialization

### Swedish Labor Law Implementation
The app implements official Swedish retail agreement rates:
- Age-based minimum wages (16-19 years)
- Experience-based wage levels (1, 2, 3+ years)
- Time-specific OB rates for different workplace types
- Automatic Swedish holiday detection
- 12% vacation compensation calculation

## Development Guidelines

### Code Style
- **Kotlin**: Modern Kotlin with coroutines and flow
- **Compose**: Declarative UI with state hoisting
- **MVVM Pattern**: ViewModels manage UI state, data classes hold business logic
- **Material Design**: Follows Material Design 3 guidelines

### Key Dependencies
- Jetpack Compose BOM 2024.02.00
- Gson 2.10.1 for JSON serialization
- Navigation Compose for screen transitions
- Material Icons Extended for comprehensive icon set

### Package Structure Notes
- Currently uses `com.example.timereportcalculator` (should be updated to proper domain)
- Well-organized separation of concerns
- No external analytics or tracking libraries

### Navigation System
- **Enhanced Bottom Navigation**: Modern Material Design 3 styling with smooth animations
- **State-aware Icons**: Different icons for selected/unselected states (outlined vs filled)
- **Professional Animations**: Spring-based transitions with proper easing
- **SmoothBottomBar**: Custom component with gradient backgrounds, pill indicators, and ripple effects

### File Management
- `export/FileManager.kt` handles all file operations
- FileProvider configured for secure file sharing
- Export formats: JSON (native), Excel-compatible CSV

### Release Process
- Follow `RELEASE_PROCESS.md` for version releases
- Automatic APK generation with proper naming
- GitHub releases with auto-tagging
- Branch cleanup after releases while preserving tags

## Important Business Context

This app calculates wages according to the official Swedish Retail Agreement (Detaljhandelsavtalet), making accuracy critical for:
- OB-tillägg (overtime compensation) calculations
- Holiday and sick day pay
- Contract level determinations
- Tax and vacation compensation

All wage calculation logic should be thoroughly tested against the official agreement rates found in the README.md tables.