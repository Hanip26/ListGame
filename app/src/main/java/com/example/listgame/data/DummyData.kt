package com.example.listgame.data

import com.example.listgame.R
import com.example.listgame.model.Game
import com.example.listgame.model.TopUpOption

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
            imageRes = R.drawable.mlbb,
            topUpOptions = listOf(
                TopUpOption("86 Diamond", "Rp 19.000"),
                TopUpOption("172 Diamond", "Rp 38.000"),
                TopUpOption("257 Diamond", "Rp 57.000", "Hemat 5%"),
                TopUpOption("514 Diamond", "Rp 110.000", "Hemat 8%"),
                TopUpOption("1.050 Diamond", "Rp 219.000", "Hemat 10%"),
                TopUpOption("2.195 Diamond", "Rp 439.000", "Hemat 15%")
            )
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
            imageRes = R.drawable.ff,
            topUpOptions = listOf(
                TopUpOption("100 Diamond", "Rp 15.000"),
                TopUpOption("210 Diamond", "Rp 29.000"),
                TopUpOption("520 Diamond", "Rp 69.000", "Hemat 5%"),
                TopUpOption("1.060 Diamond", "Rp 135.000", "Hemat 8%"),
                TopUpOption("2.180 Diamond", "Rp 269.000", "Hemat 12%"),
                TopUpOption("5.600 Diamond", "Rp 669.000", "Hemat 18%")
            )
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
            imageRes = R.drawable.gensin,
            topUpOptions = listOf(
                TopUpOption("60 Genesis Crystal", "Rp 14.000"),
                TopUpOption("300 Genesis Crystal", "Rp 69.000", "Bonus 30"),
                TopUpOption("980 Genesis Crystal", "Rp 219.000", "Bonus 110"),
                TopUpOption("1.980 Genesis Crystal", "Rp 439.000", "Bonus 260"),
                TopUpOption("3.280 Genesis Crystal", "Rp 699.000", "Bonus 600"),
                TopUpOption("6.480 Genesis Crystal", "Rp 1.399.000", "Bonus 1.600")
            )
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
            imageRes = R.drawable.pubg,
            topUpOptions = listOf(
                TopUpOption("60 UC", "Rp 15.000"),
                TopUpOption("300 UC", "Rp 69.000", "Bonus 25 UC"),
                TopUpOption("600 UC", "Rp 135.000", "Bonus 60 UC"),
                TopUpOption("1.500 UC", "Rp 329.000", "Bonus 300 UC"),
                TopUpOption("3.000 UC", "Rp 649.000", "Bonus 850 UC"),
                TopUpOption("6.000 UC", "Rp 1.299.000", "Bonus 2.100 UC")
            )
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
            imageRes = R.drawable.roblox,
            topUpOptions = listOf(
                TopUpOption("400 Robux", "Rp 69.000"),
                TopUpOption("800 Robux", "Rp 135.000"),
                TopUpOption("1.700 Robux", "Rp 269.000", "Hemat 5%"),
                TopUpOption("4.500 Robux", "Rp 699.000", "Hemat 10%"),
                TopUpOption("10.000 Robux", "Rp 1.499.000", "Hemat 15%")
            )
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
            imageRes = R.drawable.cod,
            topUpOptions = listOf(
                TopUpOption("80 CP", "Rp 15.000"),
                TopUpOption("400 CP", "Rp 69.000", "Bonus 40 CP"),
                TopUpOption("800 CP", "Rp 135.000", "Bonus 100 CP"),
                TopUpOption("2.000 CP", "Rp 329.000", "Bonus 400 CP"),
                TopUpOption("4.000 CP", "Rp 649.000", "Bonus 1.000 CP"),
                TopUpOption("8.000 CP", "Rp 1.299.000", "Bonus 3.000 CP")
            )
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
            imageRes = R.drawable.amoung,
            topUpOptions = listOf(
                TopUpOption("500 Beans", "Rp 9.000"),
                TopUpOption("1.000 Beans", "Rp 17.000"),
                TopUpOption("2.500 Beans", "Rp 39.000", "Hemat 5%"),
                TopUpOption("5.000 Beans", "Rp 75.000", "Hemat 8%"),
                TopUpOption("10.000 Beans", "Rp 145.000", "Hemat 12%")
            )
        )
    )
}