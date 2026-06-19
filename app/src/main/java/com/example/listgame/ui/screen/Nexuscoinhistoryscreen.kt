package com.example.listgame.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listgame.model.NexusCoinTransaction
import com.example.listgame.model.NexusCoinTransactionStatus
import com.example.listgame.navigation.LocalBackStack
import com.example.listgame.navigation.Route
import com.example.listgame.viewmodel.AppViewModel
import java.text.SimpleDateFormat
import java.util.*

private val NexusGold = Color(0xFFFFA726)
private val NexusDark = Color(0xFF1A1A2E)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NexusCoinHistoryScreen(appViewModel: AppViewModel) {
    val backStack         = LocalBackStack.current
    val balance           by appViewModel.nexusCoinBalance.collectAsState()
    val history           by appViewModel.nexusCoinHistory.collectAsState()

    // Filter: Semua / Berhasil / Gagal
    var filterStatus by remember { mutableStateOf("Semua") }
    val filterOptions = listOf("Semua", "Berhasil", "Gagal")

    val filteredHistory = when (filterStatus) {
        "Berhasil" -> history.filter { it.status == NexusCoinTransactionStatus.SUCCESS }
        "Gagal"    -> history.filter { it.status == NexusCoinTransactionStatus.FAILED  }
        else       -> history
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.History, null,
                            tint = NexusGold, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("NEXUS Coins", fontWeight = FontWeight.Bold)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { backStack.removeLastOrNull() }) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding      = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ── Header Saldo & tombol Top Up ──────────────────────────────
            item {
                Card(
                    shape  = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = NexusDark)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.Paid, null,
                                tint = NexusGold, modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("NEXUS Coins",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelLarge)
                        }
                        Spacer(Modifier.height(12.dp))
                        Text(
                            "%,d".format(balance).replace(',', '.'),
                            fontSize   = 36.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color      = NexusGold
                        )
                        Text("NEXUS Coins tersedia",
                            color = Color.White.copy(alpha = 0.65f),
                            style = MaterialTheme.typography.bodySmall)
                        Spacer(Modifier.height(16.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Button(
                                onClick  = { backStack.add(Route.NexusCoinTopUp) },
                                shape    = RoundedCornerShape(10.dp),
                                colors   = ButtonDefaults.buttonColors(containerColor = NexusGold),
                                modifier = Modifier.weight(1f).height(42.dp)
                            ) {
                                Icon(Icons.Rounded.AddCircle, null,
                                    modifier = Modifier.size(16.dp), tint = Color.Black)
                                Spacer(Modifier.width(4.dp))
                                Text("Top Up", fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                            OutlinedButton(
                                onClick  = { backStack.add(Route.NexusCoinRedeem) },
                                shape    = RoundedCornerShape(10.dp),
                                colors   = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                                modifier = Modifier.weight(1f).height(42.dp)
                            ) {
                                Icon(Icons.Rounded.Redeem, null, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("Redeem", fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                        Text(
                            "Note: Setelah pembelian, NEXUS Coins akan langsung ditambahkan ke akun kamu.",
                            color = Color.White.copy(alpha = 0.55f),
                            style = MaterialTheme.typography.bodySmall,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            // ── Filter Chip ───────────────────────────────────────────────
            item {
                Column {
                    Text("Riwayat",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold)
                    Text(
                        "Menampilkan data transaksi NEXUS Coins kamu.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        filterOptions.forEach { option ->
                            FilterChip(
                                selected = filterStatus == option,
                                onClick  = { filterStatus = option },
                                label    = { Text(option) },
                                colors   = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = NexusGold,
                                    selectedLabelColor     = Color.Black
                                )
                            )
                        }
                    }
                }
            }

            // ── Tabel header ──────────────────────────────────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text("Nomor Invoice",   Modifier.weight(2f),
                        style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    Text("Tanggal",         Modifier.weight(1.5f),
                        style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    Text("Harga",           Modifier.weight(1.5f),
                        style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    Text("Status",          Modifier.weight(1.2f),
                        style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End)
                }
            }

            // ── Daftar transaksi ──────────────────────────────────────────
            if (filteredHistory.isEmpty()) {
                item { NexusEmptyState() }
            } else {
                items(filteredHistory) { trx ->
                    NexusCoinHistoryRow(trx)
                }
                item {
                    Text(
                        "Menampilkan ${filteredHistory.size} dari ${history.size} hasil",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

// ── Baris riwayat ─────────────────────────────────────────────────────────────

@Composable
private fun NexusCoinHistoryRow(trx: NexusCoinTransaction) {
    val dateStr = remember(trx.createdAt) {
        SimpleDateFormat("dd/MM\nHH:mm", Locale("id")).format(Date(trx.createdAt))
    }
    fun fmtRp(v: Int) = "Rp ${ "%,d".format(v).replace(',', '.') }"

    val statusColor = when (trx.status) {
        NexusCoinTransactionStatus.SUCCESS -> Color(0xFF4CAF50)
        NexusCoinTransactionStatus.FAILED  -> Color(0xFFE53935)
        NexusCoinTransactionStatus.PENDING -> Color(0xFFFFA726)
    }
    val statusLabel = when (trx.status) {
        NexusCoinTransactionStatus.SUCCESS -> "Berhasil"
        NexusCoinTransactionStatus.FAILED  -> "Gagal"
        NexusCoinTransactionStatus.PENDING -> "Pending"
    }

    Card(
        shape  = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(2f)) {
                Text(
                    trx.invoiceId.take(16) + if (trx.invoiceId.length > 16) "…" else "",
                    style     = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    trx.denomLabel,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    "+${ "%,d".format(trx.coinValue).replace(',', '.') } Coins",
                    style = MaterialTheme.typography.labelSmall,
                    color = NexusGold,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(dateStr,
                modifier  = Modifier.weight(1.5f),
                style     = MaterialTheme.typography.bodySmall,
                color     = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(fmtRp(trx.totalPrice),
                modifier  = Modifier.weight(1.5f),
                style     = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold)
            Box(modifier = Modifier.weight(1.2f), contentAlignment = Alignment.CenterEnd) {
                Text(
                    statusLabel,
                    style     = MaterialTheme.typography.labelSmall,
                    color     = statusColor,
                    fontWeight = FontWeight.Bold,
                    modifier  = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(statusColor.copy(alpha = 0.12f))
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                )
            }
        }
    }
}

// ── Empty state ───────────────────────────────────────────────────────────────

@Composable
private fun NexusEmptyState() {
    Column(
        modifier            = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(Icons.Rounded.BarChart, null,
            modifier = Modifier.size(56.dp),
            tint     = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f))
        Text("Data tidak ditemukan!",
            fontWeight = FontWeight.SemiBold,
            color      = MaterialTheme.colorScheme.onSurfaceVariant)
        Text("Tidak ada aktifitasi data.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.65f))
    }
}