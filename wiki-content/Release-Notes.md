# 📋 Release Notes

Versionshistorik för Arbetstidskalkylator med detaljerade ändringsloggar.

## 🚀 v1.1.136 (Senaste) - 2025-01-28

### ✨ Nya Funktioner
- **🔄 AI-baserad schemaoptimering** - Intelligent schemaplanering med ML-algoritmer
- **📊 Avancerad tidsberäkning** - Förbättrad precision för arbetstidsberäkningar
- **🎯 Förbättrad export** - Nya format och detaljerade rapporter

### 🐛 Bugfixar
- Fixade krasch vid export av stora datamängder
- Löste problem med helgdagsdetektering för 2025
- Korrigerade OB-beräkningar för nattarbete över midnatt

### 🔧 Tekniska Förbättringar
- Uppdaterad Jetpack Compose BOM till 2024.02.00
- Förbättrad prestanda för stora dataset
- Optimerad minnehantering

### 📱 APK Information
- **Filnamn**: `Tidsregistrerings-Kalkylator-1.1.136-debug-beta.apk`
- **Storlek**: ~15 MB
- **Min Android**: 5.0 (API 21)
- **Target SDK**: 34 (Android 14)

---

## 🎯 v1.1.127 - 2024-12-15

### 🌟 Huvudfunktioner
- **📅 Fullständigt omdesignad kalendervy** - Modern design som matchar användarönskemål
- **🔧 Interaktiv funktionalitet** - Klicka på vilken dag som helst för dagsinformation
- **✨ Lägg till/redigera arbetsdagar** direkt från kalendern
- **🎯 Automatisk datumfyllning** - Valt datum fylls automatiskt i formulär

### 🎨 UI/UX Förbättringar
- **🎨 Smal och elegant design** - Förbättrad användarupplevelse
- **📊 Månadsnavigering** med vänster/höger pilar
- **🟢 Dagens datum** markerat med röd cirkel
- **📝 Popup-information** med detaljerad dagsinformation
- **⚡ Förbättrad prestanda** i kalenderrendering

### 🔧 Tekniska Ändringar
- Ny `CalendarView` komponent med Material Design 3
- Optimerad state management för kalenderdata
- Förbättrad navigering mellan månader

---

## ⚡ v1.1.82-beta - 2024-11-20

### 🍽️ Rasthantering
- **🎯 Förbättrade rastinställningar** och förbättrad layout i AddDayDialog
- **🔼 Flyttad 'Använd mall'** knapp till toppen för bättre användbarhet
- **⏰ Klickbara tidsväljare** för alla tidsfält med intuitivt gränssnitt
- **🍽️ Avancerad rastinmatning** med både klickbara tider och direktinmatning

### 📅 Datum & Tid
- **📅 Kalenderväljare** för enkelt datumval
- **✨ Förbättrat användarupplevelse** i hela AddDayDialog
- **🕐 Smartare tidsvalidering** med svenska felmeddelanden

### 🐛 Bugfixar
- Fixade problem med rastberäkningar för långa arbetspass
- Löste UI-buggar i tidsväljare
- Korrigerade valideringslogik för arbetstider

---

## 🎨 v1.1.71-beta - 2024-10-25

### 🔄 Duplicerade Förbättringar
- **🎯 Förbättrade rastinställningar** och förbättrad layout i AddDayDialog
- **🔼 Flyttad 'Använd mall'** knapp till toppen för bättre användbarhet
- **⏰ Klickbara tidsväljare** för alla tidsfält med intuitivt gränssnitt
- **🍽️ Avancerad rastinmatning** med både klickbara tider och direktinmatning
- **📅 Kalenderväljare** för enkelt datumval
- **✨ Förbättrat användarupplevelse** i hela AddDayDialog

*Obs: Denna version innehöll samma förbättringar som v1.1.82-beta*

---

## ✨ v1.1.62-beta - 2024-09-30

### 🆕 Ny AddDayDialog
- **✨ Ny AddDayDialog** med fullständig arbetsdag-konfiguration
- **🔧 Förbättrad textinmatning** utan automatiska nollor som störde användare
- **📅 Interaktiv datumväljare** med navigeringsknappar för enkel månadsnavigering

### 🍽️ Rasthantering
- **🍽️ Smart rasthantering** (automatisk/manuell)
  - **Automatisk**: Systemet beräknar lagstadgade raster baserat på arbetstid
  - **Manuell**: Användaren anger exakt rastlängd

### ✅ Validering
- **✅ Förbättrad validering** med svenska felmeddelanden
- Kontroll av arbetstider och rastperioder
- Användarvänliga varningar och tips

---

## 🏗️ v1.1.0 - 2024-08-15

### 🎯 Grundläggande Funktioner
- **📊 Tidsrapportering** med grundläggande kalendervy
- **💰 Löneberäkningar** enligt Detaljhandelsavtalet 2025-2026
- **⚙️ Grundinställningar** för avtalsnivåer och OB-satser
- **💾 Lokal datalagring** med JSON-baserad persistens

### 🔧 Teknisk Foundation
- **Kotlin + Jetpack Compose** arkitektur
- **MVVM pattern** för state management
- **Material Design** komponenter
- **Offline-first** approach

---

## 📊 Versionsöversikt

| Version | Datum | Huvudfokus | APK Storlek | Nya Funktioner |
|---------|-------|------------|-------------|-----------------|
| **v1.1.136** | 2025-01-28 | AI & Optimering | ~15 MB | AI-schemaoptimering, Avancerad export |
| **v1.1.127** | 2024-12-15 | Kalender-redesign | ~14 MB | Interaktiv kalender, Modern UI |
| **v1.1.82-beta** | 2024-11-20 | Rasthantering | ~13 MB | Förbättrade raster, Klickbara tider |
| **v1.1.71-beta** | 2024-10-25 | Dublering | ~13 MB | Samma som v1.1.82-beta |
| **v1.1.62-beta** | 2024-09-30 | AddDayDialog | ~12 MB | Ny dialog, Smart validering |
| **v1.1.0** | 2024-08-15 | Initial Release | ~11 MB | Grundfunktioner, Lokal lagring |

## 🔄 Migreringsguide

### Från v1.1.127 till v1.1.136
- **Automatisk migration** - Ingen användaråtgärd krävs
- **Nya AI-funktioner** aktiveras automatiskt
- **Befintlig data** bevaras fullständigt

### Från v1.1.82-beta till v1.1.127
- **Kalenderdata** migreras automatiskt
- **UI-inställningar** återställs till standard
- **Backup rekommenderas** innan uppgradering

### Från v1.1.0 till senare versioner
- **JSON-export** innan uppgradering rekommenderas
- **Inställningar** kan behöva konfigureras om
- **Testa funktioner** efter installation

## 🐛 Kända Problem

### v1.1.136
- **Långsam AI-processing** på äldre enheter (Android 6.0-)
- **Minnestryck** vid mycket stora dataset (>1000 arbetsdagar)
- **Export timeout** för extremt stora månadsrapporter

### v1.1.127
- **Kalenderrendering** kan vara långsam på första uppstart
- **Månadsnavigering** ibland kräver dubbelklick
- **Popup-fönster** kan överlappa på små skärmar

### Generella Problem
- **Google Drive backup** kräver stabil internetanslutning
- **Stora CSV-exporter** kan ta lång tid
- **Komplexe OB-beräkningar** kan ge avrundningsfel

## 🔮 Kommande Funktioner

### v1.2.0 (Planerad Q2 2025)
- **🔄 Automatisk synkronisering** mellan enheter
- **📱 Widget support** för snabb tidsregistrering
- **📊 Avancerad analytics** med trendanalys
- **🎨 Temastöd** med dark/light mode

### v1.3.0 (Planerad Q3 2025)
- **🤖 Voice input** för tidsregistrering
- **📍 GPS-baserad** automatisk registrering
- **👥 Multi-user** support för familjer
- **🏢 Företagsversion** med centraliserad hantering

### Långsiktiga Mål
- **☁️ Cloud-native** arkitektur
- **🔗 API integration** med lönesystem
- **📈 Predictive analytics** för schemaplanering
- **🌍 Multi-språk** support (engelska, finska, norska)

## 📞 Supportinformation

### Rapportera Problem
- **🐛 Bug Reports**: [GitHub Issues](https://github.com/tjelite1986/Arbetstidskalkylator/issues/new?template=bug_report.yml)
- **💡 Funktionsförslag**: [Feature Requests](https://github.com/tjelite1986/Arbetstidskalkylator/issues/new?template=feature_request.yml)
- **💬 Diskussioner**: [GitHub Discussions](https://github.com/tjelite1986/Arbetstidskalkylator/discussions)

### Version Support
- **Aktiv support**: v1.1.127 och senare
- **Säkerhetsuppdateringar**: v1.1.0 och senare
- **Legacy support**: Begränsad support för äldre versioner

### Kompatibilitet
- **Android 5.0+**: Alla funktioner stöds
- **Android 7.0+**: Optimerad prestanda
- **Android 10+**: Alla nya funktioner aktiverade
- **Android 14**: Senaste Material Design features

## 📄 Licens & Rättigheter

**MIT License** - Se [LICENSE](https://github.com/tjelite1986/Arbetstidskalkylator/blob/main/LICENSE) för fullständig licenstext.

### Third-Party Libraries
- **Jetpack Compose**: Apache 2.0 License
- **Gson**: Apache 2.0 License
- **Material Components**: Apache 2.0 License
- **Kotlin Standard Library**: Apache 2.0 License

### Upphovsrätt
© 2024-2025 Arbetstidskalkylator Project. Alla rättigheter förbehållna enligt MIT License.

---

**🚀 Tack för att du använder Arbetstidskalkylator!**

*För senaste versionen, besök [GitHub Releases](https://github.com/tjelite1986/Arbetstidskalkylator/releases)*

*För support och feedback: [GitHub Issues](https://github.com/tjelite1986/Arbetstidskalkylator/issues)*