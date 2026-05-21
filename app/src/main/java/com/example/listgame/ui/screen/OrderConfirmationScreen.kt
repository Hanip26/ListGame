package com.example.listgame.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
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
import com.example.listgame.data.DummyData
import com.example.listgame.navigation.LocalBackStack
import com.example.listgame.navigation.Route

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderConfirmationScreen(route: Route.OrderConfirmation) {
    val backStack = LocalBackStack.current
    val game      = DummyData.popularGames.find { it.id == route.gameId }
    var agreedToTerms by remember { mutableStateOf(false) }

    fun formatRp(value: Int) = "Rp ${"%,d".format(value).replace(',', '.')}"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Konfirmasi Pesanan", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { backStack.removeLastOrNull() }) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        bottomBar = {
            Surface(shadowElevation = 8.dp) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // ✅ Checkbox S&K — rapi satu baris dengan wrap
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = agreedToTerms,
                            onCheckedChange = { agreedToTerms = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(0xFFFF6B00)
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        // Gunakan Text tunggal dengan AnnotatedString agar tidak terpotong
                        androidx.compose.ui.text.buildAnnotatedString {
                            append("Dengan mengklik ")
                        }.let {
                            Text(
                                buildString {
                                    append("Dengan mengklik Pesan Sekarang, kamu sudah menyetujui Syarat & Ketentuan yang berlaku.")
                                },
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 16.sp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Tombol aksi
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick  = { backStack.removeLastOrNull() },
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp),
                            shape    = RoundedCornerShape(14.dp)
                        ) {
                            Text("Batalkan", fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                if (agreedToTerms) {
                                    backStack.add(
                                        Route.PaymentProgress(
                                            gameId      = route.gameId,
                                            username    = route.username,    // ✅
                                            playerId    = route.playerId,    // ✅
                                            amount      = route.amount,
                                            quantity    = route.quantity,
                                            paymentName = route.paymentName,
                                            totalPrice  = route.totalPrice,
                                            subtotal    = route.subtotal,
                                            adminFee    = route.adminFee
                                        )
                                    )
                                }
                            },
                            enabled  = agreedToTerms,
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp),
                            shape    = RoundedCornerShape(14.dp),
                            colors   = ButtonDefaults.buttonColors(
                                containerColor         = Color(0xFFFF6B00),
                                disabledContainerColor = Color(0xFFFF6B00).copy(alpha = 0.38f)
                            )
                        ) {
                            Text("Pesan Sekarang!",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // ── Ikon centang hijau ────────────────────────────────────────
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4CAF50)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.Check, null,
                    tint = Color.White,
                    modifier = Modifier.size(48.dp))
            }

            // ── Judul & Subjudul ──────────────────────────────────────────
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text("Buat Pesanan",
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.headlineSmall)
                Text(
                    "Pastikan data akun Kamu dan produk yang Kamu pilih valid dan sesuai.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }

            // ── Tabel Detail Pesanan ──────────────────────────────────────
            Card(
                modifier  = Modifier.fillMaxWidth(),
                shape     = RoundedCornerShape(16.dp),
                colors    = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column {
                    // ✅ Username & ID dipisah
                    ConfirmRow("Username", route.username)
                    ConfirmDivider()
                    ConfirmRow("ID", route.playerId)
                    ConfirmDivider()
                    ConfirmRow("Item", "${route.amount} (x${route.quantity})")
                    ConfirmDivider()
                    ConfirmRow("Produk", game?.title ?: "-",
                        valueWeight = FontWeight.Bold)
                    ConfirmDivider()
                    ConfirmRow("Payment", route.paymentName,
                        valueWeight = FontWeight.Bold)
                    ConfirmDivider()
                    if (route.discountAmount > 0) {
                        ConfirmRow(
                            label      = "Diskon ${route.promoDiscount}%",
                            value      = "- ${formatRp(route.discountAmount)}",
                            valueColor = Color(0xFF4CAF50)
                        )
                        ConfirmDivider()
                    }
                    ConfirmRow(
                        label = "Biaya Admin",
                        value = if (route.adminFee == 0) "Gratis"
                        else formatRp(route.adminFee)
                    )
                    ConfirmDivider()

                    // ── Total — baris khusus lebih menonjol ──────────────
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Total",
                            fontWeight = FontWeight.ExtraBold,
                            style = MaterialTheme.typography.titleMedium)
                        Text(formatRp(route.totalPrice),
                            fontWeight = FontWeight.ExtraBold,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFFFF6B00))
                    }
                }
            }

            // Spacer supaya konten tidak tertutup bottomBar
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

// ── Helper ────────────────────────────────────────────────────────────────────

@Composable
fun ConfirmRow(
    label: String,
    value: String,
    valueColor: Color = Color.Unspecified,
    valueWeight: FontWeight = FontWeight.Normal
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f))
        Text(value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = valueWeight,
            color = if (valueColor == Color.Unspecified)
                MaterialTheme.colorScheme.onSurface else valueColor,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1.5f))
    }
}

@Composable
fun ConfirmDivider() {
    HorizontalDivider(
        modifier  = Modifier.padding(horizontal = 16.dp),
        color     = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
        thickness = 0.5.dp
    )
}