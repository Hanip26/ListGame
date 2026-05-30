# 🎮 ListGame — Popular Games Top Up App

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-brightgreen?style=for-the- badge&logo=android&logoColor=white"/>
  <img src="https://img.shields.io/badge/Language-Kotlin-blueviolet?style=for-the-badge&logo=kotlin&logoColor=white"/>
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white"/>
  <img src="https://img.shields.io/badge/Architecture-MVVM-orange?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/DataStore-Preferences-success?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/Min%20SDK-24%20(Android%207.0)-orange?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/Target%20SDK-36-blue?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/Status-Completed-success?style=for-the-badge"/>
</p>
 
<p align="center">
  Aplikasi Android native berbasis <strong>Kotlin</strong> dan <strong>Jetpack Compose</strong> yang menampilkan daftar game populer lengkap dengan fitur authentication, wishlist, top up diamond, promo code, simulasi pembayaran, progress transaksi realtime, dan sistem invoice otomatis.
</p>

---

## 📱 Tentang Aplikasi 

ListGame adalah aplikasi Android Native yang dikembangkan sebagai bagian dari praktikum **Pengembangan Aplikasi Bergerak (PAB)**.

Pada Week 9, aplikasi dikembangkan lebih lanjut dengan sistem authentication dan top up game digital menggunakan pendekatan modern Android Development berbasis MVVM Architecture.

Aplikasi ini memungkinkan pengguna untuk:

1. Login & Register akun
2. Menjelajahi daftar game populer
3. Melakukan pencarian instan
4. Mengurutkan daftar game
5. Menambahkan game ke wishlist
6. Melihat detail game secara lengkap
7. Melakukan top up diamond / currency game
8. Menggunakan promo code diskon
9. Memilih metode pembayaran digital
10. Melihat progress transaksi realtime
11. Menyimpan session login menggunakan DataStore

---

## 🚀 Tech Stack

1. 🟣 Kotlin (100%)
2. ⚡ Jetpack Compose
3. 🤖 Android Native
4. 🎨 Material 3
5. 🧠 MVVM Architecture
6. 💾 DataStore Preferences
7. ✅ Completed Project

---

## ✨ Highlights

1. 🔐 Authentication System (Login & Register)
2. 💾 Session Login menggunakan DataStore
3. 🔍 Pencarian game secara real-time
4. 🔃 Sorting game (A-Z, Z-A, Rating Tertinggi)
5. ❤️ Wishlist / favorit system
6. 📄 Detail game lengkap
7. 💎 Sistem Top Up Diamond
8. 💳 Simulasi pembayaran digital
9. 🎟️ Promo code & discount system
10. 📋 Order confirmation screen
11. ⏳ Payment progress realtime
12. 🧾 Invoice transaksi otomatis
13. 👤 Profile user system
14. ⚡ UI modern berbasis Jetpack Compose
15. 🧭 Custom navigation tanpa Navigation Component

---

## 📁 Struktur Proyek

<pre>
ListGame App
├── app
│   └── src
│       └── main
│           ├── java/com/example/listgame
│           │
│           │   ├── data
│           │   │   ├── AppDataStore.kt
│           │   │   ├── DummyData.kt
│           │   │   └── UserRepository.kt
│           │
│           │   ├── model
│           │   │   ├── AppViewModel.kt
│           │   │   ├── AuthViewModel.kt
│           │   │   ├── Game.kt
│           │   │   └── User.kt
│           │
│           │   ├── navigation
│           │   │   └── Routes.kt
│           │
│           │   ├── ui
│           │   │   ├── screen
│           │   │   │   ├── DashboardScreen.kt
│           │   │   │   ├── GameDetailScreen.kt
│           │   │   │   ├── GameListScreen.kt
│           │   │   │   ├── LoginScreen.kt
│           │   │   │   ├── OrderConfirmationScreen.kt
│           │   │   │   ├── PaymentProgressScreen.kt
│           │   │   │   ├── ProfileScreen.kt
│           │   │   │   ├── RegisterScreen.kt
│           │   │   │   └── TopUpScreen.kt
│           │   │
│           │   │   └── theme
│           │   │       ├── Color.kt
│           │   │       ├── Theme.kt
│           │   │       └── Type.kt
│           │   │
│           │   └── MainActivity.kt
│           │
│           ├── res
│           │   ├── drawable
│           │   ├── mipmap
│           │   ├── values
│           │   ├── xml
│           │   └── ... (resource files)
│           │
│           └── AndroidManifest.xml
│
├── gradle
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
└── README.md
</pre>

---

## ✨ Fitur Utama

| Ikon | Fitur | Deskripsi |
| :---: | :--- | :--- |
| 🔐 | **Authentication System** | Login & Register akun menggunakan validasi user dan penyimpanan session DataStore. |
| 💾 | **Session Persistence** | Login user tetap tersimpan menggunakan DataStore Preferences. |
| 📋 | **Dynamic Game List** | Menampilkan daftar game populer menggunakan `LazyColumn` dengan performa tinggi. |
| 🔍 | **Pencarian Real-Time** | Pencarian game instan secara realtime tanpa reload halaman. |
| 🔃 | **Sorting Game** | Sorting game berdasarkan A-Z, Z-A, dan Rating Tertinggi. |
| ❤️ | **Wishlist System** | Menyimpan game favorit dengan state persistence menggunakan `rememberSaveable`. |
| 📄 | **Detail Game Lengkap** | Menampilkan banner, genre, rating, deskripsi, dan update terbaru game. |
| 💎 | **Top Up Diamond** | Pembelian diamond atau game currency dengan berbagai nominal. |
| 💳 | **Payment Method** | Simulasi pembayaran digital menggunakan QRIS, OVO, DANA, GoPay, dll. |
| 🎟️ | **Promo Code** | Sistem promo code otomatis dengan perhitungan diskon realtime. |
| 📋 | **Order Confirmation** | Ringkasan transaksi sebelum pembayaran dilakukan. |
| ⏳ | **Payment Progress** | Simulasi progress transaksi realtime hingga pembayaran berhasil. |
| 🧾 | **Invoice System** | Pembuatan invoice otomatis setelah transaksi selesai. |
| 👤 | **Profile Screen** | Menampilkan informasi akun pengguna dan logout session. |
| 🧭 | **Custom Navigation** | Sistem navigasi berbasis custom backstack tanpa Navigation Component. |

---

## 🧠 Arsitektur MVVM

Project menggunakan pendekatan modern Android Architecture yaitu:

### MVVM (Model View ViewModel)

#### 📦 Model
Berisi data class dan representasi data aplikasi:
- `Game.kt`
- `User.kt`

#### 🧠 ViewModel
Mengelola state aplikasi dan business logic:
- `AppViewModel.kt`
- `AuthViewModel.kt`

#### 🎨 View
Berisi seluruh tampilan berbasis Jetpack Compose:
- LoginScreen
- RegisterScreen
- DashboardScreen
- GameListScreen
- GameDetailScreen
- TopUpScreen
- OrderConfirmationScreen
- PaymentProgressScreen
- ProfileScreen

---

## 🔐 Authentication System

### Login Screen
Halaman login utama aplikasi:
1. Input username & password
2. Validasi form login
3. Session login otomatis
4. Error handling jika input kosong
5. Redirect menuju dashboard

### Register Screen
Halaman registrasi akun:
1. Input username baru
2. Input password
3. Validasi register
4. Simpan data user
5. Redirect ke login

### 💾 DataStore Preferences
Digunakan untuk:
- Menyimpan session login
- Menyimpan username
- Menyimpan state aplikasi
- Persist data saat aplikasi ditutup

---

## 📋 Dashboard Screen

Halaman utama setelah login:
1. Greeting user
2. Navigasi fitur utama
3. Shortcut menuju game list
4. Modern Compose UI
5. Navigasi profile

---

## 🎮 Game List Screen

Halaman daftar game populer:
1. Menampilkan game menggunakan `LazyColumn`
2. Search realtime
3. Sorting game
4. Wishlist system
5. Navigasi detail game
6. UI card modern

---

## 📄 Game Detail Screen

Halaman detail game:
1. Banner game
2. Nama developer
3. Genre game
4. Rating game
5. Ukuran file
6. Deskripsi lengkap
7. Informasi update terbaru
8. Tombol wishlist
9. Tombol top up
10. Tombol share game

---

## 💎 Top Up Screen

Fitur utama Week 9.

Pengguna dapat melakukan pembelian diamond / currency game.

### 🎯 Input User ID
User memasukkan ID akun game mereka.

### 💎 Pilihan Nominal
Contoh:
- 86 Diamonds
- 172 Diamonds
- 257 Diamonds
- 706 Diamonds
- Weekly Pass

### 💳 Payment Method
Metode pembayaran yang tersedia:
- QRIS
- DANA
- OVO
- GoPay
- ShopeePay
- Bank Transfer
- Virtual Account

### 🎟️ Promo Code
Kode promo otomatis:
| Promo | Diskon |
| :--- | :--- |
| GAME10 | 10% |
| HEMAT15 | 15% |
| TOPUP20 | 20% |
| DISKON5 | 5% |

### 🧮 Kalkulasi Otomatis
Sistem otomatis menghitung:
- Harga awal
- Diskon promo
- Total pembayaran akhir

---

## 📋 Order Confirmation Screen

Halaman ringkasan transaksi:
1. Nama game
2. User ID
3. Nominal top up
4. Metode pembayaran
5. Promo code
6. Total pembayaran
7. Status transaksi

---

## ⏳ Payment Progress Screen

Mensimulasikan transaksi pembayaran realtime.

### Tahapan Progress:
1. CREATED
2. PAYMENT
3. VERIFYING
4. PROCESSING
5. SUCCESS

### Fitur:
- Progress indicator
- Countdown timer
- Invoice otomatis
- Receipt transaksi
- Status pembayaran realtime

---

## 👤 Profile Screen

Halaman informasi user:
1. Username user
2. Session account
3. Logout account
4. Informasi akun

---

## 🧭 Alur Navigasi

Aplikasi menggunakan sistem navigasi custom back stack:

```text
Login Screen
      ↓
Register Screen
      ↓
Dashboard Screen
      ↓
Game List Screen
      ↓
Game Detail Screen
      ↓
Top Up Screen
      ↓
Order Confirmation Screen
      ↓
Payment Progress Screen
```

---

## 🖼️ Tampilan Aplikasi

| Login | Register | Dashboard |
| :---: | :---: | :---: |
| <img width="340" height="700" alt="WhatsApp Image 2026-05-25 at 17 40 50" src="https://github.com/user-attachments/assets/775dab7a-d943-46a1-ade0-7aa643dfb7e2" /> | <img width="324" height="700" alt="WhatsApp Image 2026-05-25 at 17 44 16" src="https://github.com/user-attachments/assets/d6149f77-8158-413e-92b5-eba217305a1e" /> | <img width="304" height="700" alt="WhatsApp Image 2026-05-25 at 17 49 39" src="https://github.com/user-attachments/assets/c5ceba5a-4f28-4b36-9b00-e7594e432614" />


| Game List | Detail Game | Top Up |
| :---: | :---: | :---: |
| <img width="340" height="700" alt="WhatsApp Image 2026-05-25 at 17 47 08" src="https://github.com/user-attachments/assets/160f851e-0b4f-47d5-811b-1a039fbc71bf" /> | <img width="303" height="700" alt="WhatsApp Image 2026-05-25 at 17 53 44" src="https://github.com/user-attachments/assets/ccf75fc0-a18d-4453-9896-6258d33b78ad" /> | <img width="340" height="700" alt="WhatsApp Image 2026-05-25 at 17 58 41" src="https://github.com/user-attachments/assets/2c06c281-2f27-49bf-94d2-fd67ec58bc1a" />


| Confirmation | Payment Progress | Profile |
| :---: | :---: | :---: |
| <img width="340" height="700" alt="WhatsApp Image 2026-05-25 at 18 00 09" src="https://github.com/user-attachments/assets/1f1e272d-107f-47b8-8ec6-4d4bd211d670" /> | <img width="306" height="700" alt="WhatsApp Image 2026-05-25 at 18 03 09" src="https://github.com/user-attachments/assets/794bfd34-2b22-4f62-8c7a-3ecf809e26da" /> | <img width="334" height="700" alt="WhatsApp Image 2026-05-25 at 18 06 36" src="https://github.com/user-attachments/assets/47657589-d7a5-48c9-9cb5-f443e3799e6a" />


---

## 📁 Data: 7 Game Populer

| # | Judul Game | Genre | Rating |
| :---: | :--- | :--- | :---: |
| 1 | Mobile Legends | MOBA | ⭐ 4.6 |
| 2 | Free Fire | Battle Royale | ⭐ 4.3 |
| 3 | Genshin Impact | RPG Open World | ⭐ 4.7 |
| 4 | PUBG Mobile | Shooter | ⭐ 4.4 |
| 5 | Roblox | Sandbox | ⭐ 4.4 |
| 6 | Call of Duty Mobile | FPS | ⭐ 4.5 |
| 7 | Among Us | Casual | ⭐ 4.2 |

---

## 🏗️ Arsitektur & Teknologi

| Komponen | Teknologi |
| :--- | :--- |
| **Bahasa Pemrograman** | Kotlin |
| **Platform** | Android Native |
| **UI Framework** | Jetpack Compose (Material 3) |
| **Architecture** | MVVM |
| **Session Storage** | DataStore Preferences |
| **State Management** | `remember`, `rememberSaveable`, `mutableStateOf` |
| **Daftar Item** | `LazyColumn` & `LazyRow` |
| **Dialog** | `AlertDialog` |
| **Bottom Sheet** | `ModalBottomSheet` |
| **Sharing** | Android Intent |
| **Min SDK** | 24 |
| **Target SDK** | 36 |

---

## 🛠️ Instalasi & Pengembangan

### **Prasyarat**
- Android Studio Hedgehog atau terbaru
- JDK 17+
- Android SDK 24+

### **Langkah-langkah**

1. **Clone Repository**
```bash
git clone https://github.com/Hanip26/ListGame.git
```

2. **Open Project**
```text
File → Open → Pilih Folder Project
```

3. **Sync Gradle**
```text
File → Sync Project with Gradle Files
```

4. **Run Aplikasi**
```text
Klik ▶ Run 'app'
```

---

## 👥 Tim Pengembang

Proyek ini dikembangkan untuk memenuhi tugas Praktikum **Pengembangan Aplikasi Bergerak (PAB)**.

| No | Nama | NIM |
| :---: | :--- | :--- |
| 1 | Hanief Fahrel Wilianto | L0324016 |
| 2 | Muhammad Affan Nur Zhafariza | L0324022 |
| 3 | Muhammad Rafii Setianto | L0324026 |

---

## 🎬 Link Demo

1. Pertemuan 4  
https://youtube.com/shorts/VuOMfvkpf8g

2. Pertemuan 5  
https://youtube.com/shorts/KDzaNaEwlFA

3. Pertemuan 6  
https://youtube.com/shorts/d-tUgYyTO3c

4. Pertemuan 9  
https://youtube.com/shorts/TkFYY_ZmT7k

---

<p align="center">
  <strong>Program Studi Informatika — Fakultas Teknologi Informasi dan Sains Data</strong><br>
  Universitas Sebelas Maret &nbsp;·&nbsp; Mata Kuliah: Pengembangan Aplikasi Bergerak (Week 09)
</p>
