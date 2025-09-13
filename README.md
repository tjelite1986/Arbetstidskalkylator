# Arbetstidskalkylator

En Android-app för att beräkna arbetstid och lön, byggd med Kotlin.

## Översikt

Arbetstidskalkylator hjälper dig att enkelt registrera arbetstid och beräkna din lön. Appen är specialdesignad för svenska användare och stöder automatiska raster och löneberäkningar.

## Funktioner

- ⏰ Registrera arbetstid med start- och sluttider
- 💰 Ange timlön per arbetspass
- 🍽️ Automatisk rastberäkning (30 min för pass över 6 timmar)
- 📊 Beräkna totala arbetstimmar och intjänade pengar
- 🇸🇪 Svenskt gränssnitt
- 💾 Lokal datalagring med Room database

## Teknisk information

### Arkitektur
- **MVVM Architecture** med Repository pattern
- **Room Database** för lokal datalagring
- **ViewBinding** för UI-interaktion
- **LiveData/ViewModel** för reaktiva UI-uppdateringar

### Huvudkomponenter
- `TimeEntry` - Datamodell för arbetstidsposter
- `TimeEntryDao` - Database access object
- `TimeDatabase` - Room database-instans
- `TimeEntryRepository` - Repository-lager
- `TimeEntryViewModel` - ViewModel för UI-logik
- `MainActivity` - Huvudskärm med formulär för tidsregistrering

### Krav
- Android 7.0 (API 24) eller senare
- Kotlin 1.9+
- Material Design 3 komponenter

## Installation

### Från källkod
```bash
# Klona repository
git clone https://github.com/tjelite1986/Arbetstidskalkylator.git

# Öppna i Android Studio
cd Arbetstidskalkylator

# Bygg debug APK
./gradlew assembleDebug
```

### Build-kommandon
```bash
# Bygg debug APK
./gradlew assembleDebug

# Bygg release APK
./gradlew assembleRelease

# Rensa projekt
./gradlew clean

# Kör tester
./gradlew test
```

## Användning

1. **Lägg till arbetspass**: Ange datum, start- och sluttid
2. **Ställ in timlön**: Ange din timlön för passet
3. **Automatiska raster**: Appen drar automatiskt av 30 minuter rast för pass över 6 timmar
4. **Beräkna totalt**: Se sammanfattning av arbetstimmar och intjänade pengar

## Utveckling

### Projektstruktur
```
app/src/main/java/se/thomas/arbetstidskalkylator/
├── data/          # Datamodeller och database
├── ui/            # Activities och fragments
├── viewmodel/     # ViewModels
└── repository/    # Repository-klasser
```

### Utvecklingsanteckningar
- Använder Kotlin med ViewBinding
- Room database för persistering
- Material Design 3 komponenter
- Target SDK 34, Min SDK 24

## Licens

Detta projekt är öppen källkod under MIT-licensen.

## Bidrag

Välkommen att bidra till projektet! Skapa en issue eller pull request på GitHub.

---

**Byggt med ❤️ för svenska arbetare**