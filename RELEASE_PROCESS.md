# 🚀 Release Process för Nya Versioner

## 📋 Steg för att skapa ny version och auto-taggning

### 🔄 **Workflow för nästa version (t.ex. v1.2.0-beta):**

```bash
# 1. Skapa ny utvecklingsbranch från main
git checkout main
git pull origin main
git checkout -b v1.2.0-beta

# 2. Gör utvecklingsändringar...
# - Lägg till nya funktioner
# - Fixa buggar
# - Uppdatera kod

# 3. Uppdatera versionsnummer i build.gradle automatiskt
# (sker automatiskt vid build)

# 4. Bygg ny APK
cd TimeReportCalculator
JAVA_HOME=/workspace/jdk-17.0.2 ANDROID_HOME=/workspace/android-sdk /workspace/gradle-8.1/bin/gradle assembleDebug

# 5. Commit alla ändringar
git add .
git commit -m "🚀 v1.2.0-beta: [Beskriv nya funktioner]"

# 6. Push utvecklingsbranch
git push -u origin v1.2.0-beta

# 7. Skapa release med automatisk taggning
gh release create v1.2.0-beta \
  --repo tjelite1986/Tidsregistrering-och-Lon-Kalkylator- \
  --title "🚀 v1.2.0-beta - [Titel för nya funktioner]" \
  --notes "Release notes här..." \
  --target v1.2.0-beta \
  --prerelease \
  "TimeReportCalculator/app/build/outputs/apk/debug/Tidsregistrerings-Kalkylator-v*.apk"

# 8. Ta bort utvecklingsbranch efter release (behåller tag!)
git checkout main
git branch -D v1.2.0-beta
git push origin --delete v1.2.0-beta
```

## 🎯 **Vad detta ger dig:**

### ✅ **Automatisk "Latest Release" taggning:**
- **GitHub märker automatiskt** senaste release som "Latest"
- **README badges** uppdateras automatiskt till ny version
- **Release-sidan** visar alltid senaste version först
- **APK-nedladdning** går alltid till senaste version

### 📱 **APK-filer:**
- **Korrekt filnamn** (Tidsregistrerings-Kalkylator-v1.2.0-beta.apk)
- **Automatiskt versionsnummer** från build.gradle
- **Upload till GitHub** som release asset
- **Permanent nedladdningslänk** även efter branch-borttagning

### 🏷️ **Git Tags:**
- **v1.2.0-beta tag** skapas automatiskt vid release
- **Tags förblir** även när brancher tas bort
- **Möjlighet att checkout** specifik version senare: `git checkout v1.2.0-beta`

## 🔧 **Förenklade kommandon för dig:**

### **Skapa Release Script:**
```bash
#!/bin/bash
VERSION=$1  # t.ex. "v1.2.0-beta"
TITLE=$2    # t.ex. "Ny funktion X och Y"

# Skapa branch
git checkout main && git pull
git checkout -b $VERSION

# Efter utveckling - skapa release
gh release create $VERSION \
  --title "🚀 $VERSION - $TITLE" \
  --generate-notes \
  --prerelease \
  --target $VERSION \
  "TimeReportCalculator/app/build/outputs/apk/debug/Tidsregistrerings-Kalkylator-$VERSION.apk"

# Cleanup
git checkout main
git branch -D $VERSION
git push origin --delete $VERSION
```

## 📊 **GitHub Auto-features:**

### **"Latest Release" Badge:**
- [![Release](https://img.shields.io/github/v/release/tjelite1986/Tidsregistrering-och-Lon-Kalkylator-?include_prereleases&label=Latest%20Release)]
- **Uppdateras automatiskt** till ny version

### **Auto-generated Release Notes:**
- **--generate-notes** skapar automatiska release notes
- **Baserat på commits** sedan senaste release
- **Lista över changes** och contributors

### **Download Stats:**
- **GitHub räknar nedladdningar** automatiskt
- **Statistik per release** och total
- **Visas på release-sidan**

## 🎯 **Resultat för användare:**

1. **Besöker**: https://github.com/tjelite1986/Tidsregistrering-och-Lon-Kalkylator-/releases/latest
2. **Ser**: "v1.2.0-beta" som senaste version
3. **Laddar ner**: Tidsregistrerings-Kalkylator-v1.2.0-beta.apk
4. **README badges**: Visar automatiskt ny version

**Perfect workflow för automatisk versionering! 🚀**