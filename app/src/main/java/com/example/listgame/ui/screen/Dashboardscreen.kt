package com.example.listgame.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listgame.model.Transaction
import com.example.listgame.model.TransactionStatus
import com.example.listgame.navigation.LocalBackStack
import com.example.listgame.navigation.Route
import com.example.listgame.ui.components.BottomNavBar
import com.example.listgame.ui.components.BottomNavDestination
import com.example.listgame.viewmodel.AppViewModel
import com.example.listgame.viewmodel.AuthViewModel
import java.text.SimpleDateFormat
import java.util.*

private val ColorWaiting  = Color(0xFFD4AF37)
private val ColorProcess  = Color(0xFF1976D2)
private val ColorSuccess  = Color(0xFF388E3C)
private val ColorFailed   = Color(0xFFC2185B)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    authViewModel      : AuthViewModel,
    appViewModel       : AppViewModel,
    onNavigateToProfile: () -> Unit,
    onLogout           : () -> Unit,
    onNavigateToGame   : () -> Unit = {},
    onNavigateToCek    : () -> Unit = {}
) {
    val backStack        = LocalBackStack.current
    val user             by authViewModel.currentUser.collectAsState()
    val transactions     by appViewModel.transactions.collectAsState()
    val nexusCoinBalance by appViewModel.nexusCoinBalance.collectAsState()

    val totalTrx     = transactions.size
    val totalRevenue = transactions.filter { it.status == TransactionStatus.SUCCESS }
        .sumOf { it.totalPrice }
    val countWaiting = transactions.count { it.status == TransactionStatus.PENDING }
    val countProcess = transactions.count { it.status == TransactionStatus.PROCESSING }
    val countSuccess = transactions.count { it.status == TransactionStatus.SUCCESS }
    val countFailed  = transactions.count { it.status == TransactionStatus.FAILED }

    val recentTrx = transactions.take(5)

    var showTierDialog by remember { mutableStateOf(false) }

    val userTier = when {
        totalRevenue <= 100000  -> "Member Junior"
        totalRevenue <= 500000  -> "Member Senior"
        totalRevenue <= 1000000 -> "Member Terhormat"
        totalRevenue <= 2000000 -> "Member Juragan"
        else                    -> "Sultan Tier Member"
    }

    val userTierColor = when {
        totalRevenue <= 100000  -> Color(0xFF00E676)
        totalRevenue <= 500000  -> Color(0xFF00B0FF)
        totalRevenue <= 1000000 -> Color(0xFFE040FB)
        totalRevenue <= 2000000 -> Color(0xFFFF9100)
        else                    -> Color(0xFFFF1744)
    }

    if (showTierDialog) {
        AlertDialog(
            onDismissRequest = { showTierDialog = false },
            confirmButton = {
                TextButton(onClick = { showTierDialog = false }) {
                    Text("Tutup", fontWeight = FontWeight.Bold)
                }
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.MilitaryTech, null,
                        tint = userTierColor, modifier = Modifier.size(28.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Tingkatan Loyalitas Member",
                        fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        "Total Pembelian Kamu Saat Ini:\nRp ${"%,d".format(totalRevenue).replace(',', '.')}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = userTierColor
                    )
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )
                    TierItem("1. Member Junior",    "Rp 0 - Rp 100.000",           userTier == "Member Junior",    Color(0xFF00E676))
                    TierItem("2. Member Senior",    "Rp 100.000 - Rp 500.000",     userTier == "Member Senior",    Color(0xFF00B0FF))
                    TierItem("3. Member Terhormat", "Rp 500.000 - Rp 1.000.000",   userTier == "Member Terhormat", Color(0xFFE040FB))
                    TierItem("4. Member Juragan",   "Rp 1.000.000 - Rp 2.000.000", userTier == "Member Juragan",   Color(0xFFFF9100))
                    TierItem("5. Sultan Tier Member","Di atas Rp 2.000.000",        userTier == "Sultan Tier Member", Color(0xFFFF1744))
                }
            },
            shape = RoundedCornerShape(24.dp),
            containerColor = MaterialTheme.colorScheme.surface
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title   = { Text("Dashboard", fontWeight = FontWeight.Bold) },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onNavigateToProfile() }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
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
                            Text(
                                (user?.displayName ?: "?").take(1).uppercase(),
                                fontWeight = FontWeight.ExtraBold,
                                fontSize   = 16.sp,
                                color      = Color.White
                            )
                        }
                        Spacer(Modifier.width(8.dp))
                        Column(
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                user?.displayName ?: "-",
                                style     = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color     = MaterialTheme.colorScheme.onSurface,
                                maxLines  = 1,
                                overflow  = TextOverflow.Ellipsis
                            )
                            Text(
                                userTier,
                                style      = MaterialTheme.typography.labelSmall,
                                color      = userTierColor,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize   = 10.sp,
                                modifier   = Modifier.clickable { showTierDialog = true }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            BottomNavBar(current = BottomNavDestination.DASHBOARD) { dest ->
                when (dest) {
                    BottomNavDestination.GAME          -> onNavigateToGame()
                    BottomNavDestination.CEK_TRANSAKSI -> onNavigateToCek()
                    BottomNavDestination.NEXUS_COIN    -> backStack.add(Route.NexusCoinHistory)
                    BottomNavDestination.DASHBOARD     -> { }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier  = Modifier.weight(1f).fillMaxHeight(),
                    shape     = RoundedCornerShape(20.dp),
                    colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp).fillMaxHeight()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp).clip(CircleShape)
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
                                    fontSize   = 18.sp, color = Color.White)
                            }
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(user?.displayName ?: "-",
                                    fontWeight = FontWeight.Bold,
                                    style      = MaterialTheme.typography.titleSmall,
                                    maxLines   = 1, overflow = TextOverflow.Ellipsis)
                                Text(
                                    userTier,
                                    style      = MaterialTheme.typography.labelSmall,
                                    color      = userTierColor,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize   = 10.sp,
                                    modifier   = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .clickable { showTierDialog = true }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color    = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        )
                        OutlinedButton(
                            onClick        = onNavigateToProfile,
                            shape          = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                            modifier       = Modifier.fillMaxWidth().height(36.dp)
                        ) {
                            Icon(Icons.Rounded.Edit, null, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Edit Profil", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }

                Card(
                    modifier  = Modifier.weight(1f).fillMaxHeight(),
                    shape     = RoundedCornerShape(20.dp),
                    colors    = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp).fillMaxHeight()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.Stars, null,
                                tint = Color(0xFFFFD700), modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("NEXUS Coins",
                                style      = MaterialTheme.typography.labelMedium,
                                color      = Color.White.copy(alpha = 0.9f),
                                fontWeight = FontWeight.SemiBold)
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            "%,d".format(nexusCoinBalance).replace(",", "."),
                            fontSize   = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color      = Color(0xFFFFD700)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick        = { backStack.add(Route.NexusCoinTopUp) },
                                shape          = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(0.dp),
                                modifier       = Modifier.weight(1f).height(36.dp),
                                colors         = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700))
                            ) {
                                Icon(Icons.Rounded.Add, null,
                                    modifier = Modifier.size(16.dp), tint = Color(0xFF1A1A2E))
                                Spacer(Modifier.width(4.dp))
                                Text("Top Up", fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold, color = Color(0xFF1A1A2E))
                            }
                            OutlinedButton(
                                onClick        = { backStack.add(Route.NexusCoinHistory) },
                                shape          = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(0.dp),
                                modifier       = Modifier.weight(1f).height(36.dp),
                                colors         = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                                border         = androidx.compose.foundation.BorderStroke(
                                    1.dp, Color.White.copy(alpha = 0.5f)
                                )
                            ) {
                                Icon(Icons.Rounded.History, null, modifier = Modifier.size(14.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("Riwayat", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            TopUpChartCard(transactions)

            Text("Statistik Hari Ini",
                style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatCard(Modifier.weight(1f), "Total Transaksi", totalTrx.toString(),
                    MaterialTheme.colorScheme.surface)
                StatCard(Modifier.weight(1f), "Total Pembelian",
                    "Rp ${"%,d".format(totalRevenue).replace(',', '.')}",
                    MaterialTheme.colorScheme.surface)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatusCard(Modifier.weight(1f), countWaiting.toString(), "Menunggu", ColorWaiting)
                StatusCard(Modifier.weight(1f), countProcess.toString(), "Proses",   ColorProcess)
                StatusCard(Modifier.weight(1f), countSuccess.toString(), "Sukses",   ColorSuccess)
                StatusCard(Modifier.weight(1f), countFailed.toString(),  "Gagal",    ColorFailed)
            }

            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text("Transaksi Terbaru",
                    style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
                if (transactions.size > 5) {
                    Text("Lihat Semua",
                        style      = MaterialTheme.typography.labelMedium,
                        color      = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier   = Modifier.clickable { }.padding(4.dp))
                }
            }

            Card(
                shape     = RoundedCornerShape(20.dp),
                colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                    if (recentTrx.isEmpty()) {
                        Column(
                            modifier            = Modifier.fillMaxWidth().padding(40.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Rounded.Inbox, null,
                                modifier = Modifier.size(64.dp),
                                tint     = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f))
                            Spacer(Modifier.height(16.dp))
                            Text("Belum Ada Transaksi", fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface)
                            Text("Transaksi terbarumu akan muncul di sini.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    } else {
                        recentTrx.forEachIndexed { index, trx ->
                            TransactionRow(trx)
                            if (index < recentTrx.size - 1) {
                                HorizontalDivider(
                                    modifier  = Modifier.padding(horizontal = 16.dp),
                                    thickness = 1.dp,
                                    color     = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

// ── Helper composables ────────────────────────────────────────────────────────

@Composable
private fun TierItem(name: String, range: String, isActive: Boolean, tierColor: Color) {
    val bg        = if (isActive) tierColor.copy(alpha = 0.15f) else Color.Transparent
    val textColor = if (isActive) tierColor else MaterialTheme.colorScheme.onSurface
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(bg, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Column {
            Text(name, fontWeight = if (isActive) FontWeight.ExtraBold else FontWeight.SemiBold,
                color = textColor, fontSize = 14.sp)
            Text(range, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        if (isActive) {
            Icon(Icons.Rounded.CheckCircle, null, tint = tierColor, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
private fun TransactionRow(trx: Transaction) {
    val fmt          = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault())
    val dateStr      = fmt.format(Date(trx.createdAt))
    val clipboardManager = LocalClipboardManager.current
    val context      = LocalContext.current

    Row(
        modifier          = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1.5f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .clickable {
                        clipboardManager.setText(AnnotatedString(trx.invoiceId))
                        Toast.makeText(context, "Invoice disalin!", Toast.LENGTH_SHORT).show()
                    }
                    .padding(vertical = 4.dp, horizontal = 2.dp)
            ) {
                Text(trx.invoiceId.take(10) + "…",
                    style      = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color      = MaterialTheme.colorScheme.primary,
                    maxLines   = 1)
                Spacer(Modifier.width(6.dp))
                Icon(Icons.Rounded.ContentCopy, null,
                    tint     = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                    modifier = Modifier.size(14.dp))
            }
            Text(dateStr,
                style    = MaterialTheme.typography.bodySmall,
                fontSize = 11.sp,
                color    = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        Column(modifier = Modifier.weight(1.2f)) {
            Text(trx.item,
                style      = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines   = 1, overflow = TextOverflow.Ellipsis)
            Spacer(Modifier.height(4.dp))
            Text("Rp ${"%,d".format(trx.totalPrice).replace(',', '.')}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        val (badgeColor, badgeText) = when (trx.status) {
            TransactionStatus.PENDING    -> ColorWaiting to "Menunggu"
            TransactionStatus.PROCESSING -> ColorProcess to "Proses"
            TransactionStatus.SUCCESS    -> ColorSuccess to "Sukses"
            TransactionStatus.FAILED     -> ColorFailed  to "Gagal"
        }

        Box(
            modifier = Modifier
                .weight(0.9f)
                .background(badgeColor.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(badgeText,
                style      = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color      = badgeColor)
        }
    }
}

@Composable
private fun TopUpChartCard(transactions: List<Transaction>) {
    val months = listOf("Jan","Feb","Mar","Apr","Mei","Jun","Jul","Ags","Sep","Okt","Nov","Des")
    val monthlyData = remember(transactions) {
        val data = FloatArray(12) { 0f }
        val cal  = Calendar.getInstance()
        transactions.filter { it.status == TransactionStatus.SUCCESS }.forEach {
            cal.timeInMillis = it.createdAt
            data[cal.get(Calendar.MONTH)] += it.totalPrice.toFloat()
        }
        data
    }
    val maxVal = monthlyData.maxOrNull()?.takeIf { it > 0 } ?: 1f

    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(20.dp),
        colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Grafik Transaksi Tahunan",
                style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(24.dp))
            Box(modifier = Modifier.fillMaxWidth().height(140.dp)) {
                Column(modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween) {
                    repeat(4) {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                            thickness = 1.dp)
                    }
                }
                Row(modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.Bottom) {
                    monthlyData.forEachIndexed { index, value ->
                        val ratio = value / maxVal
                        Column(horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement    = Arrangement.Bottom,
                            modifier               = Modifier.weight(1f)) {
                            Box(modifier = Modifier
                                .fillMaxWidth(0.55f)
                                .fillMaxHeight(ratio.coerceAtLeast(0.08f))
                                .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                .background(
                                    Brush.verticalGradient(listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                                    ))
                                ))
                            Spacer(Modifier.height(8.dp))
                            Text(months[index], fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCard(modifier: Modifier, label: String, value: String, bgColor: Color) {
    Card(modifier = modifier, shape = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.Start) {
            Text(label, style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(8.dp))
            Text(value, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold,
                color    = MaterialTheme.colorScheme.onSurface,
                maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
private fun StatusCard(modifier: Modifier, value: String, label: String, color: Color) {
    Card(modifier = modifier, shape = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.08f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)) {
        Column(modifier = Modifier.padding(vertical = 16.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = color)
            Spacer(Modifier.height(6.dp))
            Text(label, style = MaterialTheme.typography.labelSmall,
                color = color, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
        }
    }
}