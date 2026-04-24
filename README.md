# 🎮 ListGame — Popular Games Explorer
<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-brightgreen?style=for-the-badge&logo=android&logoColor=white"/>
  <img src="https://img.shields.io/badge/Language-Kotlin-blueviolet?style=for-the-badge&logo=kotlin&logoColor=white"/>
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white"/>
  <img src="https://img.shields.io/badge/Min%20SDK-24%20(Android%207.0)-orange?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/Target%20SDK-36-blue?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/Status-Completed-success?style=for-the-badge"/>
</p>
<p align="center">
  Aplikasi Android native berbasis <strong>Kotlin</strong> dan <strong>Jetpack Compose</strong> yang menampilkan daftar game populer, dilengkapi fitur login personal, pencarian real-time, halaman detail game, dan penanda favorit (wishlist).
</p>

## 📱Tentang Aplikasi
ListGame adalah aplikasi Android Native yang dikembangkan sebagai bagian dari praktikum
Pengembangan Aplikasi Bergerak (PAB).
Aplikasi ini memungkinkan pengguna untuk:
1. Login menggunakan nama
2. Menjelajahi daftar 7 game populer
3. Melakukan pencarian instan
4. Melihat detail game secara lengkap
5. Menyimpan game favorit ke wishlist pribadi

## 🚀 Tech Stack
1. 🟣 Kotlin (100%)
2. ⚡ Jetpack Compose
3. 🤖 Android Native
4. 🎨 Material 3
5. ✅ Completed Project

## ✨ Highlights
1. 🔐 Login dengan username (personalized greeting)
2. 🔍 Pencarian game secara real-time
3. ❤️ Wishlist / favorit system
4. 📄 Detail game lengkap (rating, genre, deskripsi)
5. ⚡ UI modern berbasis Jetpack Compose
6. 🧭 Custom navigation (tanpa Navigation Component)

## 📁 Struktur Proyek
<pre>ListGame App
├── app
│   └── src
│       └── main
│           ├── java/com/example/listgame
│           │   ├── data
│           │   │   └── DummyData.kt
│           │   ├── model
│           │   │   └── Game.kt
│           │   ├── navigation
│           │   │   └── Routes.kt
│           │   ├── ui
│           │   │   ├── screen
│           │   │   │   ├── GameDetailScreen.kt
│           │   │   │   ├── GameListScreen.kt
│           │   │   │   └── LoginScreen.kt
│           │   │   └── theme
│           │   └── MainActivity.kt
│           └── res
│               ├── drawable
│               ├── layout
│               ├── values
│               └── ... (resource files)
└── build.gradle.kts
</pre>

## ✨ Overview
ListGame adalah aplikasi Android berbasis Jetpack Compose yang menampilkan daftar game populer seperti:
1. Mobile Legends
2. Free Fire
3. Genshin Impact
4. PUBG Mobile
dan lainnya

Aplikasi ini bertujuan untuk:
1. Menampilkan informasi game secara ringkas dan detail
2. Mengimplementasikan UI modern berbasis Compose
3. Melatih konsep navigasi dan state management

## ✨ Fitur Utama

| Ikon | Fitur | Deskripsi |
| :---: | :--- | :--- |
| 🔐 | **Login Screen** | Halaman awal interaktif dengan validasi input username, data akan dikirimkan ke halaman utama. |
| 📋 | **Dynamic Game List** | Menampilkan daftar game populer dalam format *Card* modern dengan performa tinggi menggunakan `LazyColumn`. |
| 🔍 | **Pencarian Real-Time** | Fitur pencarian instan (*live filter*) untuk mencari game berdasarkan nama, tanpa membedakan huruf besar/kecil. |
| 📄 | **Detail Game Lengkap** | Menampilkan informasi komprehensif mulai dari *banner*, developer, genre tags (`LazyRow`), rating, hingga deskripsi. |
| ❤️ | **Sistem Wishlist** | Ikon interaktif untuk menandai game sebagai favorit, memanfaatkan `rememberSaveable` agar state bertahan saat layar dirotasi. |
| 🧭 | **Custom Navigation** | Sistem navigasi berbasis *back-stack* (`SnapshotStateList<Route>`) yang aman dan terintegrasi dengan tombol fisik *back* perangkat. |

### Penjelasan
🔐 Login Screen — Titik Masuk Aplikasi
Halaman pertama yang ditemui pengguna saat membuka aplikasi:
1. Input field Nama Pengguna dengan validasi — tidak boleh kosong
2. Pesan error otomatis muncul jika pengguna mencoba masuk tanpa mengisi nama
3. Tombol "Masuk ke Daftar Game" yang membawa data username ke halaman berikutnya
4. Mendemonstrasikan konsep Basic Routing dan Passing Parameter antarlayar

📋 Game List Screen — Daftar Game Populer
Halaman utama setelah login, menampilkan 7 game populer secara interaktif:

1. Sapaan personal — menyambut pengguna dengan nama yang diinput saat login (contoh: "Hallo Kelompok 3, lagi mau cari game apa nih?")
2. Daftar game menggunakan LazyColumn yang efisien dan smooth
3. Setiap item menampilkan thumbnail game, judul, nama developer, dan cuplikan deskripsi
4. Ikon favorit (❤️) pada setiap item untuk menandai/membatalkan wishlist langsung dari daftar
5. Toggle mode Wishlist di TopAppBar — beralih antara tampilan semua game dan game favorit saja
6. Menuju halaman detail saat item game ditekan

🔍 Pencarian Real-time
Fitur pencarian terintegrasi di halaman Game List:

1. OutlinedTextField dengan ikon kaca pembesar di bagian atas daftar
2. Filter berlangsung secara langsung saat mengetik — tanpa perlu menekan tombol apapun
3. Pencarian tidak membedakan huruf besar/kecil (ignoreCase = true)
4. Pesan "Game tidak ditemukan." ditampilkan jika tidak ada hasil yang cocok
5. Dapat dikombinasikan dengan mode Wishlist untuk mencari di dalam daftar favorit

📄 Game Detail Screen — Informasi Lengkap
Halaman detail yang kaya informasi saat pengguna menekan salah satu game:

1. Banner gambar berukuran penuh dengan sudut membulat
2. Judul dan nama developer game
3. Genre tags yang ditampilkan horizontal menggunakan LazyRow
4. Rating bintang (⭐) disertai nilai numerik (contoh: 4.3 / 5.0)
5. Ukuran file ditampilkan dalam Card yang rapi
6. Deskripsi lengkap dengan teks rata kiri-kanan (TextAlign.Justify)
7. Kartu Informasi Update — menampilkan patch/update terbaru game
8. Tombol panah kembali di TopAppBar untuk kembali ke halaman sebelumnya

❤️ Wishlist / Favorit
Sistem penandaan game favorit yang terintegrasi di seluruh aplikasi:

1. Ikon hati (❤️ merah = favorit, 🤍 abu = belum favorit) pada setiap item
2. State favorit dikelola menggunakan rememberSaveable — bertahan saat orientasi layar berubah
3. Toggle mode "Wishlist Saya" di TopAppBar untuk menyaring hanya game favorit
4. Penambahan dan penghapusan dari wishlist bekerja secara instan tanpa reload

## 🧭 Alur Navigasi
Aplikasi menggunakan sistem navigasi custom back stack berbasis SnapshotStateList<Route> dengan arsitektur sebagai berikut:

Login Screen → Game List → Game Detail

flowchart TD
    Start([Buka Aplikasi]) --> Login
    
    Login -- "backStack.add(Route.Home(username))" --> List
    
    List -- "backStack.add(Route.Detail(gameId))" --> Detail
    
    Detail -- "[←] backStack.removeLastOrNull()" --> List

  
1. 🔐 Login Screen
Input username, validasi kosong, kirim data ke Home via Route.Home(username)
2. 📋 Game List
Sapaan personal, search bar, toggle wishlist, LazyColumn 7 game
3. 📄 Game Detail
Informasi lengkap, banner, genre tags, rating, deskripsi, tombol kembali
4. Route (Sealed Class)

| Objek Route | Parameter | Target Screen |
| :--- | :--- | :--- |
| Route.Login | *(Tidak ada)* | LoginScreen() |
| Route.Home | username: String | GameListScreen(username) |
| Route.Detail | gameId: Int | GameDetailScreen(gameId) |

## 🖼️ Tampilan Aplikasi
Terdapat beberapa game online yang terdapat pada play store sering dimainkan oleh kebanyakan orang, Dirancang khusus untuk efisiensi dan estetika.

### 📱 Halaman List Game
Menampilkan daftar game dalam bentuk card dengan desain modern.

<img width="200" height="700" alt="Screenshot_20260416_132943" src="https://github.com/user-attachments/assets/60191082-9f6b-4210-b9e7-11f30784660c" />


---

### 📄 Halaman Detail Game
Menampilkan informasi lengkap dari game yang dipilih.

<img width="200" height="700" alt="Screenshot_20260416_133208" src="https://github.com/user-attachments/assets/36ea5cb4-1eeb-45b5-a289-540b2375c537" />


---

## 📁 Data: 7 Game Populer

Data dikurasi dalam `DummyData.kt` sebagai Kotlin `object` singleton, siap diperluas ke sumber data eksternal.

| # | Judul & Developer | Genre | Rating |
| :---: | :--- | :--- | :---: |
| 1 | **Mobile Legends: Bang Bang**<br>Moonton | MOBA | ⭐ 4.6 |
| 2 | **Free Fire**<br>Garena | Battle Royale | ⭐ 4.3 |
| 3 | **Genshin Impact**<br>HoYoverse | Action RPG | ⭐ 4.7 |
| 4 | **PUBG Mobile**<br>Level Infinite | Tactical Shooter | ⭐ 4.4 |
| 5 | **Roblox**<br>Roblox Corporation | Sandbox | ⭐ 4.4 |
| 6 | **Call of Duty: Mobile**<br>Activision | FPS | ⭐ 4.5 |
| 7 | **Among Us**<br>Innersloth | Deduksi Sosial | ⭐ 4.2 |

## 🏗️ Arsitektur & Teknologi

| Komponen | Teknologi |
| :--- | :--- |
| **Bahasa Pemrograman** | Kotlin |
| **Platform** | Android Native |
| **UI Framework** | Jetpack Compose (Material 3) |
| **Navigasi** | Custom Back Stack (`SnapshotStateList<Route>`) |
| **Daftar Item** | `LazyColumn` & `LazyRow` |
| **State Management** | `remember`, `rememberSaveable`, `mutableStateOf` |
| **Data Provider** | `DummyData` (Kotlin `object` singleton) |
| **Model Data** | Kotlin `data class Game` |
| **Min SDK** | 24 (Android 7.0 Nougat) |
| **Target SDK** | 36 |

### Penjelasan

Aplikasi ini dibangun menggunakan standar Android modern:

- **Language**: Kotlin (100%)
- **UI Framework**: Jetpack Compose
- **IDE**: Android Studio

### 🔧 Komponen Utama:

- **MainActivity.kt**  
  Entry point aplikasi dan pengatur navigasi utama

- **UI Layer (`ui.screen`)**
  - `GameListScreen.kt` → menampilkan list game  
  - `GameDetailScreen.kt` → menampilkan detail game  

- **Data Layer**
  - `Game.kt` → model data game  
  - `DummyData.kt` → sumber data statis  

- **UI Components**
  - `LazyColumn` → menampilkan list  
  - `Card` → tampilan item game  
  - `Scaffold & TopAppBar` → struktur layout  

- **State Management**
  - Menggunakan `remember` & `mutableStateOf`

---
## 🛠️ Instalasi & Pengembangan

**Prasyarat:**
* Android Studio Hedgehog | 2023.1.1 atau versi terbaru.
* JDK 17+.
* SDK Android API 24+ (Nougat) ke atas.

**Langkah-langkah:**
1.  **Clone the Repo**:
    ```bash
    git clone https://github.com/Hanip26/ListGame.git
    ```
2.  **Import Project**: Buka Android Studio, pilih `Open` dan arahkan ke folder hasil clone.
File → Open → Pilih folder ListGame → OK
4.  **Sync Gradle**: Biarkan Android Studio mengunduh dependensi yang diperlukan.
Tunggu proses sync otomatis, atau:
   File → Sync Project with Gradle Files
6.  **Run**: Jalankan di Emulator atau Device fisik melalui tombol `Shift + F10`.
Klik ▶ Run 'app'  (atau Shift + F10)
   Pilih perangkat / emulator yang tersedia

---
## Dependecies
// build.gradle.kts (app)
dependencies {
    // Jetpack Compose BOM
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)

    // AndroidX Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.navigation3:navigation3-ui:1.0.0-alpha04")
    implementation("androidx.lifecycle:lifecycle-viewmodel-navigation3:1.0.0-alpha04")
}

## KELOMPOK 3 Tugas Praktikum Pengembangan Aplikasi Bergerak — Universitas Sebelas Maret 2026
Proyek ini dikembangkan untuk memenuhi tugas Praktikum **Mobile Application Programming (PAB)**. 

TIM PENGEMBANG 🦸🏻‍♂️🦸🏻‍♂️🦸🏻‍♂️
1. Hanief Fahrel Wilianto (L0324016)
2. Muhammad Affan Nur Zhafariza (L0324022)
3. Muhammad Rafii Setianto (L0324026)
   
---

## Link Youtube

1. https://youtube.com/shorts/VuOMfvkpf8g (Pertemuan 4)
2. https://youtube.com/shorts/KDzaNaEwlFA (Pertemuan 5)

---
Program Studi Informatika — Fakultas Teknologi Informasi dan Sains Data
Universitas Sebelas Maret  ·  Mata Kuliah: Pengembangan Aplikasi Bergerak (Week 05)
