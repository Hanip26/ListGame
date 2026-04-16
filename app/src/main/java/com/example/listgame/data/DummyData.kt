package com.example.listgame.data

import com.example.listgame.R
import com.example.listgame.model.Game

object DummyData {
    val popularGames = listOf(
        Game(
            id = 1,
            title = "Mobile Legends: Bang Bang",
            developer = "Moonton",
            description = "Mobile Legends: Bang Bang adalah game arena pertarungan daring multipemain (MOBA) seluler 5v5 yang sangat populer...",
            rating = 4.6,
            size = "1.5 GB",
            genres = listOf("#1 terlaris di laga", "MOBA", "Multiplayer kompetitif", "Strategi", "Intens"),
            latestUpdate = "Patch Terbaru: Penyesuaian keseimbangan (Buff/Nerf) untuk beberapa Hero lama, optimalisasi sistem Matchmaking...",
            imageRes = R.drawable.mlbb
        ),
        Game(
            id = 2,
            title = "Free Fire",
            developer = "Garena",
            description = "Garena Free Fire adalah game survival shooter (Battle Royale) yang dirancang khusus untuk perangkat seluler...",
            rating = 4.3,
            size = "1.2 GB",
            genres = listOf("Battle Royale", "Survival shooter", "Multiplayer kompetitif", "Penuh aksi"),
            latestUpdate = "Update OB43: Karakter baru dengan kemampuan manipulasi area, senjata jenis Marksman Rifle baru...",
            imageRes = R.drawable.ff
        ),
        Game(
            id = 3,
            title = "Genshin Impact",
            developer = "HoYoverse",
            description = "Genshin Impact adalah game Action RPG Open-World yang menawarkan dunia fantasi luas bernama Teyvat...",
            rating = 4.7,
            size = "20.5 GB",
            genres = listOf("#2 terlaris di RPG", "Action RPG", "Open World", "Eksplorasi", "Visual memukau"),
            latestUpdate = "Versi 4.5: Penambahan karakter bintang 5 baru (Chiori), ekspansi area map baru untuk dieksplorasi...",
            imageRes = R.drawable.gensin
        ),
        Game(
            id = 4,
            title = "PUBG Mobile",
            developer = "Level Infinite",
            description = "PlayerUnknown's Battlegrounds (PUBG) Mobile menghadirkan pengalaman Battle Royale klasik yang realistis...",
            rating = 4.4,
            size = "2.8 GB",
            genres = listOf("#4 terlaris di aksi", "Tactical shooter", "Intens", "Multiplayer kompetitif", "Realistis", "Pencapaian"),
            latestUpdate = "Mode Tematik Baru: Menambahkan mekanik pertempuran jarak dekat (Melee) yang ditingkatkan, item taktis baru...",
            imageRes = R.drawable.pubg
        ),
        Game(
            id = 5,
            title = "Roblox",
            developer = "Roblox Corporation",
            description = "Roblox bukanlah sekadar game, melainkan platform imajinatif di mana jutaan pemain dapat membuat, membagikan...",
            rating = 4.4,
            size = "180 MB",
            genres = listOf("Sandbox", "MMO", "Kreatif", "Simulasi", "Multiplayer"),
            latestUpdate = "Pembaruan Engine: Peningkatan kualitas grafis fisika material, penambahan fitur Voice Chat (obrolan suara) spasial...",
            imageRes = R.drawable.roblox
        ),
        Game(
            id = 6,
            title = "Call of Duty: Mobile",
            developer = "Activision",
            description = "Call of Duty: Mobile (CODM) membawa aksi tembak-menembak berkecepatan tinggi (FPS) dari konsol ke genggaman Anda...",
            rating = 4.5,
            size = "2.4 GB",
            genres = listOf("FPS", "Tembak-menembak", "Multiplayer kompetitif", "Intens", "Realistis"),
            latestUpdate = "Season Terbaru: Battle Pass bertema Retro, penambahan map Multiplayer eksklusif, dan penyesuaian stabilitas...",
            imageRes = R.drawable.cod
        ),
        Game(
            id = 7,
            title = "Among Us",
            developer = "Innersloth",
            description = "Among Us adalah game deduksi sosial yang seru dimainkan bersama 4 hingga 15 pemain secara online atau via Wi-Fi lokal...",
            rating = 4.2,
            size = "250 MB",
            genres = listOf("Deduksi sosial", "Penuh tipu daya", "Multiplayer", "Kasual", "Pencapaian"),
            latestUpdate = "Map Baru 'The Fungle': Area hutan jamur misterius dengan tugas-tugas baru, mekanik sabotase asap...",
            imageRes = R.drawable.amoung
        )
    )
}