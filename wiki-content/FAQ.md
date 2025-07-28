# ❓ Vanliga Frågor (FAQ)

Svar på de vanligaste frågorna om Arbetstidskalkylator.

## 📱 Installation & Setup

### F: Vilka Android-versioner stöds?
**S:** Appen fungerar på Android 5.0 (API 21) och senare. För bästa prestanda rekommenderas Android 7.0+.

### F: Varför kan jag inte installera APK-filen?
**S:** Du behöver aktivera "Installation från okända källor":
- **Android 8.0+**: Inställningar > Säkerhet > Installera okända appar > [Välj app] > Tillåt
- **Android 7.1-**: Inställningar > Säkerhet > Okända källor (aktivera)

### F: Hur mycket lagringsutrymme behövs?
**S:** Appen kräver cirka 15 MB för installation. Ytterligare utrymme behövs för tidsdata (vanligtvis under 1 MB per år).

### F: Fungerar appen offline?
**S:** Ja! All grundfunktionalitet fungerar utan internetanslutning. Internet behövs endast för Google Drive backup.

## ⏰ Tidsregistrering

### F: Kan jag registrera arbetstider retroaktivt?
**S:** Ja, du kan lägga till arbetsdagar för vilket datum som helst. Välj bara önskat datum i kalendern.

### F: Hur hanterar jag arbetspass över midnatt?
**S:** 
1. Ange starttid (ex: 22:00)
2. Ange sluttid nästa dag (ex: 06:00)
3. Appen beräknar automatiskt tiden över midnatt

### F: Kan jag ändra en redan registrerad dag?
**S:** Ja! Klicka på dagen i kalendern och välj "Redigera" för att ändra arbetstider, raster eller beskrivning.

### F: Vad händer om jag glömmer registrera raster?
**S:** Du kan välja mellan:
- **Automatiska raster**: Systemet beräknar lagstadgade raster
- **Manuella raster**: Ange exakt rastlängd
- **Inga raster**: För korta arbetspass under 6 timmar

### F: Hur fungerar automatiska raster?
**S:** Baserat på Arbetsmiljöverkets regler:
- **6-8 timmar**: 30 minuters rast
- **8+ timmar**: 60 minuters rast
- **Under 6 timmar**: Ingen obligatorisk rast

## 💰 Löneberäkningar

### F: Varför stämmer inte min lön med appens beräkning?
**S:** Kontrollera följande:
1. **Avtalsnivå**: Rätt ålder/erfarenhetsnivå vald
2. **Arbetsplatstyp**: "Butik" vs "Lager" har olika OB-satser
3. **Lokala avtal**: Ditt företag kan ha högre löner än grundavtalet
4. **OB-satser**: Kontrollera att tidsperioderna stämmer

### F: Vad är OB-tillägg och när gäller de?
**S:** OB (obekväm arbetstid) är extra ersättning för arbete utanför normaltid:

**Butik:**
- Mån-fre 18:15-20:00: +50%
- Mån-fre efter 20:00: +70%
- Lördag efter 12:00: +100%
- Söndag: +100%
- Helgdagar: +100%

**Lager:**
- Mån-fre 06:00-07:00: +40%
- Mån-fre 18:00-23:00: +40%
- Mån-fre 23:00-06:00: +70%
- Lördag 06:00-23:00: +40%
- Söndag: +100%
- Helgdagar: +100%

### F: Inkluderas semesterersättning?
**S:** Ja, 12% semesterersättning läggs automatiskt till bruttolönen enligt kollektivavtal.

### F: Hur beräknas skatten?
**S:** Appen använder din inställda preliminärskatt (standard 30%) för att beräkna nettolön. Detta är endast en uppskattning.

### F: Vilka avtalsnivåer finns?
**S:** Baserat på Detaljhandelsavtalet 2025-2026:
- **16 år**: 101,48 kr/h
- **17 år**: 103,95 kr/h  
- **18 år**: 155,51 kr/h
- **19 år**: 157,44 kr/h
- **1 års erfarenhet**: 160,95 kr/h
- **2 års erfarenhet**: 162,98 kr/h
- **3+ års erfarenhet**: 165,84 kr/h

## 📊 Funktioner & Användning

### F: Vad är arbetsmallar och hur använder jag dem?
**S:** Mallar sparar återkommande arbetspass:
1. Fyll i en komplett arbetsdag
2. Tryck "Spara som mall"
3. Ge mallen ett namn (ex: "Helgdag Butik")
4. Använd "Använd mall" för att snabbt fylla i liknande dagar

### F: Kan jag se statistik för olika perioder?
**S:** Ja! Statistikskärmen visar:
- **Daglig** sammanfattning
- **Veckovis** uppdelning  
- **Månadsrapporter**
- **Anpassade perioder**

### F: Vilka helgdagar känner appen igen?
**S:** Alla officiella svenska helgdagar:
- Nyårsdagen, Trettondedag jul
- Långfredag, Annandag påsk, Kristi himmelsfärd
- Första maj, Nationaldagen
- Midsommarafton, Alla helgons dag
- Julafton, Juldagen, Nyårsafton

Plus möjlighet att lägga till företagsspecifika helgdagar.

## 💾 Data & Backup

### F: Var lagras mina data?
**S:** All data lagras lokalt på din enhet i appens privata mapp. Ingen data skickas automatiskt till externa servrar.

### F: Kan jag flytta data till en ny telefon?
**S:** Ja, på två sätt:
1. **JSON Export/Import**: Exportera till fil, kopiera till ny enhet
2. **Google Drive**: Automatisk backup och återställning

### F: Hur ofta ska jag göra backup?
**S:** Rekommendationer:
- **Månadsvis**: Manual JSON-export
- **Automatiskt**: Google Drive backup (dagligen/veckovis)
- **Innan uppdateringar**: Alltid göra backup först

### F: Vad händer om jag förlorar min telefon?
**S:** Om du har Google Drive backup aktiverat:
1. Installera appen på ny enhet
2. Gå till Import > "Återställ från Google Drive"
3. Välj senaste backup-fil
4. All data återställs automatiskt

### F: Kan jag exportera data till Excel?
**S:** Ja! Använd CSV-export för att öppna i Excel/Google Sheets:
1. Gå till Export-skärmen
2. Välj "Excel-kompatibel CSV"
3. Öppna filen i valfritt kalkylprogram

## 🔧 Tekniska Problem

### F: Appen kraschar när jag startar den
**S:** Prova följande:
1. **Starta om** enheten
2. **Rensa app-cache**: Inställningar > Appar > Arbetstidskalkylator > Lagring > Rensa cache
3. **Rensa app-data**: Samma som ovan men "Rensa data" (⚠️ raderar all data)
4. **Ominstallera** appen

### F: Kalendern laddar långsamt
**S:** Detta kan hända första gången eller med mycket data:
1. Vänta tills initial laddning är klar
2. Starta om appen om problemet kvarstår
3. Kontrollera att enheten har tillräckligt RAM

### F: Export/Import fungerar inte
**S:** Kontrollera:
1. **Tillräckligt lagringsutrymme** på enheten
2. **Filbehörigheter** - appen behöver åtkomst till lagring
3. **Korrekt filformat** - endast JSON för import
4. **Fil inte skadad** - testa med nyare backup

### F: Google Drive backup fungerar inte
**S:** Troubleshooting:
1. **Internetanslutning** - stabil WiFi eller mobildata
2. **Google-konto** - logga ut och in igen
3. **Drive-utrymme** - kontrollera att det finns plats
4. **App-behörigheter** - tillåt Google Drive åtkomst

## 📈 Avancerade Funktioner

### F: Kan jag anpassa OB-satserna?
**S:** Ja, i Avancerade inställningar kan du:
- Ändra alla OB-satser per kategori
- Lägga till nya tidsperioder
- Skapa företagsspecifika regler

### F: Finns det widgets för hemskärmen?
**S:** Inte i nuvarande version, men det är planerat för v1.2.0.

### F: Kan flera personer använda samma app?
**S:** För närvarande stöds endast en användare per installation. Multi-user support planeras för v1.3.0.

### F: Går det att synkronisera mellan enheter?
**S:** Endast via manuell backup/restore. Automatisk synkronisering mellan enheter planeras för framtida versioner.

## 🔄 Uppdateringar

### F: Hur uppdaterar jag appen?
**S:** 
1. Besök [GitHub Releases](https://github.com/tjelite1986/Arbetstidskalkylator/releases)
2. Ladda ner senaste APK-fil
3. Installera över befintlig version (data bevaras)

### F: Kommer mina data att försvinna vid uppdatering?
**S:** Nej, data bevaras mellan uppdateringar. Men gör alltid backup före större uppdateringar.

### F: Vilken version har jag?
**S:** Kontrollera i appen:
- Gå till Inställningar
- Scrolla ner till "Om appen"
- Versionnummer visas där

## 💼 Juridiskt & Kollektivavtal

### F: Är löneberäkningarna juridiskt bindande?
**S:** Nej, appen är endast ett hjälpmedel. Ditt anställningsavtal och företagets lokala avtal gäller alltid.

### F: Vad händer om kollektivavtalet ändras?
**S:** Appen uppdateras regelbundet med nya avtalssatser. Kontrollera för uppdateringar när nya avtal träder i kraft.

### F: Kan jag använda appen som bevis i lönedisputer?
**S:** Appen kan vara till hjälp för egen dokumentation, men officiella lönebesked från arbetsgivaren gäller juridiskt.

## 🆘 Support & Hjälp

### F: Var hittar jag mer hjälp?
**S:** 
- **📖 [Användarguide](User-Guide)** - Detaljerad manual
- **💻 [Developer Setup](Developer-Setup)** - För tekniska frågor
- **🐛 [GitHub Issues](https://github.com/tjelite1986/Arbetstidskalkylator/issues)** - Rapportera problem
- **💬 [Diskussioner](https://github.com/tjelite1986/Arbetstidskalkylator/discussions)** - Frågor och förslag

### F: Kan jag föreslå nya funktioner?
**S:** Absolut! Använd [Feature Request](https://github.com/tjelite1986/Arbetstidskalkylator/issues/new?template=feature_request.yml) för att föreslå förbättringar.

### F: Hur rapporterar jag en bugg?
**S:** Använd [Bug Report](https://github.com/tjelite1986/Arbetstidskalkylator/issues/new?template=bug_report.yml) och inkludera:
- Android-version
- App-version  
- Detaljerad beskrivning av problemet
- Steg för att återskapa felet

### F: Finns appen på Google Play Store?
**S:** Inte för närvarande. Appen distribueras via GitHub Releases som APK-fil.

### F: Kostar appen något?
**S:** Nej, Arbetstidskalkylator är helt gratis och öppen källkod (MIT-licens).

## 🔮 Framtida Funktioner

### F: Vad kommer i nästa version?
**S:** Planerat för **v1.2.0** (Q2 2025):
- Widget-support för hemskärmen
- Automatisk synkronisering mellan enheter
- Avancerad analytics med trendanalys
- Dark/Light mode themes

### F: Kommer appen att finnas på engelska?
**S:** Multi-språkstöd (engelska, finska, norska) är planerat för framtiden, men inget datum är satt ännu.

### F: Planeras företagsversion?
**S:** Ja, en företagsversion med centraliserad hantering är under övervägande för v1.3.0.

---

## 🔍 Hittade du inte svar på din fråga?

**Kontakta oss:**
- **🐛 [Rapportera Problem](https://github.com/tjelite1986/Arbetstidskalkylator/issues/new?template=bug_report.yml)**
- **💡 [Föreslå Funktioner](https://github.com/tjelite1986/Arbetstidskalkylator/issues/new?template=feature_request.yml)**
- **💬 [Diskussioner](https://github.com/tjelite1986/Arbetstidskalkylator/discussions)**

**Relaterade Guider:**
- **🏠 [Hem](Home)** - Projektöversikt
- **📱 [Installation](Installation)** - Installationsguide  
- **📚 [Användarguide](User-Guide)** - Fullständig manual
- **🔧 [API Documentation](API-Documentation)** - Teknisk dokumentation

---

**💡 Tips:** Lägg till denna sida som bokmärke för snabb åtkomst till vanliga frågor!

*Senast uppdaterad: 2025-01-28*