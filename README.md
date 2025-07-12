# 📊 TimeReportCalculator

> **Professionell tidrapporterings- och löneberäkningsapp för svenska detaljhandelsanställda**

[![Android](https://img.shields.io/badge/Android-21%2B-green.svg)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.22-blue.svg)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-2024.02.00-brightgreen.svg)](https://developer.android.com/jetpack/compose)
[![Version](https://img.shields.io/badge/Version-Beta%201.1.61-orange.svg)](#latest-release)

En modern Android-app byggd med **Kotlin** och **Jetpack Compose** för att hjälpa detaljhandelsanställda att beräkna arbetstid, OB-ersättningar och löner enligt **Detaljhandelsavtalet 2025-2026**.

## ✨ Funktioner

### 🕐 **Tidsrapportering**
- **Enkel daglig tidsregistrering** med start- och sluttider
- **Automatiska raster** baserade på arbetstid eller manuell rastinmatning
- **Intelligent validering** av tider och arbetstimmar
- **Periodfiltrering** för vecka, månad eller anpassad period

### 💰 **Löneberäkningar**
- **Automatisk OB-beräkning** enligt Detaljhandelsavtalet
- **Skilda OB-satser** för butik och lager
- **Skatteberäkning** med anpassningsbar skattesats
- **Semesterersättning** enligt kollektivavtal (12%)
- **Detaljerad uppdelning** av grundlön, OB och totallön

### ⚙️ **Avancerade Inställningar**
- **Avtalsnivåer** med minimilöner för olika åldrar och erfarenhet
- **Anpassningsbara OB-satser** för alla tidsperioder
- **Automatisk helgdagsdetektering** för svenska helgdagar
- **Företagsspecifika helgdagar** som kan läggas till manuellt
- **Kalenderinställningar** med veckonummer och månadsvy

### 📁 **Data & Export**
- **Offline-funktion** med lokal datalagring
- **JSON-export/import** för säkerhetskopiering
- **Google Drive-integration** för molnlagring
- **Excel-kompatibel export** för vidare bearbetning

### 🎨 **Modern Design**
- **Material Design 3** med intuitivt användargränssnitt
- **Mörkt/ljust tema** för optimal synlighet
- **Responsiv design** som fungerar på alla skärmstorlekar
- **Tillgänglighet** med stöd för skärmläsare

## 🚀 Installation

### Krav
- **Android 5.0 (API 21)** eller senare
- **15 MB** lagringsutrymme

### Ladda ner
1. Gå till [Releases](../../releases) 
2. Ladda ner senaste **APK-fil**
3. Installera på din Android-enhet
4. Öppna appen och börja registrera arbetstid!

## 📖 Användning

### 🆕 Lägg till ny arbetsdag
1. Tryck på **"+"** knappen
2. **Välj datum** med datum-väljaren
3. **Ange arbetstider** (start/slut)
4. **Konfigurera raster** (automatisk eller manuell)
5. **Lägg till beskrivning** (valfritt)
6. Tryck **"Lägg till"** för att spara

### ⚡ Snabbberäkning
1. **Fyll i alla arbetsdagar** för perioden
2. Tryck **"Beräkna totalt"** 
3. **Granska resultatet** i sammanfattningskortet
4. **Exportera data** vid behov

### 🔧 Anpassa inställningar
1. Gå till **Avancerade inställningar**
2. **Välj avtalsnivå** baserat på ålder/erfarenhet
3. **Justera OB-satser** för din arbetsplats
4. **Konfigurera arbetstider** och automatiska raster
5. **Lägg till företagshelgdagar** om nödvändigt

## 🏗️ Teknisk Information

### Arkitektur
- **MVVM-mönster** med Jetpack Compose State Management
- **Offline-first design** med lokal JSON-lagring
- **Material Design** komponenter för konsistent UX
- **Modulär kodstruktur** för enkel underhåll

### Huvudkomponenter
```
📂 app/src/main/java/com/example/timereportcalculator/
├── 📁 data/           # Datamodeller och business logic
├── 📁 ui/             # Compose UI komponenter
├── 📁 calculator/     # Löneberäkningslogik
├── 📁 export/         # Fil-export funktionalitet
└── 📁 debug/          # Debug-verktyg
```

### Dependencies
- **Jetpack Compose BOM 2024.02.00** - Modern Android UI
- **Material Design** - Designsystem och komponenter
- **Gson 2.10.1** - JSON serialisering
- **Navigation Compose** - Navigering mellan skärmar
- **Activity Compose** - Compose integration

## 🤝 Bidra till projektet

### Utveckling
```bash
# Klona repository
git clone https://github.com/yourusername/TimeReportCalculator.git

# Öppna i Android Studio
cd TimeReportCalculator

# Bygg debug version
./gradlew assembleDebug

# Kör tester
./gradlew test
```

### Rapportera problem
Hittat en bugg eller har en funktionsförfrågan? [Skapa en issue](../../issues/new) med:
- **Beskrivning** av problemet
- **Steg för att återskapa** 
- **Förväntad vs faktisk** beteende
- **Skärmbilder** om relevant

## 📋 Detaljhandelsavtalet 2025-2026

Appen följer officiella riktlinjer från:
- **Handelsanställdas förbund**
- **Svensk Handel** 
- **Kollektivavtal för detaljhandeln**

### Avtalsnivåer
| Kategori | Minimilön (kr/tim) | Beskrivning |
|----------|-------------------|-------------|
| 16 år | 101.48 | Minimilön för 16-åringar |
| 17 år | 103.95 | Minimilön för 17-åringar |
| 18 år | 155.51 | Minimilön för 18-åringar |
| 19 år | 157.44 | Minimilön för 19-åringar |
| 1 års erfar. | 160.95 | Efter 1 års branschexperience |
| 2 års erfar. | 162.98 | Efter 2 års branschexperience |
| 3+ års erfar. | 165.84 | Efter 3+ års branschexperience |

### OB-Satser Butik
- **Måndag-fredag 18:15-20:00**: 50%
- **Måndag-fredag efter 20:00**: 70%
- **Lördag efter 12:00**: 100%
- **Söndag hela dagen**: 100%
- **Helgdagar**: 100%

### OB-Satser Lager
- **Vardagar 06:00-07:00**: 40%
- **Vardagar 18:00-23:00**: 40%
- **Nätter 23:00-06:00**: 70%
- **Lördag dag**: 40%
- **Söndag**: 100%
- **Helgdagar**: 100%

## 🔒 Integritet & Säkerhet

- **Ingen data lämnar enheten** utan din tillåtelse
- **Lokal lagring** av all känslig information
- **Frivillig molnsynkronisering** via Google Drive
- **Inga annonser** eller tracking
- **Öppen källkod** för full transparens

## 📄 Licens

Detta projekt är licensierat under **MIT License** - se [LICENSE](LICENSE) filen för detaljer.

## 📞 Support

Behöver du hjälp? Kontakta oss via:
- 📧 **Email**: [issues](../../issues)
- 💬 **GitHub Discussions**: [discussions](../../discussions)
- 📖 **Wiki**: [dokumentation](../../wiki)

---

<div align="center">

**Byggt med ❤️ för svenska detaljhandelsanställda**

[⬆️ Tillbaka till toppen](#-timereportcalculator)

</div>