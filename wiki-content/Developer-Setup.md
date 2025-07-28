# 💻 Developer Setup

Komplett guide för att sätta upp utvecklingsmiljö för Arbetstidskalkylator.

## 🔧 Förutsättningar

### Systemkrav
- **Windows 10+**, **macOS 10.14+**, eller **Linux (Ubuntu 18.04+)**
- **8 GB RAM** (16 GB rekommenderat)
- **20 GB** ledigt diskutrymme
- **Internet** för nedladdning av dependencies

### Nödvändig Programvara

| Verktyg | Version | Nedladdning |
|---------|---------|-------------|
| **Android Studio** | 2023.1+ (Hedgehog) | [developer.android.com](https://developer.android.com/studio) |
| **JDK** | 11 eller 17 | Inkluderad med Android Studio |
| **Git** | 2.30+ | [git-scm.com](https://git-scm.com/) |
| **Android SDK** | API 21-34 | Via Android Studio SDK Manager |

## 📥 Installation & Setup

### Steg 1: Installera Android Studio

1. **Ladda ner** Android Studio från officiella sidan
2. **Kör** installationsprogrammet med standardinställningar
3. **Starta** Android Studio första gången
4. **Välj** "Standard" installation i setup-wizard
5. **Vänta** på att SDK-komponenter laddas ner

### Steg 2: Konfigurera SDK

**Öppna SDK Manager:**
- **Windows/Linux**: File → Settings → Appearance & Behavior → System Settings → Android SDK
- **macOS**: Android Studio → Preferences → Appearance & Behavior → System Settings → Android SDK

**Installera nödvändiga komponenter:**

**SDK Platforms:**
- ✅ Android 14 (API 34) - Target SDK
- ✅ Android 13 (API 33)
- ✅ Android 12 (API 31)
- ✅ Android 11 (API 30)
- ✅ Android 5.0 (API 21) - Minimum SDK

**SDK Tools:**
- ✅ Android SDK Build-Tools 34.0.0
- ✅ Android Emulator
- ✅ Android SDK Platform-Tools
- ✅ Intel x86 Emulator Accelerator (HAXM) (Windows/macOS)

### Steg 3: Klona Repository

```bash
# Klona projekt
git clone https://github.com/tjelite1986/Arbetstidskalkylator.git

# Navigera till projektmapp
cd Arbetstidskalkylator

# Kontrollera branch
git branch -a

# Växla till main branch (om inte redan aktiv)
git checkout main
```

### Steg 4: Öppna Projekt i Android Studio

1. **Starta** Android Studio
2. **Välj** "Open an existing Android Studio project"
3. **Navigera** till `Arbetstidskalkylator/TimeReportCalculator/`
4. **Välj** projektmappen och tryck "OK"
5. **Vänta** på Gradle sync (kan ta 5-10 minuter första gången)

## 🏗️ Projektstruktur

### Root Directory
```
Arbetstidskalkylator/
├── TimeReportCalculator/          # Android projekt
│   ├── app/                       # Huvudapplikation
│   ├── build.gradle              # Projekt build-konfiguration
│   ├── gradle.properties         # Gradle inställningar
│   └── settings.gradle           # Projekt moduler
├── docs/                         # Projektdokumentation
├── assets/                       # Bilder och resurser
├── releases/                     # APK filer och releases
├── README.md                     # Projektöversikt
└── CLAUDE.md                     # Utvecklingsguide
```

### App Module (`app/`)
```
app/
├── src/main/java/com/example/timereportcalculator/
│   ├── MainActivity.kt           # Huvudaktivitet
│   ├── MainApp.kt               # App-entry point
│   ├── TimeReportApp.kt         # App-konfiguration
│   ├── data/                    # Datamodeller och business logic
│   │   ├── TimeEntry.kt         # Huvuddatamodell
│   │   ├── Settings.kt          # App-inställningar
│   │   ├── HolidayManager.kt    # Helgdagshantering
│   │   └── ...
│   ├── ui/                      # Användarupplevelse
│   │   ├── components/          # Återanvändbara komponenter
│   │   │   ├── AddDayDialog.kt
│   │   │   ├── CalendarView.kt
│   │   │   └── ...
│   │   ├── screens/             # Huvudskärmar
│   │   │   ├── HomeScreen.kt
│   │   │   ├── StatisticsScreen.kt
│   │   │   └── ...
│   │   └── theme/               # Material Design tema
│   ├── calculator/              # Löneberäkningar
│   │   └── PayCalculator.kt
│   ├── export/                  # Data export/import
│   │   ├── FileManager.kt
│   │   └── BackupManager.kt
│   └── navigation/              # App navigation
└── src/main/res/                # Android resurser
    ├── values/                  # Strings, colors, themes
    └── xml/                     # Konfigurationsfiler
```

## 📱 Konfigurera Test Environment

### Skapa Virtual Device (AVD)

1. **Öppna** AVD Manager: Tools → AVD Manager
2. **Tryck** "Create Virtual Device"
3. **Välj** enhet (rekommenderat: Pixel 7 Pro)
4. **Välj** API Level 34 (Android 14)
5. **Konfigurera** AVD:
   - Name: `Arbetstid_Test_API34`
   - RAM: 4096 MB
   - Storage: 16 GB
6. **Skapa** och starta emulatorn

### Fysisk Enhet

**Aktivera Developer Options:**
1. Gå till **Settings** → **About phone**
2. **Tryck** "Build number" 7 gånger
3. Gå tillbaka till **Settings** → **Developer options**
4. Aktivera **"USB debugging"**

**Anslut enhet:**
1. Anslut via USB
2. Acceptera **"Allow USB debugging"** prompt
3. Kontrollera i Android Studio att enheten visas

## 🔨 Build Commands

### Via Android Studio GUI

**Debug Build:**
1. Välj `app` i build configuration
2. Tryck **"Run"** (▶️) eller Shift+F10

**Release Build:**
1. Build → Generate Signed Bundle/APK
2. Välj APK → Next
3. Skapa eller välj keystore
4. Build release variant

### Via Command Line

```bash
# Navigera till Android-projektet
cd TimeReportCalculator

# Debug build
./gradlew assembleDebug

# Release build (utan signering)
./gradlew assembleRelease

# Installera på ansluten enhet
./gradlew installDebug

# Rensa build cache
./gradlew clean

# Kör alla tester
./gradlew test

# Kontrollera kod-kvalitet
./gradlew lint
```

### APK Output Locations
- **Debug**: `app/build/outputs/apk/debug/`
- **Release**: `app/build/outputs/apk/release/`

## 🧪 Testing

### Unit Tests

**Kör alla tester:**
```bash
./gradlew test
```

**Test-struktur:**
```
app/src/test/java/com/example/timereportcalculator/
├── calculator/
│   └── PayCalculatorSimpleTest.kt    # Löneberäkning tester
├── data/
│   ├── TimeEntryTest.kt              # Datamodell tester
│   └── HolidayManagerTest.kt         # Helgdag tester
└── export/
    └── FileManagerSimpleTest.kt      # Export/import tester
```

### Integration Tests

**Kör instrumentation tester:**
```bash
./gradlew connectedAndroidTest
```

**Test-struktur:**
```
app/src/androidTest/java/com/example/timereportcalculator/
├── integration/                      # Integration tester
└── ui/                              # UI tester
```

### Test Coverage

**Generera test coverage:**
```bash
./gradlew createDebugCoverageReport
```

**Rapport location:**
`app/build/reports/coverage/debug/index.html`

## 🎯 Development Workflow

### Git Workflow

**Feature Development:**
```bash
# Skapa feature branch
git checkout -b feature/new-calculation-method

# Gör ändringar...
git add .
git commit -m "✨ Add new calculation method for overtime"

# Push till remote
git push origin feature/new-calculation-method

# Skapa Pull Request på GitHub
```

**Commit Message Format:**
```
<typ>: <beskrivning>

✨ feat: Ny funktionalitet
🐛 fix: Bugfix
📚 docs: Dokumentationsändringar
🎨 style: Formatering (ingen kodändring)
♻️ refactor: Code refactoring
⚡ perf: Prestandaförbättringar
✅ test: Lägga till eller ändra tester
🔧 chore: Build process eller auxiliary tools
```

### Code Style

**Kotlin Conventions:**
- Följ [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Använd `ktlint` för automatisk formatering
- Max 120 tecken per rad

**Compose Conventions:**
- Använd `@Preview` för komponenter
- Hoist state uppåt i hierarkin
- Separera stateful och stateless komponenter

### Code Review Checklist

**Innan Pull Request:**
- [ ] Alla tester passerar
- [ ] Ny kod har tester
- [ ] Dokumentation uppdaterad
- [ ] APK bygger utan varningar
- [ ] UI testat på olika skärmstorlekar

## 🐛 Debugging

### Android Studio Debugger

**Sätt breakpoints:**
1. Klicka i marginalerna bredvid radnummer
2. Kör i debug mode (🐛 ikon)
3. App pausar vid breakpoint

**Debugging Windows:**
- **Variables**: Inspektera variabelvärden
- **Call Stack**: Se anropskedja
- **Console**: Loggar och felmeddelanden

### Logcat

**Visa loggar:**
```kotlin
import android.util.Log

class PayCalculator {
    companion object {
        private const val TAG = "PayCalculator"
    }
    
    fun calculate() {
        Log.d(TAG, "Starting wage calculation")
        Log.i(TAG, "Calculation completed: $result")
        Log.e(TAG, "Error in calculation", exception)
    }
}
```

**Filtrera loggar:**
- Package: `com.example.timereportcalculator`
- Tag: `PayCalculator`
- Level: Debug/Info/Error

### Performance Profiling

**CPU Profiler:**
1. Välj running app i profiler
2. Starta CPU recording
3. Utför actions i app
4. Stoppa recording och analysera

**Memory Profiler:**
- Övervaka heap usage
- Identifiera memory leaks
- Optimera object allocation

## 📦 Deployment

### Version Management

**Version Code:** Auto-incrementerad via `version.properties`
```properties
versionCode=127
versionName=1.1.127
```

**APK Naming:**
- Debug: `Tidsregistrerings-Kalkylator-{version}-debug-beta.apk`
- Release: `Tidsregistrerings-Kalkylator-{version}.apk`

### Release Process

1. **Kontrollera** att alla tester passerar
2. **Uppdatera** version i `version.properties`
3. **Bygg** release APK
4. **Testa** APK på fysisk enhet
5. **Skapa** GitHub release
6. **Uppdatera** dokumentation

Se **[Release Notes](Release-Notes)** för versionshistorik.

## 🔗 Användbara Resurser

### Dokumentation
- **[Android Developer Guide](https://developer.android.com/guide)**
- **[Jetpack Compose Docs](https://developer.android.com/jetpack/compose)**
- **[Kotlin Language Reference](https://kotlinlang.org/docs/reference/)**
- **[Material Design 3](https://m3.material.io/)**

### Tools
- **[ADB Commands](https://developer.android.com/studio/command-line/adb)**
- **[Gradle Build Tool](https://gradle.org/)**
- **[ktlint](https://ktlint.github.io/)** - Kotlin linter

### Community
- **[Android Developers Blog](https://android-developers.googleblog.com/)**
- **[Kotlin Blog](https://blog.jetbrains.com/kotlin/)**
- **[Stack Overflow](https://stackoverflow.com/questions/tagged/android)**

## 📞 Support

**Problem med utvecklingsmiljö?**

- **🐛 [Rapportera Issue](https://github.com/tjelite1986/Arbetstidskalkylator/issues)**
- **💬 [Developer Discussions](https://github.com/tjelite1986/Arbetstidskalkylator/discussions)**
- **📖 [API Documentation](API-Documentation)**

---

**🚀 Du är nu redo att börja utveckla!**

*Nästa steg: Läs [API Documentation](API-Documentation) för teknisk implementation*