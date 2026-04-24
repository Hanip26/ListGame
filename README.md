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

# 📱Tentang Aplikasi
ListGame adalah aplikasi Android Native yang dikembangkan sebagai bagian dari praktikum
Pengembangan Aplikasi Bergerak (PAB).
Aplikasi ini memungkinkan pengguna untuk:
1. Login menggunakan nama
2. Menjelajahi daftar 7 game populer
3. Melakukan pencarian instan
4. Melihat detail game secara lengkap
5. Menyimpan game favorit ke wishlist pribadi

# ✨ Highlights
🔐 Login dengan username (personalized greeting)
🔍 Pencarian game secara real-time
❤️ Wishlist / favorit system
📄 Detail game lengkap (rating, genre, deskripsi)
⚡ UI modern berbasis Jetpack Compose
🧭 Custom navigation (tanpa Navigation Component)

# 📁 Struktur Proyek
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

**ListGame** adalah aplikasi Android berbasis **Jetpack Compose** yang menampilkan daftar game populer seperti Mobile Legends, Free Fire, Genshin Impact, dan lainnya.

Aplikasi ini dirancang untuk:
- Menampilkan informasi game secara ringkas dan detail
- Memberikan pengalaman UI modern berbasis Compose
- Melatih konsep navigasi dan state management di Android

---

## 🚀 Fitur Utama

1. **📋 Dynamic Game List**  
   Menampilkan daftar game populer dalam bentuk card list lengkap dengan gambar, nama game, developer, dan deskripsi singkat.

2. **🔍 Search Bar (UI)**  
   Tersedia kolom pencarian untuk mencari game (UI sudah tersedia dan siap dikembangkan lebih lanjut).

3. **❤️ Favorite Icon (UI Interaction)**  
   Setiap item memiliki ikon favorit (heart) sebagai elemen interaktif.

4. **📄 Detail Game Screen**  
   Saat item dipilih, pengguna akan masuk ke halaman detail yang menampilkan:
   - Banner / gambar game
   - Nama game & developer
   - Kategori (tag)
   - Rating & ukuran game
   - Deskripsi lengkap
   - Informasi update terbaru

5. **🧭 Navigation System**  
   Navigasi antar halaman (List → Detail) menggunakan pendekatan modern Android.

6. **⚡ Smooth & Modern UI**  
   Menggunakan Material Design dengan tampilan clean, card-based layout, dan responsif.

---

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

## 🛠️ Arsitektur & Teknologi

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
* SDK Android API 24 (Nougat) ke atas.

**Langkah-langkah:**
1.  **Clone the Repo**:
    ```bash
    git clone https://github.com/Hanip26/ListGame.git
    ```
2.  **Import Project**: Buka Android Studio, pilih `Open` dan arahkan ke folder hasil clone.
3.  **Sync Gradle**: Biarkan Android Studio mengunduh dependensi yang diperlukan.
4.  **Run**: Jalankan di Emulator atau Device fisik melalui tombol `Shift + F10`.

---

## 🧭 Struktur Navigasi Proyek


Proyek ini dikembangkan untuk memenuhi tugas Praktikum **Mobile Application Programming (PAB)**. 

1. Hanief Fahrel Wilianto (L0324016)
2. Muhammad Affan Nur Zhafariza (L0324022)
3. Muhammad Rafii Setianto (L0324026)
   
---

## Link Youtube

1. https://youtube.com/shorts/VuOMfvkpf8g (Pertemuan 4)
2. https://youtube.com/shorts/KDzaNaEwlFA (Pertemuan 5)

---
