package com.example.listgame.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listgame.model.NexusCoinTransaction
import com.example.listgame.model.NexusCoinTransactionStatus
import com.example.listgame.navigation.LocalBackStack
import com.example.listgame.viewmodel.AppViewModel

private val RedeemOrange = Color(0xFFE87722)
private val RedeemDark   = Color(0xFF1C1C1C)
private val RedeemCard   = Color(0xFF2B2B2B)
private val RedeemBorder = Color(0xFF3D3D3D)
private val RedeemField  = Color(0xFF3A3A3A)

// ── Peta kode voucher bawaan (bisa diperluas / ditarik dari backend) ──────────
private val validVoucherCodes: Map<String, Int> = mapOf(
    "NEXUS10K"   to  10_000,
    "NEXUS50K"   to  50_000,
    "NEXUS100K"  to 100_000,
    "FREECOIN"   to   5_000,
    "WELCOME25K" to  25_000
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NexusCoinRedeemScreen(appViewModel: AppViewModel) {
    val backStack = LocalBackStack.current

    var voucherCode     by remember { mutableStateOf("") }
    var isLoading       by remember { mutableStateOf(false) }
    var resultMessage   by remember { mutableStateOf<String?>(null) }
    var resultIsSuccess by remember { mutableStateOf(false) }

    fun doRedeem() {
        val trimmed = voucherCode.trim().uppercase()
        if (trimmed.isEmpty()) return
        isLoading = true
        resultMessage = null

        val coins = validVoucherCodes[trimmed]
        if (coins != null) {
            appViewModel.addNexusCoins(coins)
            appViewModel.addNexusCoinTransaction(
                NexusCoinTransaction(
                    invoiceId   = "RDM-${System.currentTimeMillis()}",
                    denomLabel  = "Voucher: $trimmed",
                    coinValue   = coins,
                    paymentName = "Kode Voucher",
                    totalPrice  = 0,
                    status      = NexusCoinTransactionStatus.SUCCESS
                )
            )
            resultIsSuccess = true
            resultMessage   = "Berhasil! +${ "%,d".format(coins).replace(",", ".") } NEXUS Coins ditambahkan."
            voucherCode     = ""
        } else {
            resultIsSuccess = false
            resultMessage   = "Kode voucher tidak valid atau sudah digunakan."
        }
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Riwayat", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = { backStack.removeLastOrNull() }) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor         = RedeemDark,
                    titleContentColor      = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = RedeemDark
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // ── Judul ─────────────────────────────────────────────────────
            Text(
                "Redeem Voucher Lumos Coins (Bebas Biaya Admin)",
                color      = Color.White,
                fontWeight = FontWeight.Bold,
                style      = MaterialTheme.typography.titleLarge,
                lineHeight = 28.sp
            )

            // ── Form input kode voucher ───────────────────────────────────
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "Kode Voucher Lumos Coins (Bebas Biaya Admin)",
                    color      = Color.White,
                    fontWeight = FontWeight.Bold,
                    style      = MaterialTheme.typography.bodyMedium
                )

                OutlinedTextField(
                    value         = voucherCode,
                    onValueChange = {
                        voucherCode   = it
                        resultMessage = null
                    },
                    placeholder   = {
                        Text("Input Kode Voucher Disini",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodyMedium)
                    },
                    modifier      = Modifier.fillMaxWidth(),
                    singleLine    = true,
                    shape         = RoundedCornerShape(8.dp),
                    colors        = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor      = RedeemOrange,
                        unfocusedBorderColor    = RedeemField,
                        focusedTextColor        = Color.White,
                        unfocusedTextColor      = Color.White,
                        cursorColor             = RedeemOrange,
                        focusedContainerColor   = RedeemField,
                        unfocusedContainerColor = RedeemField
                    )
                )

                // ── Tombol Redeem ─────────────────────────────────────────
                Button(
                    onClick  = { doRedeem() },
                    enabled  = voucherCode.isNotBlank() && !isLoading,
                    shape    = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(48.dp),
                    colors   = ButtonDefaults.buttonColors(
                        containerColor        = RedeemOrange,
                        disabledContainerColor = RedeemOrange.copy(alpha = 0.45f)
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color    = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Redeem",
                            fontWeight = FontWeight.Bold,
                            color      = Color.White,
                            fontSize   = 15.sp)
                    }
                }
            }

            // ── Pesan hasil redeem ────────────────────────────────────────
            if (resultMessage != null) {
                Card(
                    shape  = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (resultIsSuccess)
                            Color(0xFF1B4332)
                        else
                            Color(0xFF4A1A1A)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            if (resultIsSuccess) Icons.Rounded.CheckCircle
                            else Icons.Rounded.Error,
                            null,
                            tint = if (resultIsSuccess) Color(0xFF4CAF50)
                            else Color(0xFFEF5350),
                            modifier = Modifier.size(22.dp)
                        )
                        Text(
                            resultMessage!!,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // ── Info voucher tersedia ─────────────────────────────────────
            Card(
                shape  = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF252525)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Rounded.Info, null,
                            tint = RedeemOrange, modifier = Modifier.size(16.dp))
                        Text("Cara Redeem Voucher",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodySmall)
                    }
                    Text(
                        "1. Masukkan kode voucher yang kamu punya.\n" +
                                "2. Tekan tombol Redeem.\n" +
                                "3. NEXUS Coins akan langsung ditambahkan ke akun kamu.\n" +
                                "4. Setiap kode voucher hanya dapat digunakan satu kali.",
                        color     = Color.Gray,
                        style     = MaterialTheme.typography.bodySmall,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}