package com.example.listgame.ui.screen

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listgame.data.DummyData
import com.example.listgame.model.Transaction
import com.example.listgame.model.TransactionStatus
import com.example.listgame.navigation.LocalBackStack
import com.example.listgame.navigation.Route
import com.example.listgame.viewmodel.AppViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

// ── Enum step transaksi ───────────────────────────────────────────────────────
private enum class TxStep { CREATED, PAYMENT, PROCESSING, DONE }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentProgressScreen(
    route        : Route.PaymentProgress,
    appViewModel : AppViewModel
) {
    val backStack = LocalBackStack.current
    val game      = DummyData.popularGames.find { it.id == route.gameId }

    var currentStep    by remember { mutableStateOf(TxStep.CREATED) }
    var isPaymentDone  by remember { mutableStateOf(false) }
    var completionTime by remember { mutableStateOf("") }
    var countdown      by remember { mutableIntStateOf(10799) }

    val invoiceNumber = remember {
        "LD${System.currentTimeMillis().toString().takeLast(10)}"
    }

    fun formatRp(v: Int) = "Rp ${"%,d".format(v).replace(',', '.')}"

    // ── Rotasi ikon loading ───────────────────────────────────────────────────
    val rotation by rememberInfiniteTransition(label = "spin").animateFloat(
        initialValue  = 0f,
        targetValue   = 360f,
        animationSpec = infiniteRepeatable(tween(900, easing = LinearEasing)),
        label         = "rotation"
    )

    // ── Simulasi progress: 2 menit di PAYMENT ────────────────────────────────
    LaunchedEffect(Unit) {
        delay(800L)
        currentStep = TxStep.PAYMENT

        delay(5_000L)   // 2 menit menunggu bayar

        currentStep = TxStep.PROCESSING
        delay(3_000L)

        currentStep   = TxStep.DONE
        isPaymentDone = true
        completionTime = SimpleDateFormat(
            "yyyy/MM/dd HH:mm:ss", Locale.getDefault()
        ).format(Date())

        // ── Simpan transaksi ke DataStore ─────────────────────────────────
        appViewModel.addTransaction(
            Transaction(
                invoiceId  = invoiceNumber,
                gameTitle  = game?.title ?: "Unknown",
                gameId     = route.gameId,
                playerId   = route.playerId,
                item       = "${route.amount} (x${route.quantity})",
                payment    = route.paymentName,
                totalPrice = route.totalPrice,
                status     = TransactionStatus.SUCCESS
            )
        )
    }

    // ── Countdown mundur ──────────────────────────────────────────────────────
    LaunchedEffect(currentStep) {
        if (currentStep == TxStep.PAYMENT) {
            while (countdown > 0 && currentStep == TxStep.PAYMENT) {
                delay(1_000L)
                countdown--
            }
        }
    }

    val countdownText = remember(countdown) {
        val h = countdown / 3600
        val m = (countdown % 3600) / 60
        val s = countdown % 60
        "%d Jam  %02d Menit  %02d Detik".format(h, m, s)
    }

    val bannerBg   = if (isPaymentDone) Color(0xFF4CAF50) else Color(0xFFFFCC00)
    val bannerText = if (isPaymentDone) Color.White       else Color(0xFFCC8800)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (isPaymentDone) "Pesanan Selesai" else "Menunggu Pembayaran",
                        fontWeight = FontWeight.Bold,
                        color      = if (isPaymentDone) Color.White
                        else MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        val home = backStack.filterIsInstance<Route.Home>().lastOrNull()
                        backStack.clear()
                        backStack.add(home ?: Route.Login)
                    }) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack, "Kembali",
                            tint = if (isPaymentDone) Color.White
                            else MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isPaymentDone) Color(0xFF4CAF50)
                    else MaterialTheme.colorScheme.primaryContainer
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

            // ── Banner Atas ───────────────────────────────────────────────
            Box(
                modifier         = Modifier
                    .fillMaxWidth()
                    .background(bannerBg)
                    .padding(vertical = 36.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    if (isPaymentDone) {
                        // Banner hijau — selesai
                        Text("✦  ✧  ✦",
                            color    = Color.White.copy(alpha = 0.6f),
                            fontSize = 14.sp)
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.25f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Rounded.Check, null,
                                tint     = Color.White,
                                modifier = Modifier.size(44.dp))
                        }
                        Text("Pesanan Selesai!",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize   = 22.sp,
                            color      = Color.White)
                        Text("Pesanan kamu sudah berhasil diproses!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.85f))
                    } else {
                        // Banner kuning — menunggu
                        WaitingBannerIllustration()
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Menunggu Pembayaran",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize   = 22.sp,
                            color      = bannerText)
                        Text(
                            "Silahkan untuk melakukan pembayaran\ndengan metode yang kamu pilih.",
                            style     = MaterialTheme.typography.bodyMedium,
                            color     = bannerText.copy(alpha = 0.85f),
                            textAlign = TextAlign.Center,
                            modifier  = Modifier.padding(horizontal = 32.dp)
                        )
                    }
                }
            }

            // ── Konten Utama ──────────────────────────────────────────────
            Column(
                modifier            = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // Progress Transaksi
                Text("Progress Transaksi",
                    fontWeight = FontWeight.Bold,
                    style      = MaterialTheme.typography.titleMedium)

                TxProgressBar(currentStep = currentStep)

                // Countdown
                if (!isPaymentDone) {
                    Surface(
                        shape    = RoundedCornerShape(12.dp),
                        color    = Color(0xFFFF6B00).copy(alpha = 0.13f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier              = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 14.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment     = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Rounded.Timer, null,
                                tint     = Color(0xFFFF6B00),
                                modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(countdownText,
                                fontWeight = FontWeight.Bold,
                                color      = Color(0xFFFF6B00),
                                fontSize   = 15.sp)
                        }
                    }
                }

                // ── Informasi Akun ────────────────────────────────────────
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape    = RoundedCornerShape(14.dp),
                    colors   = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column {
                        Row(
                            modifier              = Modifier.padding(14.dp),
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                            verticalAlignment     = Alignment.Top
                        ) {
                            game?.let {
                                Image(
                                    painter            = painterResource(id = it.imageRes),
                                    contentDescription = null,
                                    modifier           = Modifier
                                        .size(80.dp)
                                        .clip(RoundedCornerShape(10.dp)),
                                    contentScale       = ContentScale.Crop
                                )
                            }
                            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                                Text("Informasi Akun",
                                    fontWeight = FontWeight.Bold,
                                    color      = MaterialTheme.colorScheme.primary,
                                    style      = MaterialTheme.typography.titleSmall)
                                TxInfoRow("Nickname", route.username)
                                TxInfoRow("ID",       route.playerId)
                                TxInfoRow("Item",     "${route.amount} (x${route.quantity})")
                            }
                        }
                        Box(
                            modifier         = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(game?.title ?: "-",
                                fontWeight = FontWeight.Bold,
                                style      = MaterialTheme.typography.labelLarge,
                                color      = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                    }
                }

                // ── Rincian & Status (2 kolom) ────────────────────────────
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Rincian Pembayaran
                    Card(
                        modifier = Modifier.weight(1f),
                        shape    = RoundedCornerShape(14.dp),
                        colors   = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier            = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text("Rincian Pembayaran",
                                fontWeight = FontWeight.Bold,
                                style      = MaterialTheme.typography.labelLarge)
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.outlineVariant
                            )
                            Spacer(modifier = Modifier.height(2.dp))

                            val unitPrice = if (route.quantity > 0)
                                route.subtotal / route.quantity else 0

                            TxDetailRow("Harga",    formatRp(unitPrice))
                            TxDetailRow("Jumlah",   "${route.quantity}x")
                            TxDetailRow("Subtotal", formatRp(route.subtotal))
                            TxDetailRow("Biaya",    formatRp(route.adminFee))

                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.outlineVariant
                            )
                            Text("Total Pembayaran",
                                fontWeight = FontWeight.Bold,
                                fontSize   = 11.sp,
                                style      = MaterialTheme.typography.bodySmall)
                            Row(
                                modifier              = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment     = Alignment.CenterVertically
                            ) {
                                Text(formatRp(route.totalPrice),
                                    fontWeight = FontWeight.ExtraBold,
                                    color      = Color(0xFFFF6B00),
                                    fontSize   = 13.sp)
                                Icon(Icons.Rounded.ContentCopy, null,
                                    tint     = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(14.dp))
                            }
                        }
                    }

                    // Status Pembayaran
                    Card(
                        modifier = Modifier.weight(1f),
                        shape    = RoundedCornerShape(14.dp),
                        colors   = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier            = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text("Metode Pembayaran",
                                fontWeight = FontWeight.Bold,
                                style      = MaterialTheme.typography.labelLarge)
                            Text(route.paymentName,
                                style      = MaterialTheme.typography.bodySmall,
                                color      = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold)

                            Spacer(modifier = Modifier.height(2.dp))

                            Text("Nomor Invoice",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Row(
                                verticalAlignment     = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(invoiceNumber,
                                    style      = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier   = Modifier.weight(1f))
                                Icon(Icons.Rounded.ContentCopy, null,
                                    tint     = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(14.dp))
                            }

                            Spacer(modifier = Modifier.height(2.dp))

                            Text("Status Pembayaran",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                            TxStatusBadge(
                                label = if (isPaymentDone) "PAID" else "UNPAID",
                                color = if (isPaymentDone) Color(0xFF4CAF50)
                                else Color(0xFFFFC107)
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text("Status Transaksi",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                            TxStatusBadge(
                                label = when (currentStep) {
                                    TxStep.CREATED    -> "CREATED"
                                    TxStep.PAYMENT    -> "PENDING"
                                    TxStep.PROCESSING -> "PROCESSING"
                                    TxStep.DONE       -> "SUCCESS"
                                },
                                color = when (currentStep) {
                                    TxStep.CREATED    -> Color(0xFF90CAF9)
                                    TxStep.PAYMENT    -> Color(0xFFFF9800)
                                    TxStep.PROCESSING -> Color(0xFF42A5F5)
                                    TxStep.DONE       -> Color(0xFF4CAF50)
                                }
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text("Pesan",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(
                                text = when (currentStep) {
                                    TxStep.CREATED    -> "Transaksi berhasil dibuat."
                                    TxStep.PAYMENT    -> "Your order is being processed. Please wait!"
                                    TxStep.PROCESSING -> "Sedang diproses oleh sistem."
                                    TxStep.DONE       -> "Transaction completed at $completionTime WIB"
                                },
                                style      = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color      = if (isPaymentDone) Color(0xFF4CAF50)
                                else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                // ── QR Code (QRIS & belum selesai) ───────────────────────
                if (!isPaymentDone &&
                    route.paymentName.contains("QRIS", ignoreCase = true)
                ) {
                    TxQrCodeSection()
                }

                // ── Instruksi Pembayaran ──────────────────────────────────
                TxInstructionSection(paymentName = route.paymentName)

                // ── Tombol Beli Lagi / Selesai ────────────────────────────
                if (isPaymentDone) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Button(
                        onClick = {
                            val home = backStack
                                .filterIsInstance<Route.Home>().lastOrNull()
                            backStack.clear()
                            backStack.add(home ?: Route.Login)
                        },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape    = RoundedCornerShape(14.dp),
                        colors   = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50)
                        )
                    ) {
                        Icon(Icons.Rounded.Home, null,
                            modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Kembali ke Beranda",
                            fontWeight = FontWeight.Bold,
                            color      = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// ── Ilustrasi Banner Menunggu ─────────────────────────────────────────────────

@Composable
private fun WaitingBannerIllustration() {
    Box(modifier = Modifier.size(120.dp), contentAlignment = Alignment.BottomCenter) {
        // Kartu bank
        Card(
            modifier = Modifier
                .size(width = 90.dp, height = 56.dp)
                .offset(x = 6.dp, y = 0.dp)
                .rotate(-6f),
            shape  = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF546E7A))
        ) {
            Column(
                modifier            = Modifier.fillMaxSize().padding(10.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Text("BANK", fontSize = 8.sp,
                        color      = Color.White.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Bold)
                }
                Text("XXXX  1234", fontSize = 9.sp,
                    color = Color.White, fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp)
            }
        }
        // Mesin kasir
        Card(
            modifier = Modifier
                .size(width = 76.dp, height = 88.dp)
                .offset(x = (-6).dp, y = (-18).dp),
            shape  = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF78909C))
        ) {
            Column(
                modifier            = Modifier.fillMaxSize().padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFF90A4AE)))
                repeat(3) {
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        repeat(3) {
                            Box(modifier = Modifier
                                .height(13.dp)
                                .weight(1f)
                                .clip(RoundedCornerShape(3.dp))
                                .background(Color(0xFF607D8B)))
                        }
                    }
                }
            }
        }
    }
}

// ── Progress Bar 4 Langkah ────────────────────────────────────────────────────

@Composable
private fun TxProgressBar(currentStep: TxStep) {
    data class StepInfo(
        val step: TxStep, val icon: ImageVector,
        val label: String, val subLabel: String
    )

    val steps = listOf(
        StepInfo(TxStep.CREATED,    Icons.Rounded.CheckCircle,
            "Transaksi Dibuat",  "Transaksi telah berhasil dibuat"),
        StepInfo(TxStep.PAYMENT,    Icons.Rounded.Payment,
            "Pembayaran",        "Silakan melakukan pembayaran"),
        StepInfo(TxStep.PROCESSING, Icons.Rounded.Autorenew,
            "Sedang Di Proses",  "Pembelian sedang dalam proses."),
        StepInfo(TxStep.DONE,       Icons.Rounded.TaskAlt,
            "Transaksi Selesai", "Transaksi telah berhasil dilakukan.")
    )

    val stepOrder    = steps.map { it.step }
    val currentIndex = stepOrder.indexOf(currentStep)

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier          = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            steps.forEachIndexed { index, info ->
                val stepIndex = stepOrder.indexOf(info.step)
                val isDone    = stepIndex < currentIndex
                val isActive  = info.step == currentStep

                val circleColor = when {
                    isDone   -> Color(0xFF4CAF50)
                    isActive -> if (currentStep == TxStep.DONE) Color(0xFF4CAF50)
                    else Color(0xFFFF6B00)
                    else     -> MaterialTheme.colorScheme.outlineVariant
                }

                Box(
                    modifier         = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(circleColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(info.icon, null,
                        tint     = Color.White,
                        modifier = Modifier.size(20.dp))
                }

                if (index < steps.lastIndex) {
                    Box(modifier = Modifier
                        .weight(1f)
                        .height(3.dp)
                        .background(
                            if (currentIndex > index) Color(0xFF4CAF50)
                            else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                        ))
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            steps.forEachIndexed { index, info ->
                val stepIndex = stepOrder.indexOf(info.step)
                val isActive  = info.step == currentStep
                val isDone    = stepIndex < currentIndex

                val align = when (index) {
                    0               -> TextAlign.Start
                    steps.lastIndex -> TextAlign.End
                    else            -> TextAlign.Center
                }

                Column(
                    modifier            = Modifier.weight(1f),
                    horizontalAlignment = when (index) {
                        0               -> Alignment.Start
                        steps.lastIndex -> Alignment.End
                        else            -> Alignment.CenterHorizontally
                    }
                ) {
                    Text(info.label,
                        style      = MaterialTheme.typography.labelSmall,
                        fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                        color = when {
                            currentStep == TxStep.DONE -> Color(0xFF4CAF50)
                            isActive -> Color(0xFFFF6B00)
                            isDone   -> Color(0xFF4CAF50)
                            else     -> MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        textAlign = align)
                    Text(info.subLabel,
                        fontSize   = 8.sp,
                        color      = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        textAlign  = align,
                        maxLines   = 2,
                        lineHeight = 10.sp)
                }
            }
        }
    }
}

// ── QR Code ───────────────────────────────────────────────────────────────────

@Composable
private fun TxQrCodeSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(14.dp),
        colors   = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier            = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier         = Modifier
                    .size(180.dp)
                    .border(2.dp,
                        MaterialTheme.colorScheme.outline,
                        RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.QrCode, null,
                    modifier = Modifier.size(150.dp),
                    tint     = Color.Black)
            }
            Button(
                onClick = {},
                shape   = RoundedCornerShape(10.dp),
                colors  = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF6B00)
                )
            ) {
                Icon(Icons.Rounded.Download, null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Unduh Kode QR", color = Color.White, fontWeight = FontWeight.Bold)
            }
            Text("Screenshot jika QR Code tidak bisa di-download.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

// ── Instruksi Pembayaran ──────────────────────────────────────────────────────

@Composable
private fun TxInstructionSection(paymentName: String) {
    var expanded by remember { mutableStateOf(false) }

    val instructions = when {
        paymentName.contains("GoPay",           ignoreCase = true) -> listOf(
            "1) Buka aplikasi GoPay",
            "2) Download Code QR pembayaran",
            "3) Buka scan QRIS di aplikasi GoPay",
            "4) Upload image Code QR",
            "5) Bayar"
        )
        paymentName.contains("OVO",             ignoreCase = true) -> listOf(
            "1) Buka aplikasi OVO",
            "2) Pilih menu Scan",
            "3) Arahkan kamera ke QR Code",
            "4) Konfirmasi pembayaran",
            "5) Bayar"
        )
        paymentName.contains("Virtual Account", ignoreCase = true) -> listOf(
            "1) Buka aplikasi m-Banking atau ATM",
            "2) Pilih menu Transfer / Virtual Account",
            "3) Masukkan nomor Virtual Account",
            "4) Konfirmasi jumlah pembayaran",
            "5) Selesaikan transaksi"
        )
        paymentName.contains("Alfamart",        ignoreCase = true) -> listOf(
            "1) Kunjungi Alfamart terdekat",
            "2) Tunjukkan kode pembayaran ke kasir",
            "3) Kasir akan memproses pembayaran",
            "4) Simpan struk sebagai bukti",
            "5) Selesai"
        )
        paymentName.contains("Indomaret",       ignoreCase = true) -> listOf(
            "1) Kunjungi Indomaret terdekat",
            "2) Tunjukkan kode pembayaran ke kasir",
            "3) Kasir akan memproses pembayaran",
            "4) Simpan struk sebagai bukti",
            "5) Selesai"
        )
        paymentName.contains("Transfer",        ignoreCase = true) -> listOf(
            "1) Buka aplikasi m-Banking",
            "2) Pilih menu Transfer",
            "3) Masukkan nomor rekening tujuan",
            "4) Masukkan jumlah transfer sesuai total",
            "5) Konfirmasi dan selesaikan transfer"
        )
        else -> listOf(
            "1) Pilih metode pembayaran yang tersedia",
            "2) Ikuti instruksi pembayaran",
            "3) Konfirmasi pembayaran",
            "4) Tunggu proses selesai",
            "5) Selesai"
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(14.dp),
        colors   = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column {
            Text("Instruksi Pembayaran",
                fontWeight = FontWeight.Bold,
                style      = MaterialTheme.typography.titleSmall,
                modifier   = Modifier.padding(16.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Row(
                modifier              = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text("Cara Melakukan Pembayaran",
                    style      = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold)
                IconButton(
                    onClick  = { expanded = !expanded },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        if (expanded) Icons.Rounded.KeyboardArrowUp
                        else Icons.Rounded.KeyboardArrowDown, null
                    )
                }
            }
            if (expanded) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color    = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                )
                Column(
                    modifier            = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    instructions.forEach { step ->
                        Text(step,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        }
    }
}

// ── Helper Composables ────────────────────────────────────────────────────────

@Composable
private fun TxInfoRow(label: String, value: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Text("$label :",
            style    = MaterialTheme.typography.bodySmall,
            color    = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(60.dp))
        Text(value,
            style      = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun TxDetailRow(label: String, value: String) {
    Row(
        modifier              = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value,
            style      = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun TxStatusBadge(label: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = color.copy(alpha = 0.18f)
    ) {
        Text(label,
            modifier   = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style      = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.ExtraBold,
            color      = color)
    }
}