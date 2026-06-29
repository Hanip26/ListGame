# 🎮 ListGame — Popular Games Top Up App
 
<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-brightgreen?style=for-the- badge&logo=android&logoColor=white"/>
  <img src="https://img.shields.io/badge/Language-Kotlin-blueviolet?style=for-the-badge&logo=kotlin&logoColor=white"/>
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white"/>
  <img src="https://img.shields.io/badge/Architecture-MVVM-orange?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/Network-Retrofit2-brightgreen?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/DI-Hilt-red?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/Min%20SDK-24%20(Android%207.0)-orange?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/Target%20SDK-36-blue?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/Status-Completed-success?style=for-the-badge"/> 
</p>
 
<p align="center">
  Aplikasi Android native berbasis <strong>Kotlin</strong> dan <strong>Jetpack Compose</strong> yang menampilkan daftar game populer lengkap dengan fitur authentication, wishlist, top up diamond, promo code, simulasi pembayaran, progress transaksi realtime, sistem invoice otomatis, dan integrasi API backend menggunakan <strong>Retrofit2</strong>.
</p>

---

## 📱 Tentang Aplikasi 

ListGame adalah aplikasi Android Native yang dikembangkan sebagai bagian dari praktikum **Pengembangan Aplikasi Bergerak (PAB)**.

Pada Week 11, aplikasi dikembangkan lebih lanjut dengan integrasi HTTP Request menggunakan Retrofit2 untuk berkomunikasi dengan backend API secara langsung, menggantikan penggunaan dummy data lokal.

Aplikasi ini memungkinkan pengguna untuk:

1. Login & Register akun melalui API backend
2. Menjelajahi daftar game populer dari API
3. Melakukan pencarian instan
4. Mengurutkan daftar game
5. Menambahkan game ke wishlist (tersimpan di backend)
6. Melihat detail game secara lengkap
7. Melakukan top up diamond / currency game
8. Menggunakan promo code diskon
9. Memilih metode pembayaran digital
10. Melihat progress transaksi realtime
11. Menyimpan session login menggunakan DataStore
12. Mengelola saldo & riwayat Nexus Coin
13. Mengecek transaksi berdasarkan Invoice ID
14. Menghitung Win Rate game
15. Reset password akun

---

# 📚 Implementasi Materi Perkuliahan PPAB

Pengembangan aplikasi **ListGame** dilakukan secara bertahap sesuai dengan materi yang dipelajari pada Praktikum Pengembangan Aplikasi Bergerak (PPAB). Setiap pertemuan memberikan kontribusi terhadap fitur dan teknologi yang digunakan dalam aplikasi ini.

| Pertemuan | Materi | Fungsi Materi | Implementasi pada Aplikasi |
|----------|---------|---------|---------|
| **Week 01** | Pengenalan Android Studio & Kotlin | Memahami lingkungan pengembangan Android dan dasar bahasa Kotlin. | Menjadi fondasi dalam pembangunan aplikasi Android Native menggunakan Kotlin. |
| **Week 02** | Dasar Pemrograman Kotlin | Mempelajari variabel, fungsi, percabangan, perulangan, dan collection. | Digunakan pada logika aplikasi seperti validasi login, filtering game, sorting game, dan perhitungan promo top up. |
| **Week 03** | Object Oriented Programming (OOP) | Memahami konsep class, object, encapsulation, inheritance, dan abstraction. | Digunakan pada model data seperti `Game.kt`, `User.kt`, ViewModel, Repository, dan data class API response. |
| **Week 04** | Pembuatan User Interface Android | Mempelajari komponen UI dan penyusunan tampilan aplikasi. | Digunakan pada Login Screen, Register Screen, Dashboard Screen, dan komponen antarmuka lainnya. |
| **Week 05** | Layout dan Interaksi Pengguna | Mempelajari event handling dan interaksi pengguna dengan aplikasi. | Implementasi form login, register, tombol navigasi, validasi input, dan interaksi pengguna. |
| **Week 06** | Jetpack Compose | Mempelajari framework UI modern Android berbasis declarative programming. | Seluruh tampilan aplikasi dibangun menggunakan Jetpack Compose dan Material 3. |
| **Week 07** | State Management & Navigation | Memahami pengelolaan state dan perpindahan halaman aplikasi. | Digunakan pada sistem navigasi antar halaman, wishlist, dan pengelolaan state aplikasi. |
| **Week 09** | Authentication & Data Persistence | Mempelajari autentikasi pengguna dan penyimpanan data lokal. | Implementasi Login, Register, Logout, Session Login, dan DataStore Preferences. |
| **Week 10** | Arsitektur Modern Android (MVVM) | Memahami pemisahan tanggung jawab antara UI dan Business Logic. | Implementasi MVVM Architecture menggunakan ViewModel, Repository, dan DataStore. |
| **Week 11** | HTTP Request & Retrofit | Mempelajari komunikasi jaringan antara aplikasi Android dan backend API menggunakan Retrofit2. | Implementasi `RetrofitClient`, `ApiService`, Repository network layer, dan integrasi seluruh fitur dengan REST API backend (games, packages, transactions, favorites, profile, Nexus Coin). |

### 🎯 Capaian Pembelajaran

Melalui pengembangan aplikasi **ListGame**, berbagai konsep yang dipelajari selama perkuliahan berhasil diimplementasikan secara nyata, meliputi:

- ✅ Kotlin Programming
- ✅ Android Native Development
- ✅ Object Oriented Programming (OOP)
- ✅ Jetpack Compose
- ✅ Material Design 3
- ✅ State Management
- ✅ Navigation System
- ✅ MVVM Architecture
- ✅ Repository Pattern
- ✅ Authentication System
- ✅ Data Persistence menggunakan DataStore
- ✅ Dynamic List menggunakan LazyColumn
- ✅ Wishlist Management
- ✅ Top Up Transaction System
- ✅ Promo Code & Discount Calculation
- ✅ HTTP Request menggunakan Retrofit2
- ✅ REST API Integration
- ✅ Dependency Injection menggunakan Hilt
- ✅ Modern Android Development Practices

---

## 🚀 Tech Stack

1. 🟣 Kotlin (100%)
2. ⚡ Jetpack Compose
3. 🤖 Android Native
4. 🎨 Material 3
5. 🧠 MVVM Architecture
6. 💾 DataStore Preferences
7. 🌐 Retrofit2 + OkHttp3
8. 🔷 Hilt (Dependency Injection)
9. ✅ Completed Project

---

## ✨ Highlights

1. 🔐 Authentication System (Login & Register via API)
2. 💾 Session Login menggunakan DataStore
3. 🌐 HTTP Request menggunakan Retrofit2
4. 🔍 Pencarian game secara real-time
5. 🔃 Sorting game (A-Z, Z-A, Rating Tertinggi)
6. ❤️ Wishlist / favorit system (tersimpan di backend)
7. 📄 Detail game lengkap dari API
8. 💎 Sistem Top Up Diamond
9. 💳 Simulasi pembayaran digital
10. 🎟️ Promo code & discount system
11. 📋 Order confirmation screen
12. ⏳ Payment progress realtime
13. 🧾 Invoice transaksi otomatis
14. 🪙 Nexus Coin (saldo, top up, redeem, riwayat)
15. 🔎 Cek transaksi by Invoice ID
16. 🧮 Kalkulator Win Rate
17. 🔑 Forgot & Reset Password
18. 👤 Profile user system (lihat & edit profil)
19. ⚡ UI modern berbasis Jetpack Compose
20. 🧭 Custom navigation tanpa Navigation Component
21. 💉 Dependency Injection menggunakan Hilt

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
│           │   │   ├── TransactionRepository.kt
│           │   │   └── UserRepository.kt
│           │
│           │   ├── di
│           │   │   └── AppModule.kt
│           │
│           │   ├── model
│           │   │   ├── api
│           │   │   │   ├── ApiGame.kt
│           │   │   │   ├── ApiPackage.kt
│           │   │   │   ├── ApiUser.kt
│           │   │   │   ├── Favorite.kt
│           │   │   │   ├── GamesResponse.kt
│           │   │   │   ├── HistoryResponse.kt
│           │   │   │   ├── LoginRequest.kt
│           │   │   │   ├── LoginResponse.kt
│           │   │   │   ├── NexusCoinApiModels.kt
│           │   │   │   ├── PackageResponse.kt
│           │   │   │   ├── ProfileApiModels.kt
│           │   │   │   ├── RegisterRequest.kt
│           │   │   │   ├── RegisterResponse.kt
│           │   │   │   ├── TransactionDetailResponse.kt
│           │   │   │   └── TransactionHistory.kt
│           │   │   │
│           │   │   ├── AppViewModel.kt
│           │   │   ├── AuthViewModel.kt
│           │   │   ├── Game.kt
│           │   │   ├── GameApiViewModel.kt
│           │   │   ├── HistoryViewModel.kt
│           │   │   ├── NexusCoinTransaction.kt
│           │   │   ├── PackageViewModel.kt
│           │   │   ├── Transaction.kt
│           │   │   ├── TransactionViewModel.kt
│           │   │   └── User.kt
│           │
│           │   ├── navigation
│           │   │   └── Routes.kt
│           │
│           │   ├── network
│           │   │   ├── ApiService.kt
│           │   │   ├── FavoriteRepository.kt
│           │   │   ├── GameRepository.kt
│           │   │   ├── HistoryRepository.kt
│           │   │   ├── NexusCoinRepository.kt
│           │   │   ├── PackageRepository.kt
│           │   │   ├── RetrofitClient.kt
│           │   │   ├── TransactionRequest.kt
│           │   │   └── TransactionResponse.kt
│           │
│           │   ├── ui
│           │   │   ├── components
│           │   │   │   └── BottomNavBar.kt
│           │   │   │
│           │   │   ├── screen
│           │   │   │   ├── CekTransaksiScreen.kt
│           │   │   │   ├── DashboardScreen.kt
│           │   │   │   ├── ForgotPasswordScreen.kt
│           │   │   │   ├── GameDetailScreen.kt
│           │   │   │   ├── GameListScreen.kt
│           │   │   │   ├── KalkulatorWinRateScreen.kt
│           │   │   │   ├── LoginScreen.kt
│           │   │   │   ├── NexusCoinHistoryScreen.kt
│           │   │   │   ├── NexusCoinRedeemScreen.kt
│           │   │   │   ├── NexusCoinTopUpScreen.kt
│           │   │   │   ├── OrderConfirmationScreen.kt
│           │   │   │   ├── PaymentProgressScreen.kt
│           │   │   │   ├── ProfileScreen.kt
│           │   │   │   ├── RegisterScreen.kt
│           │   │   │   └── TopUpScreen.kt
│           │   │
│           │   └── theme
│           │       ├── Color.kt
│           │       ├── Theme.kt
│           │       └── Type.kt
│           │
│           │   ├── ListGameApplication.kt
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
| 🔐 | **Authentication System** | Login & Register akun melalui REST API backend dengan validasi dan penyimpanan session DataStore. |
| 💾 | **Session Persistence** | Login user tetap tersimpan menggunakan DataStore Preferences. |
| 🌐 | **HTTP Request & Retrofit** | Seluruh data game, paket, transaksi, dan profil diambil langsung dari backend melalui Retrofit2. |
| 📋 | **Dynamic Game List** | Menampilkan daftar game populer dari API menggunakan `LazyColumn` dengan performa tinggi. |
| 🔍 | **Pencarian Real-Time** | Pencarian game instan secara realtime tanpa reload halaman. |
| 🔃 | **Sorting Game** | Sorting game berdasarkan A-Z, Z-A, dan Rating Tertinggi. |
| ❤️ | **Wishlist System** | Menyimpan game favorit ke backend menggunakan Favorite API endpoint. |
| 📄 | **Detail Game Lengkap** | Menampilkan banner, genre, rating, deskripsi, dan update terbaru game dari API. |
| 💎 | **Top Up Diamond** | Pembelian diamond atau game currency dengan berbagai nominal dari API. |
| 💳 | **Payment Method** | Simulasi pembayaran digital menggunakan QRIS, OVO, DANA, GoPay, dll. |
| 🎟️ | **Promo Code** | Sistem promo code otomatis dengan perhitungan diskon realtime. |
| 📋 | **Order Confirmation** | Ringkasan transaksi sebelum pembayaran dilakukan. |
| ⏳ | **Payment Progress** | Simulasi progress transaksi realtime hingga pembayaran berhasil. |
| 🧾 | **Invoice System** | Pembuatan invoice otomatis dan pengecekan transaksi berdasarkan Invoice ID melalui API. |
| 🪙 | **Nexus Coin** | Sistem koin internal: top up saldo, redeem voucher, dan riwayat transaksi via API. |
| 🧮 | **Kalkulator Win Rate** | Fitur utilitas untuk menghitung persentase Win Rate game. |
| 🔑 | **Forgot & Reset Password** | Alur check user dan reset password melalui API backend. |
| 👤 | **Profile Screen** | Menampilkan, mengedit informasi akun, ganti password, dan logout session. |
| 🧭 | **Custom Navigation** | Sistem navigasi berbasis custom backstack tanpa Navigation Component. |
| 💉 | **Dependency Injection** | Pengelolaan dependensi menggunakan Hilt (`AppModule`, `@HiltAndroidApp`). |

---

## 🧠 Arsitektur MVVM

Project menggunakan pendekatan modern Android Architecture yaitu:

### MVVM (Model View ViewModel)

#### 📦 Model
Berisi data class, representasi data aplikasi, dan API response model:
- `Game.kt`
- `User.kt`
- `Transaction.kt`
- `NexusCoinTransaction.kt`
- `api/ApiGame.kt`, `api/ApiPackage.kt`, `api/LoginRequest.kt`, `api/LoginResponse.kt`
- `api/GamesResponse.kt`, `api/PackageResponse.kt`, `api/HistoryResponse.kt`
- `api/Favorite.kt`, `api/ProfileApiModels.kt`, `api/NexusCoinApiModels.kt`
- `api/TransactionDetailResponse.kt`, `api/TransactionHistory.kt`

#### 🧠 ViewModel
Mengelola state aplikasi dan business logic:
- `AppViewModel.kt`
- `AuthViewModel.kt`
- `GameApiViewModel.kt`
- `PackageViewModel.kt`
- `HistoryViewModel.kt`
- `TransactionViewModel.kt`

#### 🌐 Network Layer
Menangani komunikasi HTTP dengan backend API:
- `RetrofitClient.kt`
- `ApiService.kt`
- `GameRepository.kt`
- `PackageRepository.kt`
- `FavoriteRepository.kt`
- `HistoryRepository.kt`
- `NexusCoinRepository.kt`
- `TransactionRequest.kt` / `TransactionResponse.kt`

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
- CekTransaksiScreen
- KalkulatorWinRateScreen
- ForgotPasswordScreen
- NexusCoinTopUpScreen
- NexusCoinHistoryScreen
- NexusCoinRedeemScreen

---

## 🌐 HTTP Request & Retrofit (Week 11)

### RetrofitClient
Konfigurasi singleton Retrofit yang mengarah ke backend API:

```kotlin
object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8000/api/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
```

### ApiService
Interface yang mendefinisikan seluruh endpoint REST API:

| Method | Endpoint | Fungsi |
| :--- | :--- | :--- |
| `POST` | `login` | Login akun pengguna |
| `POST` | `register` | Registrasi akun baru |
| `GET` | `games` | Mengambil daftar game dari server |
| `GET` | `games/{id}/packages` | Mengambil paket top up berdasarkan game |
| `POST` | `transactions` | Membuat transaksi top up baru |
| `GET` | `transactions` | Mengambil riwayat transaksi |
| `GET` | `transactions/{invoice_id}` | Mengambil detail transaksi by Invoice ID |
| `GET` | `favorites/{username}` | Mengambil daftar favorit user |
| `POST` | `favorites` | Toggle favorit game |
| `DELETE` | `favorites/{username}` | Menghapus semua favorit user |
| `GET` | `profile/{username}` | Mengambil profil pengguna |
| `PUT` | `profile` | Memperbarui data profil |
| `POST` | `change-password` | Mengganti password akun |
| `POST` | `check-user` | Cek keberadaan user (forgot password) |
| `POST` | `reset-password` | Reset password akun |
| `GET` | `nexus-coin/balance/{username}` | Mengambil saldo Nexus Coin |
| `GET` | `nexus-coin/history/{username}` | Mengambil riwayat Nexus Coin |
| `POST` | `nexus-coin/add` | Menambah saldo Nexus Coin |
| `POST` | `nexus-coin/deduct` | Mengurangi saldo Nexus Coin |

### Network Repositories
Setiap domain fitur memiliki repository tersendiri sebagai abstraction layer:

| Repository | Fungsi |
| :--- | :--- |
| `GameRepository` | Mengambil daftar game dari API |
| `PackageRepository` | Mengambil paket top up berdasarkan game ID |
| `FavoriteRepository` | Mengelola favorit game (get, toggle, clear) |
| `HistoryRepository` | Mengambil riwayat & detail transaksi |
| `NexusCoinRepository` | Mengelola saldo, riwayat, tambah, dan potong Nexus Coin |

---

## 🔐 Authentication System

### Login Screen
Halaman login utama aplikasi:
1. Input username & password
2. Validasi form login
3. Session login otomatis via API
4. Error handling jika input kosong
5. Redirect menuju dashboard

### Register Screen
Halaman registrasi akun:
1. Input username baru
2. Input password
3. Validasi register via API
4. Simpan data user ke backend
5. Redirect ke login

### Forgot Password Screen
Alur reset password:
1. Input username atau email
2. Verifikasi keberadaan user via `check-user` API
3. Reset password via `reset-password` API

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
4. Shortcut menuju Nexus Coin
5. Modern Compose UI
6. Bottom navigation bar

---

## 🎮 Game List Screen

Halaman daftar game populer:
1. Data game diambil dari API via `GameApiViewModel`
2. Search realtime
3. Sorting game
4. Wishlist system (tersinkron dengan API)
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
8. Tombol wishlist (toggle via API)
9. Tombol top up
10. Tombol share game

---

## 💎 Top Up Screen

Pengguna dapat melakukan pembelian diamond / currency game.

### 🎯 Input User ID
User memasukkan ID akun game mereka.

### 💎 Pilihan Nominal
Nominal paket diambil dari API berdasarkan game ID:
- 86 Diamonds
- 172 Diamonds
- 257 Diamonds
- 706 Diamonds
- Weekly Pass
- (dan lainnya sesuai data API)

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

Mensimulasikan transaksi pembayaran realtime dan menyimpan transaksi ke API.

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
- Transaksi tersimpan ke backend

---

## 🪙 Nexus Coin

Sistem koin internal aplikasi ListGame.

### NexusCoin Top Up Screen
Halaman top up saldo Nexus Coin:
1. Pilih nominal top up (misal: Rp 10.000, Rp 50.000, dll)
2. Pilih metode pembayaran
3. Saldo ditambahkan via `nexus-coin/add` API

### NexusCoin History Screen
Halaman riwayat & saldo Nexus Coin:
1. Tampilkan saldo terkini dari API
2. Riwayat transaksi koin

### NexusCoin Redeem Screen
Halaman redeem voucher Nexus Coin:
1. Input kode voucher
2. Koin dikurangi via `nexus-coin/deduct` API

---

## 🔎 Cek Transaksi Screen

Halaman pengecekan status transaksi:
1. Input Invoice ID
2. Tampilkan detail transaksi dari API
3. Status pembayaran

---

## 🧮 Kalkulator Win Rate Screen

Fitur utilitas tambahan:
1. Input jumlah menang & total match
2. Hitung persentase Win Rate otomatis

---

## 👤 Profile Screen

Halaman informasi user:
1. Data profil diambil dari API
2. Edit profil (display name, email, nomor HP, bio)
3. Ganti password
4. Logout account

---

## 🧭 Alur Navigasi

Aplikasi menggunakan sistem navigasi custom back stack:

```text
Login Screen ──────────────────────────── Forgot Password Screen
      ↓
Register Screen
      ↓
Dashboard Screen
  ├── Game List Screen
  │       ↓
  │   Game Detail Screen
  │       ↓
  │   Top Up Screen
  │       ↓
  │   Order Confirmation Screen
  │       ↓
  │   Payment Progress Screen
  │
  ├── Nexus Coin Top Up Screen
  ├── Nexus Coin History Screen
  ├── Nexus Coin Redeem Screen
  ├── Cek Transaksi Screen
  ├── Kalkulator Win Rate Screen
  └── Profile Screen
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

## 📁 Data: Game Populer dari API

Data game tidak lagi menggunakan dummy data lokal. Seluruh data diambil secara dinamis dari backend API melalui endpoint `GET /api/games`.

| Field | Tipe | Keterangan |
| :--- | :--- | :--- |
| `id` | Int | ID unik game |
| `title` | String | Nama game |
| `developer` | String | Nama developer |
| `description` | String | Deskripsi game |
| `image_url` | String | URL banner game |
| `category` | String | Genre/kategori game |

---

## 🏗️ Arsitektur & Teknologi

| Komponen | Teknologi |
| :--- | :--- |
| **Bahasa Pemrograman** | Kotlin |
| **Platform** | Android Native |
| **UI Framework** | Jetpack Compose (Material 3) |
| **Architecture** | MVVM |
| **Session Storage** | DataStore Preferences |
| **HTTP Client** | Retrofit2 2.11.0 |
| **JSON Converter** | Gson (GsonConverterFactory) |
| **HTTP Logging** | OkHttp3 Logging Interceptor 4.12.0 |
| **Dependency Injection** | Hilt |
| **State Management** | `remember`, `rememberSaveable`, `MutableStateFlow`, `StateFlow` |
| **Daftar Item** | `LazyColumn` & `LazyRow` |
| **Dialog** | `AlertDialog` |
| **Bottom Sheet** | `ModalBottomSheet` |
| **Sharing** | Android Intent |
| **Serialization** | kotlinx-serialization-json |
| **Min SDK** | 24 |
| **Target SDK** | 36 |

---

## 🛠️ Instalasi & Pengembangan

### **Prasyarat**
- Android Studio Hedgehog atau terbaru
- JDK 17+
- Android SDK 24+
- Backend API server berjalan di `http://localhost:8000`

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

4. **Jalankan Backend API**
```text
Pastikan backend API berjalan di http://localhost:8000
Aplikasi menggunakan http://10.0.2.2:8000/api/ (emulator → localhost)
```

5. **Run Aplikasi**
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

## 🎬 Link Video YouTube Penjelasan

1. Pertemuan 4
https://youtube.com/shorts/VuOMfvkpf8g

2. Pertemuan 5
https://youtube.com/shorts/KDzaNaEwlFA

3. Pertemuan 6
https://youtube.com/shorts/d-tUgYyTO3c

4. Pertemuan 9
https://youtube.com/shorts/TkFYY_ZmT7k

5. Pertemuan 10
https://youtube.com/shorts/3sYgIvGM6ds?si=8cCZ4adF4pMc62tU

6. Pertemuan 11
https://www.youtube.com/watch?v=eRgec5aUhDY

---

<p align="center">
  <strong>Program Studi Informatika — Fakultas Teknologi Informasi dan Sains Data</strong><br>
  Universitas Sebelas Maret &nbsp;·&nbsp; Mata Kuliah: Pengembangan Aplikasi Bergerak (Week 11)
</p>
