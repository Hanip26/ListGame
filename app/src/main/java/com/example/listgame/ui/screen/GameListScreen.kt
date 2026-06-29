package com.example.listgame.ui.screen

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.listgame.R
import com.example.listgame.model.Game
import com.example.listgame.model.GameApiViewModel
import com.example.listgame.navigation.LocalBackStack
import com.example.listgame.navigation.Route
import com.example.listgame.ui.components.BottomNavBar
import com.example.listgame.ui.components.BottomNavDestination
import kotlinx.coroutines.launch

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

    val gameApiViewModel: GameApiViewModel = viewModel()
    val apiGames by gameApiViewModel.games.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        gameApiViewModel.loadGames()
    }

    val categories = listOf(
        "Semua", "MOBA", "Battle Royale", "RPG", "FPS", "Sandbox", "Deduksi sosial"
    )

    var showSortDialog         by remember { mutableStateOf(false) }
    var showDropdownMenu       by remember { mutableStateOf(false) }

    val games = apiGames.map { apiGame ->
        Game(
            id = apiGame.id,
            title = apiGame.title,
            developer = apiGame.developer,
            description = apiGame.description,
            rating = 4.7, // Dioptimalkan untuk visualisasi rating badge kompetisi
            size = "1.2 GB",
            genres = listOf(apiGame.category),
            latestUpdate = "-",
            imageRes = when(apiGame.image_url){
                "mlbb" -> R.drawable.mlbb
                "ff" -> R.drawable.ff
                "pubg" -> R.drawable.pubg
                "genshin" -> R.drawable.gensin
                "roblox" -> R.drawable.roblox
                "cod" ->R.drawable.cod
                "among" ->R.drawable.amoung
                else -> R.drawable.mlbb
            },
            topUpOptions = emptyList()
        )
    }

    val filteredGames = games.filter { game ->
        val matchesSearch   = game.title.contains(searchQuery, ignoreCase = true)
        val matchesWishlist = if (isWishlistMode) favoriteGames.contains(game.id) else true
        val matchesCategory = if (selectedCategory == "Semua") true
        else game.genres.any { it.contains(selectedCategory, ignoreCase = true) }
        matchesSearch && matchesWishlist && matchesCategory
    }.let { list ->
        when (sortOption) {
            "A-Z"              -> list.sortedBy { it.title }
            "Z-A"             -> list.sortedByDescending { it.title }
            "Rating Tertinggi" -> list.sortedByDescending { it.rating }
            else              -> list
        }
    }

    if (showSortDialog) {
        val radioOptions = listOf("A-Z", "Z-A", "Rating Tertinggi")
        AlertDialog(
            onDismissRequest = { showSortDialog = false },
            title = { Text("Urutkan Katalog", fontWeight = FontWeight.Bold) },
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

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if (isWishlistMode) "WISHLIST SAYA" else "NEXUS STORE",
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.5.sp,
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                actions = {
                    IconButton(onClick = { isWishlistMode = !isWishlistMode }) {
                        Icon(
                            imageVector = if (isWishlistMode) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                            tint = if (isWishlistMode) Color(0xFFE91E63) else MaterialTheme.colorScheme.onSurface,
                            contentDescription = "Wishlist Toggle"
                        )
                    }

                    Box {
                        IconButton(onClick = { showDropdownMenu = true }) {
                            Icon(Icons.Rounded.MoreVert, contentDescription = "Menu Utilities")
                        }

                        DropdownMenu(
                            expanded        = showDropdownMenu,
                            onDismissRequest = { showDropdownMenu = false },
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                                .width(220.dp)
                        ) {
                            MenuSectionLabel("Fitur NEXUS")
                            NexusMenuItem(
                                icon     = Icons.Rounded.Calculate,
                                label    = "Kalkulator Win Rate",
                                onClick  = { showDropdownMenu = false; backStack.add(Route.KalkulatorWinRate) }
                            )
                            NexusMenuItem(
                                icon    = Icons.AutoMirrored.Rounded.List,
                                label   = "Urutkan Katalog",
                                onClick = { showDropdownMenu = false; showSortDialog = true }
                            )
                            HorizontalDivider(
                                modifier  = Modifier.padding(vertical = 6.dp),
                                color     = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                            )
                            MenuSectionLabel("Akun Pengguna")
                            NexusMenuItem(
                                icon    = Icons.Rounded.AccountCircle,
                                label   = "Profil Pengguna",
                                onClick = { showDropdownMenu = false; onNavigateToProfile() }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent // Borderless blend transparan yang bersih
                )
            )
        },
        bottomBar = {
            BottomNavBar(current = BottomNavDestination.GAME) { dest ->
                when (dest) {
                    BottomNavDestination.GAME          -> { }
                    BottomNavDestination.CEK_TRANSAKSI -> backStack.add(Route.CekTransaksi)
                    BottomNavDestination.NEXUS_COIN    -> backStack.add(Route.NexusCoinHistory)
                    BottomNavDestination.DASHBOARD     -> onNavigateToDashboard()
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
                            MaterialTheme.colorScheme.surface
                        )
                    )
                )
        ) {
            // Immersive Greeting Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Selamat Datang, $username!",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "Temukan Game Favoritmu Sekarang.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // High Fidelity Search Bar Component
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 4.dp),
                placeholder = { Text("Cari judul game atau developer...") },
                leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Rounded.Cancel, contentDescription = "Clear Input Text")
                        }
                    }
                },
                shape = CircleShape,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                ),
                singleLine = true
            )

            // Dynamic Category Filter System
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding        = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                modifier              = Modifier.fillMaxWidth()
            ) {
                items(categories) { category ->
                    val isSelected = selectedCategory == category
                    FilterChip(
                        selected = isSelected,
                        onClick  = { selectedCategory = category },
                        label    = { Text(category, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                        colors   = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor     = MaterialTheme.colorScheme.onPrimary,
                            containerColor         = MaterialTheme.colorScheme.surfaceContainerLow // Parameter yang sudah diperbaiki
                        ),
                        shape = CircleShape,
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                            selectedBorderColor = Color.Transparent
                        )
                    )
                }
            }

            // Real-Time Content Lazy List Wrapper
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding      = PaddingValues(start = 20.dp, end = 20.dp, bottom = 24.dp),
                modifier            = Modifier.fillMaxSize()
            ) {
                if (apiGames.isEmpty() && searchQuery.isBlank()) {
                    items(6) { ShimmerGameListItem() }
                }
                else if (filteredGames.isEmpty()) {
                    item {
                        Column(
                            modifier            = Modifier
                                .fillParentMaxSize()
                                .padding(40.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            val isWishlistEmpty = isWishlistMode && favoriteGames.isEmpty()
                            Icon(
                                imageVector = if (isWishlistEmpty) Icons.Rounded.HeartBroken else Icons.Rounded.SearchOff,
                                contentDescription = null,
                                modifier           = Modifier.size(80.dp),
                                tint               = MaterialTheme.colorScheme.error.copy(alpha = 0.6f)
                            )
                            Spacer(Modifier.height(20.dp))
                            Text(
                                text       = if (isWishlistEmpty) "Wishlist Masih Kosong" else "Hasil Tidak Ditemukan",
                                style      = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text      = if (isWishlistEmpty)
                                    "Ketuk ikon hati pada item katalog game premium kami untuk menambahkannya ke folder koleksi pribadi Anda."
                                else
                                    "Coba periksa kembali ejaan kata kunci Anda atau beralihlah ke filter kategori katalog yang lain.",
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
                                onFavoriteToggle(game.id)
                                if (isFav) {
                                    coroutineScope.launch {
                                        snackbarHostState.currentSnackbarData?.dismiss()
                                        val result = snackbarHostState.showSnackbar(
                                            message = "${game.title} dilepas dari wishlist",
                                            actionLabel = "Batal Undo",
                                            duration = SnackbarDuration.Short
                                        )
                                        if (result == SnackbarResult.ActionPerformed) {
                                            onFavoriteToggle(game.id)
                                        }
                                    }
                                }
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

// ── Modifier Shimmer Engine (Zero Layout Shifting) ──────────────────────────

fun Modifier.shimmerEffect(): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "shimmer_root")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1200f,
        animationSpec = infiniteRepeatable(
            animation = tween(1300, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_interpolation"
    )

    val brush = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.6f),
            MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.9f),
            MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.6f)
        ),
        start = Offset(0f, 0f),
        end = Offset(translateAnim, translateAnim)
    )
    this.background(brush)
}

// ── High Fidelity Skeleton Loading Item Component ───────────────────────────

@Composable
fun ShimmerGameListItem() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .shimmerEffect()
            )
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Box(modifier = Modifier.fillMaxWidth(0.5f).height(20.dp).clip(CircleShape).shimmerEffect())
                Spacer(Modifier.height(6.dp))
                Box(modifier = Modifier.fillMaxWidth(0.3f).height(14.dp).clip(CircleShape).shimmerEffect())
                Spacer(Modifier.height(10.dp))
                Box(modifier = Modifier.fillMaxWidth(0.8f).height(12.dp).clip(CircleShape).shimmerEffect())
            }
            Spacer(Modifier.width(12.dp))
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.height(88.dp).padding(vertical = 4.dp)
            ) {
                Box(modifier = Modifier.size(28.dp).clip(CircleShape).shimmerEffect())
                Box(modifier = Modifier.size(20.dp).clip(CircleShape).shimmerEffect())
            }
        }
    }
}

@Composable
fun MenuSectionLabel(title: String) {
    Text(
        text     = title.uppercase(),
        fontSize = 10.sp,
        fontWeight = FontWeight.Black,
        letterSpacing = 1.sp,
        color    = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 14.dp, end = 14.dp, top = 10.dp, bottom = 4.dp)
    )
}

@Composable
fun NexusMenuItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        text = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = label,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        },
        onClick = onClick,
        modifier = Modifier.padding(horizontal = 4.dp)
    )
}

// ── Production-Grade Game Card List Item Component ──────────────────────────

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
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors    = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.7f) // Glassmorphism layer treatment
        ),
        shape = RoundedCornerShape(20.dp), // Premium Smooth Corner Radius
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier          = Modifier.padding(12.dp)
        ) {
            with(sharedTransitionScope) {
                Image(
                    painter            = painterResource(id = game.imageRes),
                    contentDescription = "Artwork ${game.title}",
                    contentScale       = ContentScale.Crop,
                    modifier           = Modifier
                        .size(88.dp) // Menggunakan structural budget yang pas untuk item list data kompleks
                        .clip(RoundedCornerShape(14.dp))
                        .sharedElement(
                            sharedContentState      = rememberSharedContentState(key = "image_${game.id}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                )
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                // Inline Title & Metadata Layout Section
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = game.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    // INTERNATIONAL UX FEATURE: Micro Badge Rating System
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f), CircleShape)
                            .padding(horizontal = 6.dp, vertical = 2.getDp())
                    ) {
                        Icon(Icons.Rounded.Star, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(12.dp))
                        Spacer(Modifier.width(2.dp))
                        Text(game.rating.toString(), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, fontSize = 10.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                }

                Text(
                    text = game.developer,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = game.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp
                )
            }

            Spacer(Modifier.width(12.dp))

            // Interactive Command Layout Side Panel Block
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.height(88.dp).padding(vertical = 4.dp)
            ) {
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                        tint        = if (isFavorite) Color(0xFFE91E63) else MaterialTheme.colorScheme.onSurfaceVariant,
                        contentDescription = "Bookmark",
                        modifier = Modifier.size(22.dp)
                    )
                }

                Icon(
                    imageVector = Icons.Rounded.ChevronRight,
                    contentDescription = "Navigate details",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// Inline helper extension function untuk mereduksi noise matematis LaTeX padding di Jetpack Compose
private fun Int.getDp() = this.dp