package com.example.listgame.ui.screen

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
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
                errorMsg = "Win rate saat ini harus 0–100."
                return
            }
            tr == null || tr < 0 || tr > 100 -> {
                errorMsg = "Target win rate harus 0–100."
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
            errorMsg = "Target 100% tidak dapat dicapai jika ada kekalahan."
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
        ) {
            // ── Banner Atas Gelap ─────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF1A1A1A), Color(0xFF252525))
                        )
                    )
                    .padding(horizontal = 24.dp, vertical = 36.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Kalkulator Win Rate",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize   = 26.sp,
                        color      = Color.White,
                        textAlign  = TextAlign.Center
                    )
                    Text(
                        "Digunakan untuk menghitung total jumlah pertandingan " +
                                "yang harus diambil untuk mencapai target tingkat " +
                                "kemenangan yang diinginkan.",
                        style     = MaterialTheme.typography.bodySmall,
                        color     = Color.White.copy(alpha = 0.65f),
                        textAlign = TextAlign.Center,
                        modifier  = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }

            // ── Form Input ────────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // Input 1
                WinRateInputField(
                    label         = "Total Pertandingan Kamu Saat Ini",
                    value         = totalMatch,
                    onValueChange = {
                        totalMatch = it
                        result     = null
                        errorMsg   = ""
                    },
                    placeholder = "Pertandingan saat ini"
                )

                // Input 2
                WinRateInputField(
                    label         = "Total Win Rate Kamu Saat Ini",
                    value         = currentWinRate,
                    onValueChange = {
                        currentWinRate = it
                        result         = null
                        errorMsg       = ""
                    },
                    placeholder = "Menang yang ingin dicapai"
                )

                // Input 3
                WinRateInputField(
                    label         = "Win Rate Total yang Kamu Inginkan",
                    value         = targetWinRate,
                    onValueChange = {
                        targetWinRate = it
                        result        = null
                        errorMsg      = ""
                    },
                    placeholder = "Winrate"
                )

                // ── Pesan error ───────────────────────────────────────────
                if (errorMsg.isNotEmpty()) {
                    Card(
                        shape  = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Row(
                            modifier              = Modifier.padding(12.dp),
                            verticalAlignment     = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Rounded.Warning, null,
                                tint     = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                errorMsg,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }

                // ✅ Tombol Hitung — tengah layar, tanpa Pesan Joki
                Button(
                    onClick  = { calculate() },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(0.6f)   // lebar 60% layar agar terkesan terpusat
                        .height(52.dp),
                    shape  = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor         = Color(0xFFFF6B00),
                        disabledContainerColor = Color(0xFFFF6B00).copy(alpha = 0.4f)
                    ),
                    enabled = totalMatch.isNotBlank() &&
                            currentWinRate.isNotBlank() &&
                            targetWinRate.isNotBlank()
                ) {
                    Icon(
                        Icons.Rounded.Calculate, null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Hitung",
                        fontWeight = FontWeight.Bold,
                        fontSize   = 16.sp
                    )
                }

                // ── Hasil Kalkulasi ───────────────────────────────────────
                if (result != null) {
                    val r = result!!

                    // Kotak hasil utama
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(vertical = 20.dp, horizontal = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    SpanStyle(
                                        color      = MaterialTheme.colorScheme.onSurface,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize   = 14.sp
                                    )
                                ) { append("YOU NEED ABOUT ") }

                                withStyle(
                                    SpanStyle(
                                        color      = Color(0xFFFF6B00),
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize   = 14.sp
                                    )
                                ) { append("${r.gamesNeeded} WIN WITHOUT LOSE") }

                                withStyle(
                                    SpanStyle(
                                        color      = MaterialTheme.colorScheme.onSurface,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize   = 14.sp
                                    )
                                ) { append(" TO GET A ") }

                                withStyle(
                                    SpanStyle(
                                        color      = Color(0xFFFF6B00),
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize   = 14.sp
                                    )
                                ) { append("${r.targetWr}% WIN RATE") }

                                withStyle(
                                    SpanStyle(
                                        color      = MaterialTheme.colorScheme.onSurface,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize   = 14.sp
                                    )
                                ) { append(".") }
                            },
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Rincian kalkulasi
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape    = RoundedCornerShape(12.dp),
                        colors   = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(
                            modifier            = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                "Ringkasan Kalkulasi",
                                fontWeight = FontWeight.Bold,
                                style      = MaterialTheme.typography.labelLarge,
                                color      = MaterialTheme.colorScheme.primary
                            )
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.outlineVariant
                            )
                            WrDetailRow(
                                "Pertandingan saat ini",
                                totalMatch
                            )
                            WrDetailRow(
                                "Win rate saat ini",
                                "$currentWinRate%"
                            )
                            WrDetailRow(
                                "Target win rate",
                                "$targetWinRate%"
                            )
                            WrDetailRow(
                                "Kemenangan beruntun dibutuhkan",
                                "${r.gamesNeeded}"
                            )
                            WrDetailRow(
                                "Total pertandingan setelah itu",
                                "${r.totalAfter}"
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
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

// ── Helper: Input Field ───────────────────────────────────────────────────────

@Composable
private fun WinRateInputField(
    label        : String,
    value        : String,
    onValueChange: (String) -> Unit,
    placeholder  : String
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            label,
            fontWeight = FontWeight.SemiBold,
            style      = MaterialTheme.typography.bodyMedium,
            color      = MaterialTheme.colorScheme.onSurface
        )
        OutlinedTextField(
            value         = value,
            onValueChange = onValueChange,
            placeholder   = {
                Text(placeholder,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            },
            singleLine      = true,
            modifier        = Modifier.fillMaxWidth(),
            shape           = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFF6B00)
            )
        )
    }
}

// ── Helper: Baris Detail ──────────────────────────────────────────────────────

@Composable
private fun WrDetailRow(label: String, value: String) {
    Row(
        modifier              = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            style    = MaterialTheme.typography.bodySmall,
            color    = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1.5f)
        )
        Text(
            value,
            style      = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color      = MaterialTheme.colorScheme.onSurface,
            textAlign  = TextAlign.End,
            modifier   = Modifier.weight(1f)
        )
    }
}