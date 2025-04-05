# 🎮 TreasureHunter

2D platform oyunu - LibGDX ve Kotlin ile geliştirilmiştir.

## 🚀 Özellikler

- Modern ECS (Entity Component System) mimarisi
- Box2D fizik motoru entegrasyonu
- Çok katmanlı parallax arka planlar
- Tiled Map Editor ile level tasarımı
- Event-driven mimari
- Profesyonel asset yönetimi

## 🛠️ Teknolojiler & Araçlar

- **Dil:** Kotlin
- **Game Framework:** LibGDX
- **Fizik Motoru:** Box2D
- **Level Editor:** Tiled Map Editor
- **Asset Management:** TexturePacker
- **Build Tool:** Gradle
- **IDE:** Android Studio

## 📋 Gereksinimler

- JDK 8 veya üzeri
- Android Studio / IntelliJ IDEA
- Gradle 7.0 veya üzeri

## 🔧 Kurulum

1. Repoyu klonlayın:
```bash
git clone https://github.com/barmex61/TreasureHunter.git
```

2. Android Studio veya IntelliJ IDEA ile projeyi açın

3. Gradle sync işlemini tamamlayın

4. Projeyi çalıştırın:
   - Desktop versiyonu için: `lwjgl3` konfigürasyonunu seçin
   - Android versiyonu için: `android` konfigürasyonunu seçin

## 🎯 Proje Yapısı

```
TreasureHunter/
├── core/                  # Ana oyun kodları
│   └── src/
│       └── main/
│           └── kotlin/
│               └── com/libgdx/treasurehunter/
│                   ├── ai/         # AI sistemleri
│                   ├── ecs/        # Entity Component System
│                   ├── event/      # Event sistemi
│                   ├── game/       # Oyun mantığı
│                   ├── input/      # Input işlemleri
│                   ├── tiled/      # Tiled Map entegrasyonu
│                   └── utils/      # Yardımcı sınıflar
├── android/               # Android platformu için kodlar
├── lwjgl3/               # Desktop platformu için kodlar
└── assets/               # Oyun assetleri
```

## 🎮 Kontroller

**Not:** Aşağıdaki kontroller şu an için sadece desktop (PC) platformunda geçerlidir.

- **A/D veya ←/→:** Hareket
- **SPACE:** Zıplama
- **1:** Hafif Saldırı
- **2:** Orta Saldırı
- **3:** Güçlü Saldırı
- **ESC:** Pause Menu

## 🔜 Gelecek Özellikler

- [x] Kılıç Dövüş Sistemi
- [ ] Düşman AI sistemi
- [ ] Ses efektleri ve müzik
- [ ] Kaydetme sistemi
- [ ] Yeni level'lar
- [ ] Karakter animasyonları
- [ ] UI/UX geliştirmeleri

## 🤝 Katkıda Bulunma

1. Fork'layın
2. Feature branch oluşturun (`git checkout -b feature/AmazingFeature`)
3. Değişikliklerinizi commit edin (`git commit -m 'Add some AmazingFeature'`)
4. Branch'inizi push edin (`git push origin feature/AmazingFeature`)
5. Pull Request oluşturun

## 📝 Lisans

Bu proje MIT lisansı altında lisanslanmıştır. Detaylar için [LICENSE](LICENSE) dosyasına bakın.

## 📞 İletişim

Fatih - [@barmex61](https://github.com/barmex61)

Proje Linki: [https://github.com/barmex61/TreasureHunter](https://github.com/barmex61/TreasureHunter)
