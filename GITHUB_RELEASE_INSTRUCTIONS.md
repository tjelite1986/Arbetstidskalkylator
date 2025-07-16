# 📋 GitHub Release Instructions for v1.1.114

## 🔑 Authentication Setup

Before pushing, you need to authenticate with GitHub. Choose one of these methods:

### Option 1: SSH Key (Recommended)
1. **Copy the SSH public key:**
   ```
   ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAAIOWjemwlFjaIRNaaULAH0Sy+3Xkwt00jutQH1D0dL/wk claude@anthropic.com
   ```

2. **Add to GitHub:**
   - Go to: https://github.com/settings/keys
   - Click "New SSH key"
   - Title: "Claude Code SSH Key"
   - Paste the key above
   - Click "Add SSH key"

### Option 2: Personal Access Token
1. **Create token:**
   - Go to: https://github.com/settings/tokens
   - Click "Generate new token (classic)"
   - Select scopes: `repo`, `write:packages`
   - Copy the token

2. **Configure git:**
   ```bash
   git remote set-url origin https://YOUR_TOKEN@github.com/tjelite1986/Arbetstidskalkylator.git
   ```

## 📤 Push to GitHub

After authentication setup:

```bash
# Push commits and tags
git push origin main --tags
```

## 🚀 Create GitHub Release

### Step 1: Navigate to Releases
- Go to: https://github.com/tjelite1986/Arbetstidskalkylator/releases/new

### Step 2: Fill Release Information
- **Tag version:** `v1.1.114`
- **Release title:** `🚀 v1.1.114 - Stable Release`
- **Description:** Copy content from `RELEASE_NOTES_v1.1.114.md`

### Step 3: Upload APK
- Click "Attach binaries by dropping them here or selecting them"
- Select: `Tidsregistrerings-Kalkylator-1.1.114.apk` (13.4 MB)

### Step 4: Publish
- ✅ Check "Set as the latest release"
- ✅ Check "Create a discussion for this release"
- Click "Publish release"

## 📱 Download Links

After publishing, the APK will be available at:
- **Latest Release:** https://github.com/tjelite1986/Arbetstidskalkylator/releases/latest
- **Direct APK:** https://github.com/tjelite1986/Arbetstidskalkylator/releases/download/v1.1.114/Tidsregistrerings-Kalkylator-1.1.114.apk

## 📝 Update README

Update the README.md with new version information:

```markdown
### 🚀 **v1.1.114** (Senaste)
- **🎯 Första officiella stable release** av Arbetstidskalkylator
- **🎨 Modern UI redesign** för tidrapport-sidan
- **⏰ Klickbara tidsväljare** för alla tidsfält
- **🍽️ Förbättrade rastinställningar** med automatisk och manuell konfiguration
- **📱 Optimerad APK-storlek** (13.4 MB)
- **🔒 Förbättrad säkerhet** och offline-funktion
```

## ✅ Verification

After release, verify:
- [ ] APK downloads correctly
- [ ] Version shows as "1.1.114" (not beta)
- [ ] App installs on Android device
- [ ] All features work as expected

## 📊 Release Analytics

Monitor release adoption:
- **Downloads:** GitHub Insights → Traffic → Downloads
- **Issues:** Watch for any bug reports
- **Discussions:** Engage with user feedback

---

**🎯 Release is ready to go live!**

All files are prepared and tagged. Just need GitHub authentication to push and publish.