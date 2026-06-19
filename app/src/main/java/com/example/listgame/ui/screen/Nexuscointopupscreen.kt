package com.example.listgame.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.listgame.model.NexusCoinTransaction
import com.example.listgame.model.NexusCoinTransactionStatus
import com.example.listgame.navigation.LocalBackStack
import com.example.listgame.navigation.Route
import com.example.listgame.viewmodel.AppViewModel

// ── Warna brand ───────────────────────────────────────────────────────────────
private val NexusOrange = Color(0xFFE87722)
private val NexusGold   = Color(0xFFFFA726)
private val NexusDark   = Color(0xFF1C1C1C)
private val NexusCard   = Color(0xFF2B2B2B)
private val NexusBorder = Color(0xFF3D3D3D)

// ── Denominasi NEXUS Coin ──────────────────────────────────────────────────────
data class NexusCoinDenom(
    val id       : String,
    val label    : String,
    val price    : Int,
    val coinValue: Int
)

val nexusDenominations = listOf(
    NexusCoinDenom("nc_10k",  "Rp. 10.000",      10_000,      10_000),
    NexusCoinDenom("nc_50k",  "Rp. 50.000",      50_000,      50_000),
    NexusCoinDenom("nc_100k", "Rp. 100.000",    100_000,     100_000),
    NexusCoinDenom("nc_200k", "Rp. 200.000",    200_000,     200_000),
    NexusCoinDenom("nc_300k", "Rp. 300.000",    300_000,     300_000),
    NexusCoinDenom("nc_500k", "Rp. 500.000",    500_000,     500_000),
    NexusCoinDenom("nc_1m",   "Rp. 1.000.000",1_000_000,  1_000_000),
    NexusCoinDenom("nc_5m",   "Rp. 5.000.000",5_000_000,  5_000_000)
)

// ── Metode Pembayaran ─────────────────────────────────────────────────────────
data class NexusPaymentMethod(
    val id       : String,
    val name     : String,
    val fee      : Int,
    val feeLabel : String,
    val subLabel : String = "Proses Otomatis"
)

val nexusPaymentMethods = listOf(
    NexusPaymentMethod("superindo",   "SUPERINDO",                  3_000, "Rp 3.000",  "SUPERINDO"),
    NexusPaymentMethod("qris_bri",    "QRIS BRI",                       0, "Rp 0",      "QRIS BRI"),
    NexusPaymentMethod("ovo",         "OVO",                            0, "Rp 0"),
    NexusPaymentMethod("danamon",     "Danamon",                    4_000, "Rp 4.000"),
    NexusPaymentMethod("shopeepay",   "ShopeePay",                      0, "Rp 0",      "ShopeePay"),
    NexusPaymentMethod("permata",     "Permata",                    4_000, "Rp 4.000"),
    NexusPaymentMethod("dana",        "DANA",                           0, "Rp 0"),
    NexusPaymentMethod("indomaret",   "Indomaret",                  3_000, "Rp 3.000",  "Biaya Layanan +3000"),
    NexusPaymentMethod("alfamart",    "Alfamart",                   2_000, "Rp 2.000"),
    NexusPaymentMethod("cimb",        "CIMB Niaga",                 4_000, "Rp 4.000"),
    NexusPaymentMethod("sinarmas_va", "Sinarmas Virtual Account",   4_000, "Rp 4.000",  "Sinarmas Virtual Account"),
    NexusPaymentMethod("qris_bni",    "QRIS BNI",                       0, "Rp 0",      "QRIS BNI"),
    NexusPaymentMethod("qris_dana",   "QRIS DANA",                      0, "Rp 0",      "QRIS Dana"),
    NexusPaymentMethod("qris_ovo",    "QRIS BNI Mobile",                0, "Rp 0",      "Proses Otomatis"),
    NexusPaymentMethod("mandiri",     "Mandiri",                        0, "Rp 0",      "QRIS Mandiri"),
    NexusPaymentMethod("linkaja",     "LinkAja",                        0, "Rp 0",      "LinkAja"),
    NexusPaymentMethod("qris_bca",    "QRIS BCA",                       0, "Rp 0",      "QRIS BCA"),
    NexusPaymentMethod("qris_ovo2",   "QRIS OVO",                       0, "Rp 0",      "QRIS OVO"),
    NexusPaymentMethod("linkaja2",    "LinkAja",                        0, "Rp 0",      "QRIS LinkAja"),
    NexusPaymentMethod("gopay",       "GoPay",                          0, "Rp 0",      "QRIS GoPay"),
    NexusPaymentMethod("shopeepay2",  "ShopeePay",                      0, "Rp 0",      "QRIS ShopeePay"),
    NexusPaymentMethod("briva",       "BRIVA",                      4_000, "Rp 4.000"),
    NexusPaymentMethod("bsi_va",      "BSI Virtual Account",         4_000, "Rp 4.000",  "BSI Virtual Account"),
    NexusPaymentMethod("bni_va",      "BNI Virtual Account",         4_000, "Rp 4.000",  "BNI Virtual Account"),
    NexusPaymentMethod("mandiri_va",  "Mandiri Virtual Account",        0, "Rp 0"),
    NexusPaymentMethod("lawson",      "Lawson",                      3_000, "Rp 3.000",  "Lawson"),
    NexusPaymentMethod("alfamart2",   "Alfamart",                   3_000, "Rp 3.000",  "Alfamart"),
    NexusPaymentMethod("bsi_va2",     "BSI Virtual Account",         4_000, "Rp 4.000",  "BSI Virtual Account")
)

// ── Screen Utama ──────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NexusCoinTopUpScreen(appViewModel: AppViewModel) {
    val backStack        = LocalBackStack.current
    val nexusCoinBalance by appViewModel.nexusCoinBalance.collectAsState()

    var selectedDenom    by remember { mutableStateOf<NexusCoinDenom?>(null) }
    var selectedPayment  by remember { mutableStateOf<NexusPaymentMethod?>(null) }
    var qty              by remember { mutableIntStateOf(1) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    fun formatRp(v: Int) = "Rp " + "%,d".format(v).replace(",", ".")
    val totalPrice = ((selectedDenom?.price ?: 0) + (selectedPayment?.fee ?: 0)) * qty

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Top Up NEXUS COIN", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = { backStack.removeLastOrNull() }) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = NexusDark,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = NexusDark,
        bottomBar = {
            // Tombol Topup Sekarang selalu tampil di bawah
            Surface(
                color = NexusDark,
                shadowElevation = 0.dp
            ) {
                Button(
                    onClick = {
                        if (selectedDenom != null && selectedPayment != null) {
                            showConfirmDialog = true
                        }
                    },
                    enabled  = selectedDenom != null && selectedPayment != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .height(52.dp),
                    shape  = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor        = NexusOrange,
                        disabledContainerColor = NexusOrange.copy(alpha = 0.5f)
                    )
                ) {
                    Icon(Icons.Rounded.FlashOn, null,
                        modifier = Modifier.size(18.dp), tint = Color.White)
                    Spacer(Modifier.width(6.dp))
                    Text(
                        "Topup Sekarang!",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {

            // ── Header card denominasi ────────────────────────────────────
            Card(
                shape  = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF252525)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Top-up Voucher Lumos Coins (Bebas Biaya Admin)",
                        color      = Color.White,
                        fontWeight = FontWeight.Bold,
                        style      = MaterialTheme.typography.bodyMedium,
                        modifier   = Modifier.padding(bottom = 12.dp)
                    )

                    // ── Grid denominasi 3 kolom ───────────────────────────
                    val rows = nexusDenominations.chunked(3)
                    rows.forEach { rowItems ->
                        Row(
                            modifier              = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowItems.forEach { denom ->
                                val isSelected = selectedDenom?.id == denom.id
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(vertical = 4.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            if (isSelected) Color(0xFF3A3A3A)
                                            else Color(0xFF333333)
                                        )
                                        .border(
                                            width = if (isSelected) 1.5.dp else 0.dp,
                                            color = if (isSelected) NexusOrange
                                            else Color.Transparent,
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .clickable { selectedDenom = denom }
                                        .padding(10.dp)
                                ) {
                                    Column {
                                        Text(
                                            "Saldo ${denom.label}",
                                            color = Color.White,
                                            style = MaterialTheme.typography.labelSmall,
                                            lineHeight = 14.sp
                                        )
                                        Text(
                                            denom.label,
                                            color      = if (isSelected) NexusOrange else Color.White,
                                            fontWeight = FontWeight.Bold,
                                            style      = MaterialTheme.typography.labelSmall
                                        )
                                    }
                                }
                            }
                            // Isi slot kosong jika baris tidak penuh
                            repeat(3 - rowItems.size) {
                                Box(modifier = Modifier.weight(1f))
                            }
                        }
                    }

                    Spacer(Modifier.height(12.dp))
                    HorizontalDivider(color = NexusBorder)
                    Spacer(Modifier.height(12.dp))

                    // ── Qty ───────────────────────────────────────────────
                    Text("Qty", color = Color.White,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value         = qty.toString(),
                            onValueChange = {},
                            readOnly      = true,
                            modifier      = Modifier.weight(1f),
                            singleLine    = true,
                            shape         = RoundedCornerShape(8.dp),
                            colors        = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor   = NexusBorder,
                                unfocusedBorderColor = NexusBorder,
                                focusedTextColor     = Color.White,
                                unfocusedTextColor   = Color.White,
                                unfocusedContainerColor = Color(0xFF3A3A3A),
                                focusedContainerColor   = Color(0xFF3A3A3A)
                            )
                        )
                        // Tombol +
                        Button(
                            onClick  = { qty++ },
                            shape    = RoundedCornerShape(8.dp),
                            modifier = Modifier.size(52.dp),
                            contentPadding = PaddingValues(0.dp),
                            colors   = ButtonDefaults.buttonColors(containerColor = NexusOrange)
                        ) {
                            Text("+", fontSize = 24.sp, color = Color.White,
                                fontWeight = FontWeight.Bold)
                        }
                        // Tombol -
                        Button(
                            onClick  = { if (qty > 1) qty-- },
                            enabled  = qty > 1,
                            shape    = RoundedCornerShape(8.dp),
                            modifier = Modifier.size(52.dp),
                            contentPadding = PaddingValues(0.dp),
                            colors   = ButtonDefaults.buttonColors(
                                containerColor        = Color(0xFF8B4500),
                                disabledContainerColor = Color(0xFF555555)
                            )
                        ) {
                            Text("−", fontSize = 24.sp, color = Color.White,
                                fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── Section Metode Pembayaran ─────────────────────────────────
            Card(
                shape  = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF252525)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Metode Pembayaran",
                        color      = Color.White,
                        fontWeight = FontWeight.Bold,
                        style      = MaterialTheme.typography.bodyLarge,
                        modifier   = Modifier.padding(bottom = 4.dp)
                    )
                    HorizontalDivider(color = NexusBorder, modifier = Modifier.padding(vertical = 8.dp))

                    // ── Grid metode pembayaran 3 kolom ────────────────────
                    val rows = nexusPaymentMethods.chunked(3)
                    rows.forEach { rowItems ->
                        Row(
                            modifier              = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowItems.forEach { method ->
                                val isSelected = selectedPayment?.id == method.id
                                PaymentMethodCard(
                                    method     = method,
                                    isSelected = isSelected,
                                    modifier   = Modifier.weight(1f),
                                    onClick    = { selectedPayment = method }
                                )
                            }
                            // isi slot kosong jika baris tidak penuh
                            repeat(3 - rowItems.size) {
                                Box(modifier = Modifier.weight(1f))
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
        }
    }

    // ── Dialog Konfirmasi ─────────────────────────────────────────────────────
    if (showConfirmDialog && selectedDenom != null && selectedPayment != null) {
        NexusCoinConfirmDialog(
            denom      = selectedDenom!!,
            payment    = selectedPayment!!,
            qty        = qty,
            totalPrice = totalPrice,
            onConfirm  = {
                showConfirmDialog = false
                val coinsEarned = selectedDenom!!.coinValue * qty
                appViewModel.addNexusCoins(coinsEarned)
                appViewModel.addNexusCoinTransaction(
                    NexusCoinTransaction(
                        invoiceId   = "NC-${System.currentTimeMillis()}",
                        denomLabel  = selectedDenom!!.label,
                        coinValue   = coinsEarned,
                        paymentName = selectedPayment!!.name,
                        totalPrice  = totalPrice,
                        status      = NexusCoinTransactionStatus.SUCCESS
                    )
                )
                showSuccessDialog = true
            },
            onDismiss  = { showConfirmDialog = false }
        )
    }

    // ── Dialog Sukses ─────────────────────────────────────────────────────────
    if (showSuccessDialog && selectedDenom != null) {
        NexusCoinSuccessDialog(
            coinValue = selectedDenom!!.coinValue * qty,
            onDone    = {
                showSuccessDialog = false
                backStack.removeLastOrNull()
            }
        )
    }
}

// ── Kartu metode pembayaran ───────────────────────────────────────────────────
@Composable
private fun PaymentMethodCard(
    method    : NexusPaymentMethod,
    isSelected: Boolean,
    modifier  : Modifier = Modifier,
    onClick   : () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (isSelected) Color(0xFF3A3A3A) else Color(0xFF333333)
            )
            .border(
                width = if (isSelected) 1.5.dp else 0.5.dp,
                color = if (isSelected) NexusOrange else NexusBorder,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .padding(10.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            // Logo placeholder — ikon generik sesuai kategori
            val icon = when {
                method.id.contains("qris") -> Icons.Rounded.QrCode
                method.id.contains("ovo") || method.id.contains("gopay") ||
                        method.id.contains("dana") || method.id.contains("shopeepay") ||
                        method.id.contains("linkaja") -> Icons.Rounded.Wallet
                method.id.contains("va") || method.id.contains("briva") ||
                        method.id.contains("mandiri") || method.id.contains("cimb") ||
                        method.id.contains("danamon") || method.id.contains("permata") -> Icons.Rounded.AccountBalance
                method.id.contains("alfamart") || method.id.contains("indomaret") ||
                        method.id.contains("lawson") || method.id.contains("superindo") -> Icons.Rounded.Store
                else -> Icons.Rounded.Payment
            }
            Icon(icon, null,
                tint     = if (isSelected) NexusOrange else Color.LightGray,
                modifier = Modifier.size(20.dp))
            Text(
                method.feeLabel,
                color      = if (isSelected) NexusOrange else Color.White,
                fontWeight = FontWeight.Bold,
                style      = MaterialTheme.typography.labelSmall
            )
            Text(
                method.subLabel,
                color = Color.Gray,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 2,
                lineHeight = 12.sp
            )
        }
    }
}

// ── Dialog Konfirmasi ─────────────────────────────────────────────────────────
@Composable
fun NexusCoinConfirmDialog(
    denom     : NexusCoinDenom,
    payment   : NexusPaymentMethod,
    qty       : Int,
    totalPrice: Int,
    onConfirm : () -> Unit,
    onDismiss : () -> Unit
) {
    fun fmt(v: Int) = "Rp " + "%,d".format(v).replace(",", ".")

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor   = Color(0xFF252525),
        icon = {
            Icon(Icons.Rounded.Paid, null,
                tint = NexusOrange, modifier = Modifier.size(32.dp))
        },
        title = {
            Text("Konfirmasi Top Up",
                fontWeight = FontWeight.Bold, color = Color.White)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ConfirmRow("Nominal",       denom.label)
                ConfirmRow("Koin Diterima",
                    "%,d".format(denom.coinValue * qty).replace(",", ".") + " Coins")
                ConfirmRow("Jumlah",        "x$qty")
                ConfirmRow("Metode",        payment.name)
                ConfirmRow("Biaya Admin",   payment.feeLabel)
                HorizontalDivider(color = NexusBorder)
                ConfirmRow("Total",         fmt(totalPrice), highlight = true)
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors  = ButtonDefaults.buttonColors(containerColor = NexusOrange)
            ) { Text("Bayar Sekarang", color = Color.White, fontWeight = FontWeight.Bold) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal", color = Color.Gray)
            }
        }
    )
}

@Composable
private fun ConfirmRow(label: String, value: String, highlight: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray)
        Text(value,
            style      = MaterialTheme.typography.bodyMedium,
            fontWeight = if (highlight) FontWeight.ExtraBold else FontWeight.SemiBold,
            color      = if (highlight) NexusOrange else Color.White)
    }
}

// ── Dialog Sukses ─────────────────────────────────────────────────────────────
@Composable
fun NexusCoinSuccessDialog(
    coinValue: Int,
    onDone   : () -> Unit
) {
    Dialog(onDismissRequest = onDone) {
        Card(
            shape  = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
        ) {
            Column(
                modifier            = Modifier.padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(Icons.Rounded.CheckCircle, null,
                    tint = Color(0xFF4CAF50), modifier = Modifier.size(60.dp))
                Text("Top Up Berhasil!",
                    fontSize   = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color      = Color.White)
                Text(
                    "+" + "%,d".format(coinValue).replace(",", ".") + " NEXUS Coins",
                    fontSize   = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color      = NexusOrange,
                    textAlign  = TextAlign.Center
                )
                Text("telah ditambahkan ke akun kamu",
                    color     = Color.Gray,
                    textAlign = TextAlign.Center,
                    style     = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(4.dp))
                Button(
                    onClick  = onDone,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape    = RoundedCornerShape(12.dp),
                    colors   = ButtonDefaults.buttonColors(containerColor = NexusOrange)
                ) { Text("Selesai", fontWeight = FontWeight.Bold, color = Color.White) }
            }
        }
    }
}