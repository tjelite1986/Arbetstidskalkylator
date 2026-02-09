# 📱 Installation Guide

Denna guide hjälper dig att installera Arbetstidskalkylator på din Android-enhet.

## 📋 Systemkrav

- **📱 Android 5.0 (API 21)** eller senare
- **💾 15 MB** lagringsutrymme
- **🔒 Tillåt installation från okända källor** (för APK-installation)

## 📦 Installation via GitHub Releases (Rekommenderat)

### Steg 1: Ladda ner APK

1. Gå till **[GitHub Releases](https://github.com/tjelite1986/Arbetstidskalkylator/releases/latest)**
2. Ladda ner senaste **APK-fil** (ex. `Tidsregistrerings-Kalkylator-v1.1.127.apk`)
3. Spara filen på din Android-enhet

### Steg 2: Aktivera Installation från Okända Källor

**Android 8.0+ (Oreo och senare):**
1. Öppna **Inställningar** > **Säkerhet & integritet** > **Installera okända appar**
2. Välj **webbläsaren** eller **filhanteraren** du använder
3. Aktivera **"Tillåt från denna källa"**

**Android 7.1 och tidigare:**
1. Öppna **Inställningar** > **Säkerhet** 
2. Aktivera **"Okända källor"**
3. Bekräfta genom att trycka **"OK"**

### Steg 3: Installera Appen

1. Öppna **APK-filen** med filhanteraren
2. Tryck **"Installera"**
3. Vänta tills installationen är klar
4. Tryck **"Öppna"** för att starta appen

## 🔧 Installation för Utvecklare

### Förutsättningar
- **Android Studio** (senaste versionen)
- **JDK 11** eller högre
- **Android SDK** med API 21+

### Klona Repository
```bash
git clone https://github.com/tjelite1986/Arbetstidskalkylator.git
cd Arbetstidskalkylator
```

### Bygg och Installera
```bash
# Navigera till Android-projektet
cd TimeReportCalculator

# Bygg debug APK
./gradlew assembleDebug

# Installera på ansluten enhet
./gradlew installDebug
```

### Alternativt via Android Studio
1. Öppna **Android Studio**
2. Välj **"Open an Existing Project"**
3. Navigera till `TimeReportCalculator/` mappen
4. Tryck **"Run"** (▶️) för att bygga och installera

## 📱 Första Uppstart

### Steg 1: Välkomstskärm
- Läs igenom appöversikten
- Tryck **"Kom igång"** för att fortsätta

### Steg 2: Grundinställningar
1. **Ålder/Erfarenhet**: Välj din avtalsnivå
2. **Arbetsplatstyp**: Välj "Butik" eller "Lager"
3. **Skattesats**: Ange din preliminärskatt (standard 30%)

### Steg 3: Lägg till din första arbetsdag
1. Tryck **"+"** knappen
2. Välj **datum** från kalendern
3. Ange **arbetstider** (start/slut)
4. Konfigurera **raster** om nödvändigt
5. Tryck **"Lägg till"**

## ⚙️ Appbehörigheter

Appen begär följande behörigheter:

| Behörighet | Syfte | Obligatorisk |
|------------|-------|--------------|
| **Lagring** | Spara tidsrapporter lokalt | ✅ Ja |
| **Internet** | Google Drive backup (frivilligt) | ❌ Nej |
| **Konto** | Google Drive autentisering | ❌ Nej |

## 🔧 Felsökning

### Installation Misslyckas
**Problem**: "Appen kan inte installeras"
**Lösning**: 
- Kontrollera att "Okända källor" är aktiverat
- Se till att du har tillräckligt lagringsutrymme
- Starta om enheten och försök igen

### Appen Kraschar vid Start
**Problem**: Appen stängs direkt efter start
**Lösning**:
- Kontrollera att enheten har Android 5.0+
- Rensa appdata: Inställningar > Appar > Arbetstidskalkylator > Lagring > Rensa data
- Avinstallera och installera om appen

### Kan inte se data
**Problem**: Tidigare data visas inte
**Lösning**:
- Kontrollera att du har samma version installerad
- Försök importera backup om tillgänglig
- Kontakta support om problemet kvarstår

## 📞 Support

Om du stöter på problem:

1. **🐛 [Rapportera Bug](https://github.com/tjelite1986/Arbetstidskalkylator/issues/new?template=bug_report.yml)**
2. **❓ [Ställ Frågor](https://github.com/tjelite1986/Arbetstidskalkylator/discussions)**
3. **📖 [Läs Användarguide](User-Guide)**

## 🔄 Uppdateringar

Appen uppdateras regelbundet. För att få senaste versionen:

1. Besök **[GitHub Releases](https://github.com/tjelite1986/Arbetstidskalkylator/releases)**
2. Ladda ner **senaste APK-fil**
3. Installera över befintlig version (data bevaras)

---

**🎉 Grattis! Du är nu redo att börja använda Arbetstidskalkylator.**

**Nästa steg**: Läs **[Användarguide](User-Guide)** för att lära dig alla funktioner.