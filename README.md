# 📊 Arbetstidskalkylator

> **Professionell tidrapporterings- och löneberäkningsapp för svenska detaljhandelsanställda**

[![Android](https://img.shields.io/badge/Android-21%2B-green.svg)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.22-blue.svg)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-2024.02.00-brightgreen.svg)](https://developer.android.com/jetpack/compose)
[![Release](https://img.shields.io/github/v/release/tjelite1986/Arbetstidskalkylator?include_prereleases&label=Latest%20Release)](https://github.com/tjelite1986/Arbetstidskalkylator/releases/latest)
[![Issues](https://img.shields.io/github/issues/tjelite1986/Arbetstidskalkylator)](https://github.com/tjelite1986/Arbetstidskalkylator/issues)
[![License](https://img.shields.io/github/license/tjelite1986/Arbetstidskalkylator)](LICENSE)

En modern Android-app byggd med **Kotlin** och **Jetpack Compose** för att hjälpa detaljhandelsanställda att beräkna arbetstid, OB-ersättningar och löner enligt **Detaljhandelsavtalet 2025-2026**.

## 📱 Appöversikt

<div align="center">

### 🏠 Huvudfunktioner

![Startsida](https://github.com/tjelite1986/Arbetstidskalkylator/blob/main/exempel/Screenshot_20250716_231538_Tidrapport_Kalkylator.png)
*Välkomstsida med appöversikt och funktionsbeskrivning*

![Tidrapport](https://github.com/tjelite1986/Arbetstidskalkylator/blob/main/assets/screenshots/Screenshot_20250716_231551_Tidrapport_Kalkylator.png)
*Tidrapport med periodsammanfattning och totaler*

![Statistik](https://github.com/tjelite1986/Arbetstidskalkylator/blob/main/assets/screenshots/Screenshot_20250716_231622_Tidrapport_Kalkylator.png)
*Statistikvy med detaljerade löneberäkningar och OB-tillägg*

### 📅 Kalender & Tidsregistrering

![Kalendervy](https://github.com/tjelite1986/Arbetstidskalkylator/blob/main/assets/screenshots/Screenshot_20250716_231647_Tidrapport_Kalkylator.png)
*Interaktiv kalendervy med klickbara dagar, veckonummer och månadsnavigering*

![Daginfo popup](https://github.com/tjelite1986/Arbetstidskalkylator/blob/main/assets/screenshots/Screenshot_20250716_231657_Tidrapport_Kalkylator.png)
*Popup med detaljerad dagsinformation och redigeringsmöjligheter*

![Lägg till dag](https://github.com/tjelite1986/Arbetstidskalkylator/blob/main/assets/screenshots/Screenshot_20250716_231605_Tidrapport_Kalkylator.png)
*Fullständig dialog för att lägga till arbetsdagar med alla inställningar*

![Tidsinställningar](https://github.com/tjelite1986/Arbetstidskalkylator/blob/main/assets/screenshots/Screenshot_20250716_231638_Tidrapport_Kalkylator.png)
*Avancerade tidsinställningar och rasthantering*

### 💾 Export & Backup

![Export funktioner](https://github.com/tjelite1986/Arbetstidskalkylator/blob/main/assets/screenshots/Screenshot_20250716_231712_Tidrapport_Kalkylator.png)
*Export och backup-funktioner för datasäkerhet*

![Backup inställningar](https://github.com/tjelite1986/Arbetstidskalkylator/blob/main/assets/screenshots/Screenshot_20250716_231705_Tidrapport_Kalkylator.png)
*Google Drive backup och synkroniseringsinställningar*

### ⚙️ Inställningar & Konfiguration

![Avancerade inställningar](https://github.com/tjelite1986/Arbetstidskalkylator/blob/main/assets/screenshots/Screenshot_20250716_231723_Tidrapport_Kalkylator.png)
*Avancerade inställningar med alla anpassningsmöjligheter*

![Lönenivåer](https://github.com/tjelite1986/Arbetstidskalkylator/blob/main/assets/screenshots/Screenshot_20250716_231738_Tidrapport_Kalkylator.png)
*Detaljhandelsavtalets lönenivåer och avtalsinställningar*

![OB-inställningar](https://github.com/tjelite1986/Arbetstidskalkylator/blob/main/assets/screenshots/Screenshot_20250716_231744_Tidrapport_Kalkylator.png)
*OB-satser för butik och lager enligt kollektivavtalet*

### 📊 Mallar & Scheman

![Arbetsmallar](https://github.com/tjelite1986/Arbetstidskalkylator/blob/main/assets/screenshots/Screenshot_20250716_231801_Tidrapport_Kalkylator.png)
*Arbetsmallar för snabb registrering av återkommande arbetspass*

![Veckoschema](https://github.com/tjelite1986/Arbetstidskalkylator/blob/main/assets/screenshots/Screenshot_20250716_231812_Tidrapport_Kalkylator.png)
*Veckoschema för planering av arbetstider*

![Schema inställningar](https://github.com/tjelite1986/Arbetstidskalkylator/blob/main/assets/screenshots/Screenshot_20250716_231827_Tidrapport_Kalkylator.png)
*Detaljerade schema- och mallinställningar*

</div>

## ✨ Funktioner

### 🕐 **Tidsrapportering**
- **📅 Klickbar kalendervy** - Klicka på arbetstider för detaljerad information
- **ℹ️ Informationsdialog** - Visa starttid, sluttid, rast, arbetstimmar och intjänade pengar
- **⏰ Intelligent tidsregistrering** med start- och sluttider
- **🍽️ Smarta rastinställningar** - både klickbara tider och direktinmatning
- **⏱️ Klickbara tidsväljare** för alla tidsfält med intuitivt gränssnitt
- **📅 Kalenderväljare** för enkelt datumval med månadsnavigering
- **✅ Smart validering** av tider och arbetstimmar med svenska felmeddelanden
- **📊 Periodfiltrering** för vecka, månad eller anpassad period

### 💰 **Löneberäkningar**
- **🤖 Automatisk OB-beräkning** enligt Detaljhandelsavtalet 2025-2026
- **🏪 Skilda OB-satser** för butik och lager
- **📈 Skatteberäkning** med anpassningsbar skattesats
- **🏖️ Semesterersättning** enligt kollektivavtal (12%)
- **📋 Detaljerad uppdelning** av grundlön, OB och totallön

### ⚙️ **Avancerade Inställningar**
- **👥 Avtalsnivåer** med minimilöner för olika åldrar och erfarenhet
- **🔧 Anpassningsbara OB-satser** för alla tidsperioder
- **🇸🇪 Automatisk helgdagsdetektering** för svenska helgdagar
- **🏢 Företagsspecifika helgdagar** som kan läggas till manuellt
- **📅 Kalenderinställningar** med veckonummer och månadsvy
- **🛠️ Förbättrad textinmatning** utan automatiska formaterings-störningar

### 📁 **Data & Export**
- **💾 Offline-funktion** med lokal datalagring
- **📤 JSON-export/import** för säkerhetskopiering
- **☁️ Google Drive-integration** för molnlagring
- **📊 Excel-kompatibel export** för vidare bearbetning

### 🎨 **Modern Design**
- **🎨 Material Design 3** med intuitivt användargränssnitt
- **📅 Förbättrad kalendervy** med veckonummer och elegant layout
- **🌙 Förenklat tema-val** - Ljust, mörkt eller följ system (färgschema borttaget)
- **📱 Responsiv design** som fungerar på alla skärmstorlekar
- **♿ Tillgänglighet** med stöd för skärmläsare

## 🚀 Installation

### Systemkrav
- **📱 Android 5.0 (API 21)** eller senare
- **💾 15 MB** lagringsutrymme
- **🔒 Tillåt installation från okända källor** (för APK-installation)

### Ladda ner

#### 📦 **GitHub Releases (Rekommenderat)**
1. Gå till [🚀 Releases](https://github.com/tjelite1986/Arbetstidskalkylator/releases/latest)
2. Ladda ner senaste **APK-fil** (v1.1.127)
3. Öppna APK-filen på din Android-enhet
4. Följ installationsinstruktionerna
5. Öppna appen och börja registrera arbetstid!

#### 🔧 **För Utvecklare**
```bash
# Klona repository
git clone https://github.com/tjelite1986/Arbetstidskalkylator.git

# Bygg själv
cd Arbetstidskalkylator
./gradlew assembleDebug
```

## 📖 Snabbstart

### 🆕 **Lägg till din första arbetsdag**
1. **📅 Klicka på kalendern** - Välj datum direkt i kalendervy
2. **➕ Tryck "Lägg till ny dag"** - Datum fylls automatiskt i
3. **⏰ Ange arbetstider** med klickbara tidsväljare eller direktinmatning
4. **🍽️ Konfigurera raster** med förbättrade inställningar:
   - **Automatisk**: Systemet beräknar raster baserat på arbetstid
   - **Manuell**: Klickbara tider eller direktinmatning av rastminuter
5. **🛠️ Använd mall** - Knappen är nu placerad överst för enkel åtkomst
6. **📝 Lägg till beskrivning** (valfritt) - "Lagerarbete", "Kundtjänst" etc.
7. **✅ Tryck "Lägg till"** - Dagen sparas och visas på kalendern

### ⚡ **Snabbberäkning**
1. **📋 Fyll i alla arbetsdagar** för perioden
2. **🧮 Tryck "Beräkna totalt"** - Automatisk beräkning av allt
3. **📊 Granska resultatet** i sammanfattningskortet
4. **💾 Exportera data** vid behov (JSON/Excel)

### 🔧 **Anpassa för din situation**
1. **⚙️ Gå till Avancerade inställningar**
2. **👤 Välj avtalsnivå** baserat på ålder/erfarenhet
3. **🏪 Välj arbetsplatstyp** (Butik/Lager) för korrekta OB-satser
4. **⏰ Konfigurera arbetstider** och automatiska raster
5. **🏢 Lägg till företagshelgdagar** om nödvändigt

## 🏗️ Teknisk Information

### Arkitektur
- **🏛️ MVVM-mönster** med Jetpack Compose State Management
- **💾 Offline-first design** med lokal JSON-lagring
- **🎨 Material Design** komponenter för konsistent UX
- **🧩 Modulär kodstruktur** för enkel underhåll och utveckling

### Huvudkomponenter
```
📂 TimeReportCalculator/app/src/main/java/com/example/timereportcalculator/
├── 📁 data/           # Datamodeller och business logic
│   ├── TimeEntry.kt   # Huvuddatamodell för arbetsdagar
│   └── Settings.kt    # Appinställningar och konfiguration
├── 📁 ui/             # Jetpack Compose UI komponenter
│   ├── screens/       # Huvudskärmar (Settings, Statistics)
│   └── components/    # Återanvändbara komponenter (AddDayDialog)
├── 📁 calculator/     # Löneberäkningslogik
│   └── PayCalculator.kt # OB-beräkning enligt Handelsavtalet
├── 📁 export/         # Fil-export funktionalitet
│   └── FileManager.kt # JSON/Excel export och Google Drive
└── 📁 debug/          # Debug-verktyg och SHA1-fingerprint
```

### Dependencies
- **🎯 Jetpack Compose BOM 2024.02.00** - Modern Android UI
- **🎨 Material Design** - Designsystem och komponenter
- **📝 Gson 2.10.1** - JSON serialisering för data
- **🧭 Navigation Compose** - Navigering mellan skärmar
- **🔗 Activity Compose** - Compose integration

## 🤝 Bidra till projektet

### 🐛 **Rapportera problem**
Hittat en bugg eller har en funktionsförfrågan? 

📋 [**Skapa en Issue**](https://github.com/tjelite1986/Arbetstidskalkylator/issues/new/choose)

**Välj rätt typ:**
- **🐛 Bug Report** - För fel och problem
- **✨ Feature Request** - För nya funktioner  
- **🛠️ Custom Issue** - För support och annat

### 💬 **Diskussioner**
För frågor och diskussioner: [💬 GitHub Discussions](https://github.com/tjelite1986/Arbetstidskalkylator/discussions)

### 🔧 **Utveckling**
```bash
# Klona repository
git clone https://github.com/tjelite1986/Arbetstidskalkylator.git

# Öppna i Android Studio
cd Arbetstidskalkylator

# Bygg debug version
./gradlew assembleDebug

# Kör tester
./gradlew test
```

## 📋 Detaljhandelsavtalet 2025-2026

Appen följer officiella riktlinjer från:
- **🏛️ Handelsanställdas förbund**
- **🏢 Svensk Handel** 
- **📄 Kollektivavtal för detaljhandeln**

### 💰 Avtalsnivåer och Minimilöner
| Kategori | Minimilön (kr/tim) | Beskrivning |
|----------|-------------------|-------------|
| **16 år** | 101.48 | Minimilön för 16-åringar enligt avtalet |
| **17 år** | 103.95 | Minimilön för 17-åringar enligt avtalet |
| **18 år** | 155.51 | Minimilön för 18-åringar enligt avtalet |
| **19 år** | 157.44 | Minimilön för 19-åringar enligt avtalet |
| **1 års erfar.** | 160.95 | Efter 1 års branschexperience |
| **2 års erfar.** | 162.98 | Efter 2 års branschexperience |
| **3+ års erfar.** | 165.84 | Efter 3+ års branschexperience |

### 🏪 OB-Satser Butik
| Tidsperiod | OB-Tillägg | Beskrivning |
|------------|------------|-------------|
| **Mån-fre 18:15-20:00** | +50% | Kvällstid vardagar |
| **Mån-fre efter 20:00** | +70% | Sena kvällar vardagar |
| **Lördag efter 12:00** | +100% | Lördag eftermiddag/kväll |
| **Söndag hela dagen** | +100% | Söndag alla timmar |
| **Helgdagar** | +100% | Röda dagar året runt |

### 📦 OB-Satser Lager
| Tidsperiod | OB-Tillägg | Beskrivning |
|------------|------------|-------------|
| **Mån-fre 06:00-07:00** | +40% | Tidig morgon vardagar |
| **Mån-fre 18:00-23:00** | +40% | Kväll vardagar |
| **Mån-fre 23:00-06:00** | +70% | Natt vardagar |
| **Lördag 06:00-23:00** | +40% | Lördag dag |
| **Lördag nätter** | +70% | Lördag natt (00-06, 23-24) |
| **Söndag** | +100% | Söndag alla timmar |
| **Helgdagar** | +100% | Röda dagar året runt |

## 🔒 Integritet & Säkerhet

- **🔐 Ingen data lämnar enheten** utan din explicit tillåtelse
- **💾 Lokal lagring** av all känslig löne- och tidsinformation
- **☁️ Frivillig molnsynkronisering** via Google Drive (du väljer)
- **🚫 Inga annonser** eller tracking av användarbeteende
- **📖 Öppen källkod** för full transparens och säkerhetsgranskning

## 📄 Licens

Detta projekt är licensierat under **MIT License** - se [LICENSE](LICENSE) filen för detaljer.

## 📞 Support & Hjälp

### 🆘 **Behöver du hjälp?**
- **🐛 Buggrapporter**: [Skapa Bug Report](https://github.com/tjelite1986/Arbetstidskalkylator/issues/new?template=bug_report.yml)
- **💡 Funktionsförslag**: [Skapa Feature Request](https://github.com/tjelite1986/Arbetstidskalkylator/issues/new?template=feature_request.yml)
- **❓ Allmänna frågor**: [GitHub Discussions](https://github.com/tjelite1986/Arbetstidskalkylator/discussions)
- **📖 Dokumentation**: [Wiki](https://github.com/tjelite1986/Arbetstidskalkylator/wiki)

### 🔗 **Användbara länkar**
- **📱 Senaste version**: [Releases](https://github.com/tjelite1986/Arbetstidskalkylator/releases/latest)
- **📋 Kända problem**: [Issues](https://github.com/tjelite1986/Arbetstidskalkylator/issues)
- **🏛️ Handelsavtalet**: [Handels.se](https://www.handels.se/)

## 🏆 Versionshistorik

### 🚀 **v1.1.164** (Senaste)
- **ℹ️ Ny informationsdialog för kalender** - Klicka på arbetstider för detaljvy
- **📊 Detaljerad dagsinformation** - Visa starttid, sluttid, rast, arbetstimmar och intjänade pengar
- **🎨 Förenklat tema-system** - Endast ljust/mörkt/följ system, färgschema borttaget
- **📅 Månadsnamn med stor bokstav** - Förbättrad kalenderheader
- **🔧 Förbättrad kalenderinteraktion** - Klickbara arbetstider med popup-info
- **🎯 Bättre användarupplevelse** - Enklare och renare gränssnitt

### 📋 **v1.1.127**
- **📅 Fullständigt omdesignad kalendervy** - Modern design som matchar önskemål
- **🔧 Interaktiv funktionalitet** - Klicka på vilken dag som helst för information
- **✨ Lägg till/redigera arbetsdagar** direkt från kalendern
- **🎯 Automatisk datumfyllning** - Valt datum fylls automatiskt i
- **🎨 Smal och elegant design** - Förbättrad användarupplevelse
- **📊 Månadsnavigering** med vänster/höger pilar
- **🟢 Dagens datum** markerat med röd cirkel
- **📝 Popup-information** med detaljerad dagsinformation
- **⚡ Förbättrad prestanda** i kalenderrendering

### 📋 **Tidigare versioner**
**v1.1.82-beta**:
- 🎯 Förbättrade rastinställningar och förbättrad layout i AddDayDialog
- 🔼 Flyttad 'Använd mall' knapp till toppen för bättre användbarhet
- ⏰ Klickbara tidsväljare för alla tidsfält med intuitivt gränssnitt
- 🍽️ Avancerad rastinmatning med både klickbara tider och direktinmatning
- 📅 Kalenderväljare för enkelt datumval
- ✨ Förbättrat användarupplevelse i hela AddDayDialog

**v1.1.71-beta**:
- 🎯 Förbättrade rastinställningar och förbättrad layout i AddDayDialog
- 🔼 Flyttad 'Använd mall' knapp till toppen för bättre användbarhet
- ⏰ Klickbara tidsväljare för alla tidsfält med intuitivt gränssnitt
- 🍽️ Avancerad rastinmatning med både klickbara tider och direktinmatning
- 📅 Kalenderväljare för enkelt datumval
- ✨ Förbättrat användarupplevelse i hela AddDayDialog

**v1.1.62-beta**:
- ✨ Ny AddDayDialog med fullständig arbetsdag-konfiguration
- 🔧 Förbättrad textinmatning utan automatiska nollor
- 📅 Interaktiv datumväljare med navigeringsknappar
- 🍽️ Smart rasthantering (automatisk/manuell)
- ✅ Förbättrad validering med svenska felmeddelanden

Se [Release Notes](https://github.com/tjelite1986/Arbetstidskalkylator/releases) för komplett versionshistorik.

---

<div align="center">

**🇸🇪 Byggt med ❤️ för svenska detaljhandelsanställda**

*Hjälper dig att få rätt betalt för ditt arbete enligt Detaljhandelsavtalet*

[![Download](https://img.shields.io/badge/📱%20Ladda%20ner-APK-brightgreen?style=for-the-badge)](https://github.com/tjelite1986/Arbetstidskalkylator/releases/latest)
[![Issues](https://img.shields.io/badge/🐛%20Rapportera-Problem-red?style=for-the-badge)](https://github.com/tjelite1986/Arbetstidskalkylator/issues/new/choose)
[![Discussions](https://img.shields.io/badge/💬%20Frågor-Diskussioner-blue?style=for-the-badge)](https://github.com/tjelite1986/Arbetstidskalkylator/discussions)

[⬆️ Tillbaka till toppen](#-arbetstidskalkylator)

</div>
