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
  Aplikasi Android native berbasis <strong>Kotlin</strong> dan <strong>Jetpack Compose</strong> yang menampilkan daftar game populer, dilengkapi fitur login personal, pencarian real-time, halaman detail game, penanda favorit (wishlist), berbagi game, dan cek spesifikasi perangkat.
</p>

---

## 📱 Tentang Aplikasi

ListGame adalah aplikasi Android Native yang dikembangkan sebagai bagian dari praktikum **Pengembangan Aplikasi Bergerak (PAB)**.

Aplikasi ini memungkinkan pengguna untuk:
1. Login menggunakan nama
2. Menjelajahi daftar 7 game populer
3. Melakukan pencarian instan
4. Mengurutkan daftar game (A-Z, Z-A, Rating Tertinggi)
5. Melihat detail game secara lengkap
6. Menyimpan game favorit ke wishlist pribadi
7. Membagikan informasi game via WhatsApp atau aplikasi lain
8. Mengecek spesifikasi perangkat minimum yang dibutuhkan

---

## 🚀 Tech Stack

1. 🟣 Kotlin (100%)
2. ⚡ Jetpack Compose
3. 🤖 Android Native
4. 🎨 Material 3
5. ✅ Completed Project

---

## ✨ Highlights

1. 🔐 Login dengan username (personalized greeting)
2. 🔍 Pencarian game secara real-time
3. 🔃 Sorting game (A-Z, Z-A, Rating Tertinggi)
4. ❤️ Wishlist / favorit system dengan konfirmasi hapus
5. 📄 Detail game lengkap (rating, genre, deskripsi, update terbaru)
6. 📤 Fitur Bagikan Game via Modal Bottom Sheet (WhatsApp, salin tautan, app lain)
7. 📋 Cek Spesifikasi Perangkat via Alert Dialog
8. 🚪 Logout dengan konfirmasi via Alert Dialog
9. ⚡ UI modern berbasis Jetpack Compose
10. 🧭 Custom navigation (tanpa Navigation Component)

---

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

---

## ✨ Fitur Utama

| Ikon | Fitur | Deskripsi |
| :---: | :--- | :--- |
| 🔐 | **Login Screen** | Halaman awal interaktif dengan validasi input username, data akan dikirimkan ke halaman utama. |
| 📋 | **Dynamic Game List** | Menampilkan daftar game populer dalam format *Card* modern dengan performa tinggi menggunakan `LazyColumn`. |
| 🔍 | **Pencarian Real-Time** | Fitur pencarian instan (*live filter*) untuk mencari game berdasarkan nama, tanpa membedakan huruf besar/kecil. |
| 🔃 | **Sorting Game** | Pengurutan daftar game berdasarkan A-Z, Z-A, atau Rating Tertinggi melalui komponen Alert Dialog. |
| 📄 | **Detail Game Lengkap** | Menampilkan informasi komprehensif mulai dari *banner*, developer, genre tags (`LazyRow`), rating, hingga deskripsi dan info update terbaru. |
| ❤️ | **Sistem Wishlist** | Ikon interaktif untuk menandai game sebagai favorit, memanfaatkan `rememberSaveable` agar state bertahan saat layar dirotasi, dilengkapi konfirmasi hapus. |
| 📤 | **Bagikan Game** | Fitur berbagi informasi game melalui Modal Bottom Sheet dengan pilihan: Salin Tautan, Bagikan via WhatsApp, atau Pilihan Aplikasi Lainnya. |
| 📋 | **Cek Spesifikasi** | Alert Dialog yang menampilkan spesifikasi minimum dan rekomendasi perangkat untuk memainkan game yang dipilih. |
| 🚪 | **Logout** | Konfirmasi keluar dari aplikasi melalui Alert Dialog untuk kembali ke halaman login. |
| 🧭 | **Custom Navigation** | Sistem navigasi berbasis *back-stack* (`SnapshotStateList<Route>`) yang aman dan terintegrasi dengan tombol fisik *back* perangkat. |

---

### Penjelasan Fitur

#### 🔐 Login Screen — Titik Masuk Aplikasi
Halaman pertama yang ditemui pengguna saat membuka aplikasi:
1. Input field Nama Pengguna dengan validasi — tidak boleh kosong
2. Pesan error otomatis muncul jika pengguna mencoba masuk tanpa mengisi nama
3. Tombol "Masuk ke Daftar Game" yang membawa data username ke halaman berikutnya
4. Mendemonstrasikan konsep Basic Routing dan Passing Parameter antarlayar

#### 📋 Game List Screen — Daftar Game Populer
Halaman utama setelah login, menampilkan 7 game populer secara interaktif:
1. Sapaan personal — menyambut pengguna dengan nama yang diinput saat login (contoh: *"Hallo Kelompok 3, cari game apa?"*)
2. Daftar game menggunakan `LazyColumn` yang efisien dan smooth
3. Setiap item menampilkan thumbnail game, judul, nama developer, dan cuplikan deskripsi
4. Ikon favorit (❤️) pada setiap item untuk menandai/membatalkan wishlist langsung dari daftar
5. Fitur Sorting (A-Z, Z-A, Rating Tertinggi) via Alert Dialog
6. Toggle mode Wishlist di TopAppBar — beralih antara tampilan semua game dan game favorit saja
7. Tombol Hapus Semua Favorit dan tombol Logout di TopAppBar
8. Menuju halaman detail saat item game ditekan

#### 🔍 Pencarian Real-time
Fitur pencarian terintegrasi di halaman Game List:
1. `OutlinedTextField` dengan ikon kaca pembesar di bagian atas daftar
2. Filter berlangsung secara langsung saat mengetik — tanpa perlu menekan tombol apapun
3. Pencarian tidak membedakan huruf besar/kecil (`ignoreCase = true`)
4. Pesan *"Tidak ditemukan."* ditampilkan jika tidak ada hasil yang cocok
5. Dapat dikombinasikan dengan mode Wishlist untuk mencari di dalam daftar favorit

#### 📄 Game Detail Screen — Informasi Lengkap
Halaman detail yang kaya informasi saat pengguna menekan salah satu game:
1. Banner gambar berukuran penuh dengan sudut membulat
2. Judul dan nama developer game
3. Genre tags yang ditampilkan horizontal menggunakan `LazyRow`
4. Rating bintang (⭐) disertai nilai numerik (contoh: 4.3 / 5.0)
5. Ukuran file ditampilkan dalam Card yang rapi
6. Deskripsi lengkap dengan teks rata kiri-kanan (`TextAlign.Justify`)
7. Kartu Informasi Update — menampilkan patch/update terbaru game
8. Tombol **Bagikan Game** yang membuka Modal Bottom Sheet
9. Tombol **Cek Spesifikasi Perangkat** yang membuka Alert Dialog spesifikasi sistem
10. Tombol panah kembali di TopAppBar untuk kembali ke halaman sebelumnya

#### ❤️ Wishlist / Favorit
Sistem penandaan game favorit yang terintegrasi di seluruh aplikasi:
1. Ikon hati (❤️ merah = favorit, 🤍 abu = belum favorit) pada setiap item
2. State favorit dikelola menggunakan `rememberSaveable` — bertahan saat orientasi layar berubah
3. Konfirmasi Alert Dialog saat menghapus satu item dari favorit
4. Toggle mode *"Wishlist Saya"* di TopAppBar untuk menyaring hanya game favorit
5. Tombol hapus semua favorit dengan konfirmasi Alert Dialog
6. Penambahan dan penghapusan dari wishlist bekerja secara instan tanpa reload

#### 📤 Bagikan Game (Modal Bottom Sheet)
Fitur berbagi informasi game melalui panel bawah layar:
1. Muncul dari bagian bawah layar saat tombol "Bagikan Game" ditekan
2. **Salin Tautan Game** — menyalin teks ke clipboard dengan notifikasi Toast
3. **Bagikan via WhatsApp** — mengirim informasi game langsung ke WhatsApp
4. **Pilihan Aplikasi Lainnya** — membuka app chooser sistem Android

#### 📋 Cek Spesifikasi Perangkat (Alert Dialog)
Dialog informasi spesifikasi sistem untuk memainkan game:
- **Minimum:** Android 8.0+, RAM 3 GB, penyimpanan sesuai ukuran game
- **Rekomendasi:** Android 11+, RAM 6 GB+, koneksi internet stabil

#### 🚪 Logout (Alert Dialog)
Konfirmasi keluar dari aplikasi:
- Menampilkan dialog konfirmasi "Yakin ingin kembali ke login?"
- Pilihan **Ya** akan membersihkan backstack dan kembali ke Login Screen
- Pilihan **Batal** untuk tetap di dalam aplikasi

---

## 🧭 Alur Navigasi

Aplikasi menggunakan sistem navigasi custom back stack berbasis `SnapshotStateList<Route>`:

```
Login Screen → Game List → Game Detail
```

```
flowchart TD
    Start([Buka Aplikasi]) --> Login

    Login -- "backStack.add(Route.Home(username))" --> List

    List -- "backStack.add(Route.Detail(gameId))" --> Detail

    Detail -- "[←] backStack.removeLastOrNull()" --> List

    List -- "[Logout] backStack.clear() + add(Route.Login)" --> Login
```

| Objek Route | Parameter | Target Screen |
| :--- | :--- | :--- |
| `Route.Login` | *(Tidak ada)* | `LoginScreen()` |
| `Route.Home` | `username: String` | `GameListScreen(username)` |
| `Route.Detail` | `gameId: Int` | `GameDetailScreen(gameId)` |

---

## 🖼️ Tampilan Aplikasi

| Login | Lazy List | Detail Game | Bottom Sheet | Alert Dialog | Alert Dialog |
| :---: | :---: | :---: | :---: | :---: | :---: |
 |<img width="295" height="750" alt="image" src="https://github.com/user-attachments/assets/65c57850-c0da-430c-937a-281b88e5ed6c" /> | <img width="295" height="750" alt="image" src="https://github.com/user-attachments/assets/7a3e07c0-862a-4fbb-a308-265ccc6a081a" /> | <img width="295" height="750" alt="image" src="https://github.com/user-attachments/assets/5426b31b-16ec-4ec1-b5db-40e63edde377" /> | <img width="295" height="740" alt="image" src="https://github.com/user-attachments/assets/4f1275e5-1abc-429c-9774-5c3abab00414" /> | <img width="295" height="740" alt="image" src="https://github.com/user-attachments/assets/8fdcd17f-9e78-4a10-8805-a3569ae0d261" /> |<img width="295" height="740" alt="image" src="https://github.com/user-attachments/assets/9358de14-f664-45bb-a42e-59bb9753b465" />

---

## 📁 Data: 7 Game Populer

Data dikurasi dalam `DummyData.kt` sebagai Kotlin `object` singleton, siap diperluas ke sumber data eksternal.

| # | Judul & Developer | Genre | Rating | Ukuran |
| :---: | :--- | :--- | :---: | :---: |
| 1 | **Mobile Legends: Bang Bang**<br>Moonton | MOBA, Multiplayer, Strategi | ⭐ 4.6 | 1.5 GB |
| 2 | **Free Fire**<br>Garena | Battle Royale, Survival Shooter | ⭐ 4.3 | 1.2 GB |
| 3 | **Genshin Impact**<br>HoYoverse | Action RPG, Open World | ⭐ 4.7 | 20.5 GB |
| 4 | **PUBG Mobile**<br>Level Infinite | Tactical Shooter, Realistis | ⭐ 4.4 | 2.8 GB |
| 5 | **Roblox**<br>Roblox Corporation | Sandbox, MMO, Kreatif | ⭐ 4.4 | 180 MB |
| 6 | **Call of Duty: Mobile**<br>Activision | FPS, Tembak-menembak | ⭐ 4.5 | 2.4 GB |
| 7 | **Among Us**<br>Innersloth | Deduksi Sosial, Kasual | ⭐ 4.2 | 250 MB |

---

## 🏗️ Arsitektur & Teknologi

| Komponen | Teknologi |
| :--- | :--- |
| **Bahasa Pemrograman** | Kotlin |
| **Platform** | Android Native |
| **UI Framework** | Jetpack Compose (Material 3) |
| **Navigasi** | Custom Back Stack (`SnapshotStateList<Route>`) |
| **Daftar Item** | `LazyColumn` & `LazyRow` |
| **State Management** | `remember`, `rememberSaveable`, `mutableStateOf` |
| **Dialog** | `AlertDialog` (logout, sort, hapus favorit, spesifikasi) |
| **Bottom Sheet** | `ModalBottomSheet` (berbagi game) |
| **Sharing** | Android `Intent` (WhatsApp, app chooser, clipboard) |
| **Data Provider** | `DummyData` (Kotlin `object` singleton) |
| **Model Data** | Kotlin `data class Game` |
| **Min SDK** | 24 (Android 7.0 Nougat) |
| **Target SDK** | 36 |

### 🔧 Komponen Utama

- **`MainActivity.kt`** — Entry point aplikasi, pengatur navigasi utama, dan pengelola state global (favorit, sorting)
- **`LoginScreen.kt`** — Halaman login dengan validasi username dan navigasi ke Home
- **`GameListScreen.kt`** — Menampilkan list game dengan search, sort, wishlist, logout
- **`GameDetailScreen.kt`** — Menampilkan detail game, Lazy List genre, Alert Dialog spesifikasi, dan Modal Bottom Sheet berbagi
- **`Game.kt`** — Model data game (`data class`)
- **`DummyData.kt`** — Sumber data statis berupa 7 game populer
- **`Routes.kt`** — Sealed class navigasi dengan `LocalBackStack` via `CompositionLocalProvider`

### State Management
- `remember` & `mutableStateOf` — untuk state lokal UI (dialog, input)
- `rememberSaveable` — untuk state yang bertahan saat rotasi layar (favorit, sorting, search query)
- `SnapshotStateList<Route>` — untuk manajemen back stack navigasi

---

## 🛠️ Instalasi & Pengembangan

**Prasyarat:**
- Android Studio Hedgehog | 2023.1.1 atau versi terbaru
- JDK 17+
- SDK Android API 24+ (Nougat) ke atas

**Langkah-langkah:**

1. **Clone the Repo:**
   ```bash
   git clone https://github.com/Hanip26/ListGame.git
   ```

2. **Import Project:**
   Buka Android Studio → `File → Open` → arahkan ke folder hasil clone → OK

3. **Sync Gradle:**
   Biarkan Android Studio mengunduh dependensi yang diperlukan secara otomatis, atau:
   ```
   File → Sync Project with Gradle Files
   ```

4. **Run:**
   Klik ▶ **Run 'app'** atau tekan `Shift + F10`, lalu pilih perangkat / emulator yang tersedia.

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

| Pertemuan | Link |
| :---: | :--- | :---: |
| Pertemuan 4 | https://youtube.com/shorts/VuOMfvkpf8g |
| Pertemuan 5 | https://youtube.com/shorts/KDzaNaEwlFA |
| Pertemuan 6 | https://youtube.com/shorts/d-tUgYyTO3c |                                       |

---

<p align="center">
  <strong>Program Studi Informatika — Fakultas Teknologi Informasi dan Sains Data</strong><br>
  Universitas Sebelas Maret &nbsp;·&nbsp; Mata Kuliah: Pengembangan Aplikasi Bergerak (Week 04, 05 & 06)
</p>
