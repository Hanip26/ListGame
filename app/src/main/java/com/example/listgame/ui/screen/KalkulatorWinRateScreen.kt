package com.example.listgame.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listgame.navigation.LocalBackStack
import kotlin.math.ceil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KalkulatorWinRateScreen() {
    val backStack = LocalBackStack.current

    var totalMatch     by remember { mutableStateOf("") }
    var currentWinRate by remember { mutableStateOf("") }
    var targetWinRate  by remember { mutableStateOf("") }
    var result         by remember { mutableStateOf<WinRateResult?>(null) }
    var errorMsg       by remember { mutableStateOf("") }

    // Mempertahankan brand color oranye dari kodemu sebelumnya
    val brandOrange = Color(0xFFFF6B00)

    fun calculate() {
        errorMsg = ""
        result   = null

        val n  = totalMatch.toIntOrNull()
        val wr = currentWinRate.toDoubleOrNull()
        val tr = targetWinRate.toDoubleOrNull()

        when {
            n == null || n <= 0 -> {
                errorMsg = "Total pertandingan harus angka positif."
                return
            }
            wr == null || wr < 0 || wr > 100 -> {
                errorMsg = "Win rate saat ini harus 0–100%."
                return
            }
            tr == null || tr < 0 || tr > 100 -> {
                errorMsg = "Target win rate harus 0–100%."
                return
            }
            tr <= wr -> {
                errorMsg = "Target win rate harus lebih tinggi dari saat ini."
                return
            }
        }

        val wins   = n!! * wr!! / 100.0
        val target = tr!!

        if (target >= 100.0) {
            errorMsg = "Target 100% tidak realistis jika ada kekalahan."
            return
        }

        val gamesNeeded = ceil(n * (target - wr) / (100.0 - target)).toInt()
        result = WinRateResult(
            gamesNeeded = gamesNeeded,
            targetWr    = target.toInt(),
            currentWins = wins.toInt(),
            totalAfter  = n + gamesNeeded
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kalkulator Win Rate", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { backStack.removeLastOrNull() }) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // ── Premium Header Banner ─────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF1E1E1E), Color(0xFF2C2C2C))
                        )
                    )
                    .padding(horizontal = 24.dp, vertical = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Insights,
                        contentDescription = null,
                        tint = brandOrange,
                        modifier = Modifier.size(48.dp)
                    )
                    Text(
                        "Kalkulator Winrate",
                        fontWeight = FontWeight.Black,
                        fontSize   = 24.sp,
                        color      = Color.White,
                        textAlign  = TextAlign.Center
                    )
                    Text(
                        "Ketahui berapa banyak kemenangan beruntun yang kamu butuhkan untuk mencapai win rate impianmu.",
                        style     = MaterialTheme.typography.bodyMedium,
                        color     = Color.White.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp,
                        modifier  = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ── Form Input Section ──────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                WinRateInputField(
                    label         = "Total Pertandingan Saat Ini",
                    value         = totalMatch,
                    onValueChange = { totalMatch = it; result = null; errorMsg = "" },
                    placeholder   = "Contoh: 150",
                    icon          = Icons.Rounded.SportsEsports,
                    brandColor    = brandOrange
                )

                WinRateInputField(
                    label         = "Win Rate Saat Ini (%)",
                    value         = currentWinRate,
                    onValueChange = { currentWinRate = it; result = null; errorMsg = "" },
                    placeholder   = "Contoh: 45.5",
                    icon          = Icons.Rounded.DataUsage,
                    brandColor    = brandOrange
                )

                WinRateInputField(
                    label         = "Target Win Rate (%)",
                    value         = targetWinRate,
                    onValueChange = { targetWinRate = it; result = null; errorMsg = "" },
                    placeholder   = "Contoh: 60",
                    icon          = Icons.Rounded.EmojiEvents,
                    brandColor    = brandOrange
                )

                // ── Animated Error Banner ─────────────────────────────────────
                AnimatedVisibility(
                    visible = errorMsg.isNotEmpty(),
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.errorContainer,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(Icons.Rounded.WarningAmber, null, tint = MaterialTheme.colorScheme.error)
                            Text(
                                errorMsg,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                // ── Action Button ─────────────────────────────────────────────
                Button(
                    onClick  = { calculate() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .shadow(if (totalMatch.isNotBlank()) 8.dp else 0.dp, RoundedCornerShape(16.dp), spotColor = brandOrange),
                    shape  = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor         = brandOrange,
                        disabledContainerColor = brandOrange.copy(alpha = 0.3f)
                    ),
                    enabled = totalMatch.isNotBlank() && currentWinRate.isNotBlank() && targetWinRate.isNotBlank()
                ) {
                    Icon(Icons.Rounded.AutoGraph, null, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Kalkulasi Sekarang", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                // ── Animated Result Section ───────────────────────────────────
                AnimatedVisibility(
                    visible = result != null,
                    enter = fadeIn(tween(500)) + expandVertically(tween(500)),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    if (result != null) {
                        val r = result!!
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                            // Hero Statistic Card
                            Surface(
                                color = brandOrange.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(20.dp),
                                border = null,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        "KAMU MEMBUTUHKAN",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = brandOrange,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.Bottom) {
                                        Text(
                                            text = "${r.gamesNeeded}",
                                            fontSize = 48.sp,
                                            fontWeight = FontWeight.Black,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = " WIN",
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.padding(bottom = 6.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "tanpa kalah untuk mencapai win rate ${r.targetWr}%.",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                            // Detail Breakdown Card
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape    = RoundedCornerShape(16.dp),
                                colors   = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(20.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Rounded.Analytics, null, tint = brandOrange, modifier = Modifier.size(20.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            "Ringkasan Statistik",
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                                    WrDetailRow("Pertandingan saat ini", totalMatch)
                                    WrDetailRow("Win rate saat ini", "$currentWinRate%")
                                    WrDetailRow("Target win rate", "$targetWinRate%")
                                    WrDetailRow("Total pertandingan nanti", "${r.totalAfter}")
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

// ── Data class hasil kalkulasi ────────────────────────────────────────────────
private data class WinRateResult(
    val gamesNeeded : Int,
    val targetWr    : Int,
    val currentWins : Int,
    val totalAfter  : Int
)

// ── Helper: Input Field Premium ───────────────────────────────────────────────
@Composable
private fun WinRateInputField(
    label        : String,
    value        : String,
    onValueChange: (String) -> Unit,
    placeholder  : String,
    icon         : ImageVector,
    brandColor   : Color
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = label,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        OutlinedTextField(
            value         = value,
            onValueChange = onValueChange,
            placeholder   = { Text(placeholder, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)) },
            leadingIcon   = { Icon(icon, contentDescription = null, tint = brandColor) },
            singleLine    = true,
            modifier      = Modifier.fillMaxWidth(),
            shape         = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = brandColor,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
        )
    }
}

// ── Helper: Baris Detail ──────────────────────────────────────────────────────
@Composable
private fun WrDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}