# 🎮 ListGame App

![Kotlin](https://img.shields.io/badge/Kotlin-Programming-blue?logo=kotlin) 
![Android Studio](https://img.shields.io/badge/IDE-Android%20Studio-green?logo=androidstudio)
![Platform](https://img.shields.io/badge/Platform-Android-brightgreen?logo=android)
![License](https://img.shields.io/badge/license-Free-blue) 

> 📱 Aplikasi Android modern untuk menampilkan daftar game populer dengan tampilan menarik dan navigasi interaktif.

---
## 🌟 Mengapa ListGame?
Di era mobile gaming yang pesat, pengguna membutuhkan antarmuka yang bersih untuk mengeksplorasi pilihan mereka. ListGame hadir dengan filosofi desain yang mengutamakan kecepatan akses informasi dan estetika visual yang minimalis namun informatif.

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
│           │   ├── ui
│           │   │   ├── screen
│           │   │   │   ├── GameDetailScreen.kt
│           │   │   │   └── GameListScreen.kt
│           │   │   └── theme
│           │   │       ├── Color.kt
│           │   │       ├── Theme.kt
│           │   │       └── Type.kt
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
