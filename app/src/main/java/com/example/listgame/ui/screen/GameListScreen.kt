package com.example.listgame.ui.screen

import androidx.compose.animation.AnimatedVisibilityScope // EFEK TRANSISI
import androidx.compose.animation.ExperimentalSharedTransitionApi // EFEK TRANSISI
import androidx.compose.animation.SharedTransitionScope // EFEK TRANSISI
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ExitToApp
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.listgame.data.DummyData
import com.example.listgame.model.Game
import com.example.listgame.navigation.LocalBackStack
import com.example.listgame.navigation.Route

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class) // EFEK TRANSISI
@Composable
fun GameListScreen(
    username: String,
    favoriteGames: List<Int>,
    sortOption: String,
    onSortChange: (String) -> Unit,
    onFavoriteToggle: (Int) -> Unit,
    onClearFavorites: () -> Unit,
    sharedTransitionScope: SharedTransitionScope, // EFEK TRANSISI
    animatedVisibilityScope: AnimatedVisibilityScope, // EFEK TRANSISI
    modifier: Modifier = Modifier
) {
    val backStack = LocalBackStack.current
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var isWishlistMode by rememberSaveable { mutableStateOf(false) }

    // --- FITUR 1: State untuk Filter Chips ---
    var selectedCategory by rememberSaveable { mutableStateOf("Semua") }
    val categories = listOf("Semua", "MOBA", "Battle Royale", "RPG", "FPS", "Sandbox", "Deduksi sosial")

    var showLogoutDialog by remember { mutableStateOf(false) }
    var showClearWishlistDialog by remember { mutableStateOf(false) }
    var showSortDialog by remember { mutableStateOf(false) }
    var gameToRemove by remember { mutableStateOf<Game?>(null) }

    val filteredGames = DummyData.popularGames.filter { game ->
        val matchesSearch = game.title.contains(searchQuery, ignoreCase = true)
        val matchesWishlist = if (isWishlistMode) favoriteGames.contains(game.id) else true
        // Logika baru: Filter berdasarkan Kategori Genre
        val matchesCategory = if (selectedCategory == "Semua") {
            true
        } else {
            game.genres.any { it.contains(selectedCategory, ignoreCase = true) }
        }

        matchesSearch && matchesWishlist && matchesCategory
    }.let { list ->
        when (sortOption) {
            "A-Z" -> list.sortedBy { it.title }
            "Z-A" -> list.sortedByDescending { it.title }
            "Rating Tertinggi" -> list.sortedByDescending { it.rating }
            else -> list
        }
    }

    if (showSortDialog) {
        val radioOptions = listOf("A-Z", "Z-A", "Rating Tertinggi")
        AlertDialog(
            onDismissRequest = { showSortDialog = false },
            title = { Text("Urutkan Game", fontWeight = FontWeight.Bold) },
            text = {
                Column(Modifier.selectableGroup()) {
                    radioOptions.forEach { text ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .selectable(
                                    selected = (text == sortOption),
                                    onClick = {
                                        onSortChange(text)
                                        showSortDialog = false
                                    },
                                    role = Role.RadioButton
                                )
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = (text == sortOption), onClick = null)
                            Text(text = text, modifier = Modifier.padding(start = 16.dp))
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = { TextButton(onClick = { showSortDialog = false }) { Text("Batal") } }
        )
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Konfirmasi") },
            text = { Text("Yakin ingin kembali ke login?") },
            confirmButton = {
                TextButton(onClick = {
                    backStack.clear()
                    backStack.add(Route.Login)
                }) { Text("Ya", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = { TextButton(onClick = { showLogoutDialog = false }) { Text("Batal") } }
        )
    }

    if (gameToRemove != null) {
        AlertDialog(
            onDismissRequest = { gameToRemove = null },
            title = { Text("Hapus") },
            text = { Text("Hapus '${gameToRemove?.title}' dari favorit?") },
            confirmButton = {
                TextButton(onClick = {
                    gameToRemove?.let { onFavoriteToggle(it.id) }
                    gameToRemove = null
                }) { Text("Hapus", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = { TextButton(onClick = { gameToRemove = null }) { Text("Batal") } }
        )
    }

    if (showClearWishlistDialog) {
        AlertDialog(
            onDismissRequest = { showClearWishlistDialog = false },
            title = { Text("Kosongkan") },
            text = { Text("Hapus semua game dari favorit?") },
            confirmButton = {
                TextButton(onClick = {
                    onClearFavorites()
                    showClearWishlistDialog = false
                }) { Text("Hapus", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = { TextButton(onClick = { showClearWishlistDialog = false }) { Text("Batal") } }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (isWishlistMode) "Wishlist Saya" else "NEXUS", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { showSortDialog = true }) {
                        Icon(imageVector = Icons.AutoMirrored.Rounded.List, contentDescription = "Sort")
                    }
                    if (isWishlistMode && favoriteGames.isNotEmpty()) {
                        IconButton(onClick = { showClearWishlistDialog = true }) {
                            Icon(imageVector = Icons.Rounded.Delete, tint = MaterialTheme.colorScheme.error, contentDescription = null)
                        }
                    }
                    IconButton(onClick = { isWishlistMode = !isWishlistMode }) {
                        Icon(
                            imageVector = if (isWishlistMode) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                            tint = if (isWishlistMode) Color.Red else MaterialTheme.colorScheme.onSurface,
                            contentDescription = null
                        )
                    }
                    IconButton(onClick = { showLogoutDialog = true }) {
                        Icon(imageVector = Icons.AutoMirrored.Rounded.ExitToApp, tint = MaterialTheme.colorScheme.error, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        }
    ) { innerPadding ->
        Column(modifier = modifier.fillMaxSize().padding(innerPadding)) {
            Text(
                text = "Hallo $username, cari game apa?",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                textAlign = TextAlign.Center
            )

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                placeholder = { Text("Cari game...") },
                leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
                shape = RoundedCornerShape(16.dp)
            )

            // --- FITUR 1: Komponen LazyRow untuk Filter Chips ---
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { category ->
                    FilterChip(
                        selected = (selectedCategory == category),
                        onClick = { selectedCategory = category },
                        label = { Text(category) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                if (filteredGames.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillParentMaxSize()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            val isWishlistEmpty = isWishlistMode && favoriteGames.isEmpty()
                            val emptyIcon = if (isWishlistEmpty) Icons.Rounded.FavoriteBorder else Icons.Rounded.Search
                            val emptyTitle = if (isWishlistEmpty) "Wishlist Masih Kosong" else "Game Tidak Ditemukan"
                            val emptyDesc = if (isWishlistEmpty) {
                                "Kamu belum menambahkan game apa pun. Klik ikon hati pada daftar game untuk menyimpannya ke sini."
                            } else {
                                "Maaf, tidak ada game dengan kategori atau kata kunci tersebut."
                            }

                            Icon(
                                imageVector = emptyIcon,
                                contentDescription = "Empty State",
                                modifier = Modifier.size(100.dp),
                                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(text = emptyTitle, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = emptyDesc, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
                        }
                    }
                } else {
                    items(filteredGames, key = { it.id }) { game ->
                        val isFav = favoriteGames.contains(game.id)
                        GameListItem(
                            game = game,
                            isFavorite = isFav,
                            onFavoriteClick = { if (isFav) gameToRemove = game else onFavoriteToggle(game.id) },
                            // EFEK TRANSISI: Pass scope ke komponen
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = animatedVisibilityScope,
                            modifier = Modifier
                                .animateItem()
                                .clickable { backStack.add(Route.Detail(game.id)) }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class) // EFEK TRANSISI
@Composable
fun GameListItem(
    game: Game,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    sharedTransitionScope: SharedTransitionScope, // EFEK TRANSISI
    animatedVisibilityScope: AnimatedVisibilityScope, // EFEK TRANSISI
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            // EFEK TRANSISI: Mengatur gambar agar bisa melayang
            with(sharedTransitionScope) {
                Image(
                    painter = painterResource(id = game.imageRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .sharedElement(
                            sharedContentState = rememberSharedContentState(key = "image_${game.id}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = game.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = game.developer, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                Text(text = game.description, style = MaterialTheme.typography.bodyMedium, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = if (isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                    tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant,
                    contentDescription = null
                )
            }
        }
    }
}