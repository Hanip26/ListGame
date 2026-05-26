package com.example.listgame.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ExitToApp
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listgame.model.Transaction
import com.example.listgame.model.TransactionStatus
import com.example.listgame.navigation.LocalBackStack
import com.example.listgame.navigation.Route
import com.example.listgame.viewmodel.AppViewModel
import com.example.listgame.viewmodel.AuthViewModel
import java.text.SimpleDateFormat
import java.util.*

private val ColorWaiting  = Color(0xFFB5970B)
private val ColorProcess  = Color(0xFF1565C0)
private val ColorSuccess  = Color(0xFF2E7D32)
private val ColorFailed   = Color(0xFF880E4F)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    authViewModel      : AuthViewModel,
    appViewModel       : AppViewModel,           // ← tambah untuk baca transaksi
    onNavigateToProfile: () -> Unit,
    onLogout           : () -> Unit
) {
    val backStack    = LocalBackStack.current
    val user         by authViewModel.currentUser.collectAsState()
    val transactions by appViewModel.transactions.collectAsState()   // ← Flow transaksi

    // Statistik dihitung dari data nyata
    val totalTrx      = transactions.size
    val totalRevenue  = transactions.filter { it.status == TransactionStatus.SUCCESS }
        .sumOf { it.totalPrice }
    val countWaiting  = transactions.count { it.status == TransactionStatus.PENDING }
    val countProcess  = transactions.count { it.status == TransactionStatus.PROCESSING }
    val countSuccess  = transactions.count { it.status == TransactionStatus.SUCCESS }
    val countFailed   = transactions.count { it.status == TransactionStatus.FAILED }

    // Hanya tampilkan 5 transaksi terbaru di dashboard
    val recentTrx = transactions.take(5)

    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title  = { Text("Konfirmasi Logout") },
            text   = { Text("Yakin ingin keluar dari akun ini?") },
            confirmButton = {
                TextButton(onClick = { showLogoutDialog = false; onLogout() }) {
                    Text("Keluar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = { TextButton(onClick = { showLogoutDialog = false }) { Text("Batal") } }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title   = { Text("Dashboard", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(Icons.Rounded.AccountCircle, "Profil")
                    }
                    IconButton(onClick = { showLogoutDialog = true }) {
                        Icon(Icons.AutoMirrored.Rounded.ExitToApp,
                            tint = MaterialTheme.colorScheme.error,
                            contentDescription = "Logout")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ── Banner keamanan ───────────────────────────────────────────
            Card(
                shape  = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1565C0))
            ) {
                Row(
                    modifier            = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment   = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Tingkatkan keamanan!",
                            fontWeight = FontWeight.Bold, color = Color.White,
                            style = MaterialTheme.typography.titleSmall)
                        Text("Gunakan fitur keamanan agar akun kamu lebih aman.",
                            color = Color.White.copy(alpha = 0.85f),
                            style = MaterialTheme.typography.bodySmall)
                    }
                    Icon(Icons.Rounded.Security, null,
                        tint = Color.White, modifier = Modifier.size(36.dp))
                }
            }

            // ── Profil + Saldo ────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Kartu profil
                Card(
                    modifier = Modifier.weight(1f),
                    shape    = RoundedCornerShape(16.dp),
                    colors   = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp).clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(
                                            listOf(
                                                MaterialTheme.colorScheme.primary,
                                                MaterialTheme.colorScheme.tertiary
                                            )
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text((user?.displayName ?: "?").take(1).uppercase(),
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize   = 20.sp, color = Color.White)
                            }
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(user?.displayName ?: "-",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleSmall,
                                    maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Surface(shape = RoundedCornerShape(4.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer) {
                                    Text("Member",
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.Email, null,
                                modifier = Modifier.size(14.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(Modifier.width(4.dp))
                            Text(user?.email ?: "-",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                        Spacer(Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.Phone, null,
                                modifier = Modifier.size(14.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(Modifier.width(4.dp))
                            Text(if (user?.phone.isNullOrBlank()) "---" else user!!.phone,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Spacer(Modifier.height(12.dp))
                        OutlinedButton(onClick = onNavigateToProfile,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()) {
                            Icon(Icons.Rounded.Edit, null, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Edit Profil", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }

                // Kartu saldo
                Card(
                    modifier = Modifier.weight(1f),
                    shape    = RoundedCornerShape(16.dp),
                    colors   = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.Paid, null,
                                tint = Color(0xFFFFA726), modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("NEXUS Coins",
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        Spacer(Modifier.height(16.dp))
                        Text("0", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFFFA726))
                        Text("NEXUS Coins",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.7f))
                        Spacer(Modifier.height(16.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(onClick = {}, shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f).height(36.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA726))
                            ) { Text("Top Up", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black) }
                            OutlinedButton(onClick = {}, shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f).height(36.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                            ) { Text("Redeem", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                        }
                    }
                }
            }

            // ── Statistik transaksi ───────────────────────────────────────
            Text("Transaksi Hari Ini",
                style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(Modifier.weight(1f), "Total Transaksi", totalTrx.toString(),
                    MaterialTheme.colorScheme.surfaceVariant)
                StatCard(Modifier.weight(1f), "Total Pendapatan",
                    "Rp ${"%,d".format(totalRevenue).replace(',', '.')}",
                    MaterialTheme.colorScheme.surfaceVariant)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatusCard(Modifier.weight(1f), countWaiting.toString(),  "Menunggu",     ColorWaiting)
                StatusCard(Modifier.weight(1f), countProcess.toString(),  "Dalam Proses", ColorProcess)
                StatusCard(Modifier.weight(1f), countSuccess.toString(),  "Sukses",       ColorSuccess)
                StatusCard(Modifier.weight(1f), countFailed.toString(),   "Gagal",        ColorFailed)
            }

            // ── Riwayat Transaksi — DATA NYATA ────────────────────────────
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text("Riwayat Transaksi Terbaru",
                    style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
                if (transactions.size > 5) {
                    TextButton(onClick = { /* TODO: halaman semua transaksi */ }) {
                        Text("Lihat Semua", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }

            Card(
                shape  = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Header tabel
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                    ) {
                        listOf("Invoice", "Item", "Total", "Status").forEachIndexed { i, h ->
                            Text(h,
                                modifier   = Modifier.weight(if (i == 0) 1.6f else 1f),
                                fontWeight = FontWeight.Bold,
                                style      = MaterialTheme.typography.labelSmall,
                                color      = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    HorizontalDivider()

                    if (recentTrx.isEmpty()) {
                        // Empty state
                        Column(
                            modifier            = Modifier.fillMaxWidth().padding(40.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Rounded.BarChart, null,
                                modifier = Modifier.size(52.dp),
                                tint     = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f))
                            Spacer(Modifier.height(12.dp))
                            Text("Data tidak ditemukan!",
                                fontWeight = FontWeight.Bold,
                                color      = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("Belum ada transaksi yang tercatat.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f))
                        }
                    } else {
                        // Baris data transaksi
                        recentTrx.forEach { trx ->
                            TransactionRow(trx)
                            HorizontalDivider(thickness = 0.5.dp,
                                color = MaterialTheme.colorScheme.outlineVariant)
                        }
                    }
                }
            }

            // ── Menu cepat ────────────────────────────────────────────────
            Text("Menu", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickMenuCard(Modifier.weight(1f), Icons.Rounded.Games, "Game",
                    MaterialTheme.colorScheme.primaryContainer) {
                    backStack.removeLastOrNull()
                }
                QuickMenuCard(Modifier.weight(1f), Icons.Rounded.Favorite, "Wishlist",
                    MaterialTheme.colorScheme.secondaryContainer) {
                    backStack.removeLastOrNull()
                }
                QuickMenuCard(Modifier.weight(1f), Icons.Rounded.Person, "Profil",
                    MaterialTheme.colorScheme.tertiaryContainer) {
                    onNavigateToProfile()
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

// ── Baris transaksi ───────────────────────────────────────────────────────────
@Composable
private fun TransactionRow(trx: Transaction) {
    val fmt = SimpleDateFormat("dd/MM HH:mm", Locale.getDefault())
    val dateStr = fmt.format(Date(trx.createdAt))

    Row(
        modifier          = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1.6f)) {
            Text(trx.invoiceId.take(14) + "…",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1)
            Text(dateStr,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Text(trx.item, modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.labelSmall,
            maxLines = 2, overflow = TextOverflow.Ellipsis)
        Text("Rp ${"%,d".format(trx.totalPrice).replace(',','.')}",
            modifier   = Modifier.weight(1f),
            style      = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold)
        // Badge status
        val (badgeColor, badgeText) = when (trx.status) {
            TransactionStatus.PENDING    -> ColorWaiting to "Menunggu"
            TransactionStatus.PROCESSING -> ColorProcess to "Proses"
            TransactionStatus.SUCCESS    -> ColorSuccess to "Sukses"
            TransactionStatus.FAILED     -> ColorFailed  to "Gagal"
        }
        Surface(shape  = RoundedCornerShape(6.dp),
            color  = badgeColor.copy(alpha = 0.15f),
            modifier = Modifier.weight(1f)) {
            Text(badgeText,
                modifier   = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                style      = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color      = badgeColor)
        }
    }
}

// ── Helper composables ────────────────────────────────────────────────────────

@Composable
private fun StatCard(modifier: Modifier, label: String, value: String, bgColor: Color) {
    Card(modifier = modifier, shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold,
                maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(label, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun StatusCard(modifier: Modifier, value: String, label: String, color: Color) {
    Card(modifier = modifier, shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = color)) {
        Column(modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
            Spacer(Modifier.height(4.dp))
            Text(label, style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.9f), textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun QuickMenuCard(
    modifier: Modifier, icon: ImageVector, label: String,
    bgColor: Color, onClick: () -> Unit
) {
    Card(modifier = modifier.clickable { onClick() }, shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)) {
        Column(modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, null, modifier = Modifier.size(28.dp),
                tint = MaterialTheme.colorScheme.onSurface)
            Spacer(Modifier.height(6.dp))
            Text(label, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
        }
    }
}