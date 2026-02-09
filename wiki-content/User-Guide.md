# 📚 Användarguide

Komplett guide för att använda Arbetstidskalkylator effektivt.

## 🏠 Huvudskärm (Hem)

### 📊 Översiktskort
- **Aktuell Period**: Visar vald tidsperiod (vecka/månad)
- **Totala Timmar**: Summa arbetstimmar för perioden
- **Beräknad Lön**: Total lön inklusive OB-tillägg
- **Genomsnittslön**: Lön per timme inklusive tillägg

### 📅 Kalendervy
- **Klicka på datum** för att se dagsinformation
- **Gröna dagar** = Registrerade arbetsdagar
- **Röd cirkel** = Dagens datum
- **Pilar** = Navigera mellan månader

## ➕ Lägga till Arbetsdag

### Grundläggande Registrering

1. **Välj Datum**
   - Klicka på önskat datum i kalendern
   - Eller tryck kalenderikonen för datumväljare

2. **Ange Arbetstider**
   - **Starttid**: När du började arbeta
   - **Sluttid**: När du slutade arbeta
   - Använd klickbara tidsknappar eller skriv direkt

3. **Konfigurera Raster**
   - **Automatisk**: Systemet beräknar lagstadgade raster
   - **Manuell**: Ange exakt rastlängd i minuter
   - **Inga raster**: För korta arbetspass

4. **Välj Arbetsplatstyp**
   - **Butik**: Använder butiks-OB-satser
   - **Lager**: Använder lager-OB-satser

5. **Beskrivning** (Valfritt)
   - Ex: "Lagerarbete", "Kundtjänst", "Inventering"

### 🚀 Använd Arbetsmallar

**Skapa Mall:**
1. Fyll i en komplett arbetsdag
2. Tryck **"Spara som mall"**
3. Ge mallen ett namn (ex: "Helgdag Butik")

**Använda Mall:**
1. Tryck **"Använd mall"** (högst upp i dialogen)
2. Välj önskad mall från listan
3. Justera vid behov och spara

## 📊 Statistik & Rapporter

### 📈 Statistikskärm

**Periodsammanfattning:**
- **Totala arbetstimmar** för vald period
- **Grundlön** (exklusive tillägg)
- **OB-tillägg** uppdelat per typ
- **Bruttolön** (före skatt)
- **Nettolön** (efter preliminärskatt)
- **Semesterersättning** (12% av bruttolön)

**Detaljerad Uppdelning:**
- **Per dag**: Daglig lön och timmar
- **Per vecka**: Veckosammanfattningar
- **Per månad**: Månadsrapporter

### 📊 Grafer och Visualiseringar
- **Timmar per dag**: Stapeldiagram
- **Löneutveckling**: Linjediagram
- **OB-fördelning**: Cirkeldiagram

## ⚙️ Inställningar

### 👤 Personliga Inställningar

**Avtalsnivå:**
- Välj baserat på ålder och erfarenhet
- Påverkar din grundlön per timme

**Skattesats:**
- Ange din preliminärskatt (vanligt 25-35%)
- Används för nettoberäkningar

### 🏢 Arbetsplatsinställningar

**Standard Arbetsplatstyp:**
- Välj "Butik" eller "Lager" som standard
- Kan ändras per arbetsdag

**Automatiska Raster:**
- Aktivera för automatisk rastberäkning
- Följer Arbetsmiljöverkets regler

### 💰 OB-Satser (Avancerat)

**Butik OB-Satser:**
- Måndag-Fredag 18:15-20:00: +50%
- Måndag-Fredag efter 20:00: +70%
- Lördag efter 12:00: +100%
- Söndag hela dagen: +100%
- Helgdagar: +100%

**Lager OB-Satser:**
- Måndag-Fredag 06:00-07:00: +40%
- Måndag-Fredag 18:00-23:00: +40%
- Måndag-Fredag 23:00-06:00: +70%
- Lördag 06:00-23:00: +40%
- Lördag nätter: +70%
- Söndag: +100%
- Helgdagar: +100%

### 📅 Helgdagar

**Automatiska Svenska Helgdagar:**
- Nyårsdagen, Trettondedag jul
- Långfredag, Annandag påsk
- Första maj, Kristi himmelsfärd
- Nationaldagen, Midsommarafton
- Alla helgons dag, Julafton, Juldagen, Nyårsafton

**Företagsspecifika Helgdagar:**
- Lägg till extra helgdagar för ditt företag
- Ex: Extra ledig dag, företagsspecifika dagar

## 💾 Data & Backup

### 📤 Export Funktioner

**JSON Export:**
- Fullständig backup av all data
- Kan importeras till ny installation
- Bevarar alla inställningar och historik

**Excel-kompatibel CSV:**
- Importera till Excel/Google Sheets
- Färdiga kolumner för analys
- Månadsrapporter och sammanfattningar

### ☁️ Google Drive Backup

**Konfigurera Backup:**
1. Gå till **Export** > **Backup Inställningar**
2. Tryck **"Anslut Google Drive"**
3. Logga in med ditt Google-konto
4. Välj **backup-frekvens** (dagligen/veckovis)

**Återställa från Backup:**
1. Installera appen på ny enhet
2. Gå till **Import** funktionen
3. Välj **"Återställ från Google Drive"**
4. Välj önskad backup-fil

### 📱 Lokal Datahantering

**Importera Data:**
- Välj JSON-fil från enhetens lagring
- Bekräfta import (befintlig data ersätts)
- Kontrollera att data importerats korrekt

**Rensa Data:**
- **Rensa period**: Ta bort data för viss tidsperiod
- **Rensa allt**: Återställ appen till grundinställningar
- **Varning**: Denna åtgärd kan inte ångras

## 🔧 Tips & Tricks

### ⚡ Effektiv Tidsregistrering

1. **Använd mallar** för återkommande arbetspass
2. **Registrera samma dag** du arbetar
3. **Aktivera påminnelser** för att inte glömma
4. **Dubbelkolla OB-tider** för maximala tillägg

### 📊 Optimera Löneberäkningar

1. **Kontrollera avtalsnivå** regelbundet
2. **Använd rätt arbetsplatstyp** för korrekta OB-satser
3. **Inkludera alla arbetstimmar** även korta pass
4. **Uppdatera skattesats** vid förändring

### 🔄 Rutiner för Backup

1. **Exportera månadsvis** som säkerhet
2. **Testa återställning** någon gång per år
3. **Spara flera kopior** på olika platser
4. **Dokumentera lösenord** för Google Drive

## ❓ Vanliga Frågor (FAQ)

### 🕐 Tidsregistrering

**F: Kan jag ändra en redan registrerad dag?**
S: Ja, klicka på dagen i kalendern och välj "Redigera".

**F: Vad händer om jag glömmer registrera en dag?**
S: Du kan lägga till dagar retroaktivt. Välj datum och fyll i informationen.

**F: Hur hanteras arbetstider över midnatt?**
S: Ange sluttid nästa dag (ex: start 22:00, slut 06:00 +1 dag).

### 💰 Löneberäkningar

**F: Varför stämmer inte min lön med beräkningen?**  
S: Kontrollera avtalsnivå, arbetsplatstyp och OB-satser. Olika företag kan ha lokala avtal.

**F: Inkluderas semesterersättning i månadsberäkningen?**
S: Ja, 12% semesterersättning läggs till bruttolönen enligt kollektivavtal.

**F: Hur beräknas skatt?**
S: Preliminärskatt dras från bruttolön baserat på din inställda skattesats.

### 📱 Tekniskt

**F: Kan jag använda appen offline?**
S: Ja, all grundfunktionalitet fungerar utan internetanslutning.

**F: Synkroniseras data mellan enheter?**
S: Endast via manuell backup/restore till Google Drive.

**F: Är mina data säkra?**
S: Ja, all data lagras lokalt på din enhet. Ingen data skickas utan din tillåtelse.

## 📞 Support & Hjälp

**Behöver du hjälp?**

- **🐛 [Rapportera Problem](https://github.com/tjelite1986/Arbetstidskalkylator/issues/new?template=bug_report.yml)**
- **💡 [Föreslå Förbättringar](https://github.com/tjelite1986/Arbetstidskalkylator/issues/new?template=feature_request.yml)**
- **💬 [Diskussioner & Frågor](https://github.com/tjelite1986/Arbetstidskalkylator/discussions)**

**Relaterade Guider:**
- **🏠 [Hem](Home)** - Projektöversikt
- **📱 [Installation](Installation)** - Installationsguide
- **💻 [Developer Setup](Developer-Setup)** - För utvecklare

---

**🎯 Du är nu expert på Arbetstidskalkylator!**

*För teknisk dokumentation, se [API Documentation](API-Documentation)*