package com.example.listgame.ui.screen

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listgame.data.DummyData
import com.example.listgame.model.Game
import com.example.listgame.navigation.LocalBackStack
import com.example.listgame.navigation.Route

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun GameListScreen(
    username: String,
    favoriteGames: List<Int>,
    sortOption: String,
    onSortChange: (String) -> Unit,
    onFavoriteToggle: (Int) -> Unit,
    onClearFavorites: () -> Unit,
    onLogout: () -> Unit,
    onNavigateToDashboard: () -> Unit,
    onNavigateToProfile: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier
) {
    val backStack = LocalBackStack.current
    var searchQuery      by rememberSaveable { mutableStateOf("") }
    var isWishlistMode   by rememberSaveable { mutableStateOf(false) }
    var selectedCategory by rememberSaveable { mutableStateOf("Semua") }

    val categories = listOf(
        "Semua", "MOBA", "Battle Royale", "RPG", "FPS", "Sandbox", "Deduksi sosial"
    )

    // ── Dialog & Menu state ───────────────────────────────────────────────────
    var showLogoutDialog       by remember { mutableStateOf(false) }
    var showClearWishlistDialog by remember { mutableStateOf(false) }
    var showSortDialog         by remember { mutableStateOf(false) }
    var gameToRemove           by remember { mutableStateOf<Game?>(null) }

    // ✅ State untuk DropdownMenu titik tiga
    var showDropdownMenu by remember { mutableStateOf(false) }

    // ── Filter & Sort ─────────────────────────────────────────────────────────
    val filteredGames = DummyData.popularGames.filter { game ->
        val matchesSearch   = game.title.contains(searchQuery, ignoreCase = true)
        val matchesWishlist = if (isWishlistMode) favoriteGames.contains(game.id) else true
        val matchesCategory = if (selectedCategory == "Semua") true
        else game.genres.any {
            it.contains(selectedCategory, ignoreCase = true)
        }
        matchesSearch && matchesWishlist && matchesCategory
    }.let { list ->
        when (sortOption) {
            "A-Z"              -> list.sortedBy { it.title }
            "Z-A"             -> list.sortedByDescending { it.title }
            "Rating Tertinggi" -> list.sortedByDescending { it.rating }
            else              -> list
        }
    }

    // ── Dialog: Sort ──────────────────────────────────────────────────────────
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
                                    onClick  = { onSortChange(text); showSortDialog = false },
                                    role     = Role.RadioButton
                                )
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = (text == sortOption), onClick = null)
                            Text(text, modifier = Modifier.padding(start = 16.dp))
                        }
                    }
                }
            },
            confirmButton  = {},
            dismissButton  = {
                TextButton(onClick = { showSortDialog = false }) { Text("Batal") }
            }
        )
    }

    // ── Dialog: Logout ────────────────────────────────────────────────────────
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Konfirmasi Logout") },
            text  = { Text("Yakin ingin keluar dari akun ini?") },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    onLogout()
                }) { Text("Keluar", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) { Text("Batal") }
            }
        )
    }

    // ── Dialog: Hapus satu favorit ────────────────────────────────────────────
    if (gameToRemove != null) {
        AlertDialog(
            onDismissRequest = { gameToRemove = null },
            title = { Text("Hapus Favorit") },
            text  = { Text("Hapus '${gameToRemove?.title}' dari favorit?") },
            confirmButton = {
                TextButton(onClick = {
                    gameToRemove?.let { onFavoriteToggle(it.id) }
                    gameToRemove = null
                }) { Text("Hapus", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { gameToRemove = null }) { Text("Batal") }
            }
        )
    }

    // ── Dialog: Kosongkan wishlist ────────────────────────────────────────────
    if (showClearWishlistDialog) {
        AlertDialog(
            onDismissRequest = { showClearWishlistDialog = false },
            title = { Text("Kosongkan Wishlist") },
            text  = { Text("Hapus semua game dari favorit?") },
            confirmButton = {
                TextButton(onClick = {
                    onClearFavorites()
                    showClearWishlistDialog = false
                }) { Text("Hapus", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { showClearWishlistDialog = false }) { Text("Batal") }
            }
        )
    }

    // ── Scaffold ──────────────────────────────────────────────────────────────
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        if (isWishlistMode) "Wishlist Saya" else "NEXUS",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    // ✅ Hanya 2 ikon di TopAppBar: Wishlist + Titik Tiga
                    // Ikon Wishlist — tetap visible karena sering dipakai
                    IconButton(onClick = { isWishlistMode = !isWishlistMode }) {
                        Icon(
                            imageVector = if (isWishlistMode) Icons.Rounded.Favorite
                            else Icons.Rounded.FavoriteBorder,
                            tint = if (isWishlistMode) Color.Red
                            else MaterialTheme.colorScheme.onSurface,
                            contentDescription = "Wishlist"
                        )
                    }

                    // ✅ Tombol titik tiga — semua opsi lain masuk ke sini
                    Box {
                        IconButton(onClick = { showDropdownMenu = true }) {
                            Icon(
                                Icons.Rounded.MoreVert,
                                contentDescription = "Menu",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        // ✅ DropdownMenu — muncul dari titik tiga
                        DropdownMenu(
                            expanded        = showDropdownMenu,
                            onDismissRequest = { showDropdownMenu = false },
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .width(220.dp)
                        ) {
                            // ── Grup 1: Fitur utama ───────────────────────
                            MenuSectionLabel("Fitur")

                            NexusMenuItem(
                                icon    = Icons.Rounded.ShoppingCart,
                                label   = "Top Up",
                                iconTint = MaterialTheme.colorScheme.primary,
                                onClick = {
                                    showDropdownMenu = false
                                    // TODO: navigasi ke TopUp general jika ada
                                }
                            )

                            NexusMenuItem(
                                icon    = Icons.AutoMirrored.Rounded.List,
                                label   = "Urutkan Game",
                                onClick = {
                                    showDropdownMenu = false
                                    showSortDialog   = true
                                }
                            )

                            NexusMenuItem(
                                icon    = Icons.Rounded.Search,
                                label   = "Cek Transaksi",
                                onClick = {
                                    showDropdownMenu = false
                                    // TODO: navigasi ke halaman transaksi
                                }
                            )

                            // ── Divider ───────────────────────────────────
                            HorizontalDivider(
                                modifier  = Modifier.padding(vertical = 4.dp),
                                color     = MaterialTheme.colorScheme.outlineVariant
                            )

                            // ── Grup 2: Akun ──────────────────────────────
                            MenuSectionLabel("Akun")

                            NexusMenuItem(
                                icon    = Icons.Rounded.Dashboard,
                                label   = "Dashboard",
                                onClick = {
                                    showDropdownMenu = false
                                    onNavigateToDashboard()
                                }
                            )

                            NexusMenuItem(
                                icon    = Icons.Rounded.AccountCircle,
                                label   = "Profil",
                                onClick = {
                                    showDropdownMenu = false
                                    onNavigateToProfile()
                                }
                            )

                            NexusMenuItem(
                                icon     = Icons.Rounded.MonetizationOn,
                                label    = "Nexus Coin",
                                subLabel = "Bebas Biaya Admin",
                                iconTint = Color(0xFFFFD700),
                                onClick  = {
                                    showDropdownMenu = false
                                    // TODO: navigasi ke halaman Nexus Coin
                                }
                            )

                            // ── Divider ───────────────────────────────────
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 4.dp),
                                color    = MaterialTheme.colorScheme.outlineVariant
                            )

                            // ── Wishlist clear (kondisional) ──────────────
                            if (isWishlistMode && favoriteGames.isNotEmpty()) {
                                NexusMenuItem(
                                    icon     = Icons.Rounded.Delete,
                                    label    = "Kosongkan Wishlist",
                                    iconTint = MaterialTheme.colorScheme.error,
                                    labelColor = MaterialTheme.colorScheme.error,
                                    onClick  = {
                                        showDropdownMenu      = false
                                        showClearWishlistDialog = true
                                    }
                                )
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 4.dp),
                                    color    = MaterialTheme.colorScheme.outlineVariant
                                )
                            }

                            // ── Logout ────────────────────────────────────
                            NexusMenuItem(
                                icon       = Icons.AutoMirrored.Rounded.ExitToApp,
                                label      = "Keluar",
                                iconTint   = MaterialTheme.colorScheme.error,
                                labelColor = MaterialTheme.colorScheme.error,
                                onClick    = {
                                    showDropdownMenu = false
                                    showLogoutDialog = true
                                }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(modifier = modifier.fillMaxSize().padding(innerPadding)) {
            Text(
                text      = "Hallo $username, cari game apa?",
                style     = MaterialTheme.typography.bodyLarge,
                modifier  = Modifier.fillMaxWidth().padding(16.dp),
                textAlign = TextAlign.Center
            )

            OutlinedTextField(
                value         = searchQuery,
                onValueChange = { searchQuery = it },
                modifier      = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                placeholder   = { Text("Cari game...") },
                leadingIcon   = { Icon(Icons.Rounded.Search, null) },
                shape         = RoundedCornerShape(16.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding        = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                modifier              = Modifier.fillMaxWidth()
            ) {
                items(categories) { category ->
                    FilterChip(
                        selected = (selectedCategory == category),
                        onClick  = { selectedCategory = category },
                        label    = { Text(category) },
                        colors   = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor     = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding      = PaddingValues(
                    start = 16.dp, end = 16.dp, bottom = 16.dp
                ),
                modifier = Modifier.fillMaxSize()
            ) {
                if (filteredGames.isEmpty()) {
                    item {
                        Column(
                            modifier            = Modifier
                                .fillParentMaxSize()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            val isWishlistEmpty = isWishlistMode && favoriteGames.isEmpty()
                            Icon(
                                imageVector = if (isWishlistEmpty)
                                    Icons.Rounded.FavoriteBorder
                                else Icons.Rounded.Search,
                                contentDescription = null,
                                modifier           = Modifier.size(100.dp),
                                tint               = MaterialTheme.colorScheme.primary
                                    .copy(alpha = 0.5f)
                            )
                            Spacer(Modifier.height(24.dp))
                            Text(
                                text       = if (isWishlistEmpty) "Wishlist Masih Kosong"
                                else "Game Tidak Ditemukan",
                                style      = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                textAlign  = TextAlign.Center
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text      = if (isWishlistEmpty)
                                    "Kamu belum menambahkan game apa pun. Klik ikon hati pada daftar game untuk menyimpannya ke sini."
                                else
                                    "Maaf, tidak ada game dengan kategori atau kata kunci tersebut.",
                                style     = MaterialTheme.typography.bodyMedium,
                                color     = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    items(filteredGames, key = { it.id }) { game ->
                        val isFav = favoriteGames.contains(game.id)
                        GameListItem(
                            game                    = game,
                            isFavorite              = isFav,
                            onFavoriteClick         = {
                                if (isFav) gameToRemove = game
                                else onFavoriteToggle(game.id)
                            },
                            sharedTransitionScope   = sharedTransitionScope,
                            animatedVisibilityScope = animatedVisibilityScope,
                            modifier                = Modifier
                                .animateItem()
                                .clickable { backStack.add(Route.Detail(game.id)) }
                        )
                    }
                }
            }
        }
    }
}

// ── Komponen: Label Section di Dropdown ──────────────────────────────────────

@Composable
fun MenuSectionLabel(title: String) {
    Text(
        text     = title,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color    = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(
            start = 16.dp, end = 16.dp,
            top = 8.dp, bottom = 2.dp
        )
    )
}

// ── Komponen: Item Menu Dropdown ──────────────────────────────────────────────

@Composable
fun NexusMenuItem(
    icon: ImageVector,
    label: String,
    subLabel: String   = "",
    iconTint: Color    = Color.Unspecified,
    labelColor: Color  = Color.Unspecified,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        text = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Ikon dengan background bulat
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (iconTint == Color.Unspecified)
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            else
                                iconTint.copy(alpha = 0.12f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (iconTint == Color.Unspecified)
                            MaterialTheme.colorScheme.primary
                        else iconTint,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Label & subLabel
                Column {
                    Text(
                        text = label,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (labelColor == Color.Unspecified)
                            MaterialTheme.colorScheme.onSurface
                        else labelColor
                    )
                    if (subLabel.isNotEmpty()) {
                        Text(
                            text = subLabel,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        },
        onClick = onClick,
        modifier = Modifier.padding(horizontal = 4.dp)
    )
}

// ── GameListItem ──────────────────────────────────────────────────────────────

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun GameListItem(
    game: Game,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier
) {
    Card(
        modifier  = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors    = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier          = Modifier.padding(16.dp)
        ) {
            with(sharedTransitionScope) {
                Image(
                    painter            = painterResource(id = game.imageRes),
                    contentDescription = null,
                    contentScale       = ContentScale.Crop,
                    modifier           = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .sharedElement(
                            sharedContentState      = rememberSharedContentState(
                                key = "image_${game.id}"
                            ),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                )
            }
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    game.title,
                    style      = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    game.developer,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    game.description,
                    style    = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = if (isFavorite) Icons.Rounded.Favorite
                    else Icons.Rounded.FavoriteBorder,
                    tint        = if (isFavorite) Color.Red
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    contentDescription = null
                )
            }
        }
    }
}