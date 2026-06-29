package com.example.listgame.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState // Tambahan import
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll // Tambahan import
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listgame.model.Transaction
import com.example.listgame.model.TransactionStatus
import com.example.listgame.navigation.LocalBackStack
import com.example.listgame.network.HistoryRepository
import com.example.listgame.viewmodel.AppViewModel
import com.example.listgame.ui.components.BottomNavBar
import com.example.listgame.ui.components.BottomNavDestination
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CekTransaksiScreen(
    appViewModel: AppViewModel,
    onNavigateToGame      : () -> Unit = {},
    onNavigateToNexusCoin : () -> Unit = {},
    onNavigateToDashboard : () -> Unit = {}
)  {
    val backStack      = LocalBackStack.current
    val clipboardManager = LocalClipboardManager.current
    val keyboardCtrl   = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()
    val repository     = remember { HistoryRepository() }

    var invoiceInput   by remember { mutableStateOf("") }
    var searchResult   by remember { mutableStateOf<Transaction?>(null) }
    var notFound       by remember { mutableStateOf(false) }
    var hasSearched    by remember { mutableStateOf(false) }
    var isLoading      by remember { mutableStateOf(false) }

    // State untuk kontrol scroll
    val scrollState    = rememberScrollState()

    fun mapStatus(raw: String): TransactionStatus = when (raw.lowercase()) {
        "success"    -> TransactionStatus.SUCCESS
        "pending"    -> TransactionStatus.PENDING
        "processing" -> TransactionStatus.PROCESSING
        else         -> TransactionStatus.FAILED
    }

    fun doSearch() {
        keyboardCtrl?.hide()
        val query = invoiceInput.trim()
        if (query.isBlank()) return

        hasSearched  = true
        notFound     = false
        searchResult = null
        isLoading    = true

        coroutineScope.launch {
            try {
                val response = repository.getTransactionByInvoice(query)

                if (response.isSuccessful && response.body()?.success == true) {
                    val trx = response.body()?.transaction
                    if (trx != null) {
                        searchResult = Transaction(
                            invoiceId  = trx.invoice_id,
                            gameTitle  = trx.game_name,
                            gameId     = 0,
                            playerId   = trx.player_id ?: "-",
                            item       = trx.amount,
                            payment    = trx.payment_method ?: "-",
                            totalPrice = trx.price,
                            status     = mapStatus(trx.status)
                        )
                    } else {
                        notFound = true
                    }
                } else {
                    notFound = true
                }
            } catch (e: Exception) {
                notFound = true
            } finally {
                isLoading = false
            }
        }
    }

    fun formatRp(v: Int) = "Rp ${"%,d".format(v).replace(',', '.')}"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cek Transaksi", fontWeight = FontWeight.Bold) },
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
            BottomNavBar(current = BottomNavDestination.CEK_TRANSAKSI) { dest ->
                when (dest) {
                    BottomNavDestination.GAME          -> onNavigateToGame()
                    BottomNavDestination.CEK_TRANSAKSI -> { }
                    BottomNavDestination.NEXUS_COIN    -> onNavigateToNexusCoin()
                    BottomNavDestination.DASHBOARD     -> onNavigateToDashboard()
                }
            }
        }
    ) { innerPadding ->
        // Mengubah Column utama agar mendukung vertikal scroll penuh
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF1C1C1C), Color(0xFF2A2A2A))
                        )
                    )
                    .padding(horizontal = 24.dp, vertical = 48.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Cek Invoice Kamu dengan Mudah dan Cepat",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize   = 22.sp,
                        color      = Color.White,
                        textAlign  = TextAlign.Center,
                        lineHeight = 30.sp
                    )
                    Text(
                        "Lihat detail pembelian kamu menggunakan nomor Invoice.",
                        style     = MaterialTheme.typography.bodyMedium,
                        color     = Color.White.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        shape  = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF2C2C2C)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier            = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                "Cari detail pembelian kamu disini",
                                fontWeight = FontWeight.Bold,
                                color      = Color.White,
                                fontSize   = 13.sp
                            )

                            OutlinedTextField(
                                value         = invoiceInput,
                                onValueChange = {
                                    invoiceInput = it
                                    hasSearched  = false
                                    notFound     = false
                                    searchResult = null
                                },
                                placeholder = {
                                    Text(
                                        "Masukkan nomor Invoice Kamu (Contoh: LDXXXXXXXXXXXXXX)",
                                        fontSize = 12.sp,
                                        color    = Color.White.copy(alpha = 0.4f)
                                    )
                                },
                                trailingIcon = {
                                    IconButton(onClick = {
                                        val text = clipboardManager.getText()?.text ?: ""
                                        if (text.isNotBlank()) invoiceInput = text
                                    }) {
                                        Icon(
                                            Icons.Rounded.ContentPaste, null,
                                            tint = Color.White.copy(alpha = 0.6f)
                                        )
                                    }
                                },
                                singleLine = true,
                                shape      = RoundedCornerShape(10.dp),
                                modifier   = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Search
                                ),
                                keyboardActions = KeyboardActions(
                                    onSearch = { doSearch() }
                                ),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor   = Color(0xFFFF6B00),
                                    unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                                    focusedTextColor     = Color.White,
                                    unfocusedTextColor   = Color.White,
                                    cursorColor          = Color(0xFFFF6B00)
                                )
                            )

                            Button(
                                onClick  = { doSearch() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape  = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFFF6B00)
                                ),
                                enabled = invoiceInput.isNotBlank() && !isLoading
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(18.dp),
                                        color = Color.White,
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Icon(
                                        Icons.Rounded.Receipt, null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "Cari Invoice",
                                        fontWeight = FontWeight.Bold,
                                        fontSize   = 15.sp,
                                        color      = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (hasSearched && notFound && !isLoading) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape    = RoundedCornerShape(16.dp),
                        colors   = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Row(
                            modifier  = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                Icons.Rounded.SearchOff, null,
                                tint     = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(32.dp)
                            )
                            Column {
                                Text(
                                    "Invoice Tidak Ditemukan",
                                    fontWeight = FontWeight.Bold,
                                    color      = MaterialTheme.colorScheme.error
                                )
                                Text(
                                    "Pastikan nomor invoice yang kamu masukkan sudah benar.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                            }
                        }
                    }
                }

                if (searchResult != null) {
                    val tx = searchResult!!
                    Text(
                        "Detail Transaksi",
                        fontWeight = FontWeight.Bold,
                        style      = MaterialTheme.typography.titleMedium
                    )

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape    = RoundedCornerShape(16.dp),
                        colors   = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier            = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier              = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment     = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Invoice #${tx.invoiceId}",
                                    fontWeight = FontWeight.Bold,
                                    style      = MaterialTheme.typography.bodyMedium
                                )
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = when (tx.status) {
                                        TransactionStatus.SUCCESS    -> Color(0xFF4CAF50).copy(alpha = 0.15f)
                                        TransactionStatus.PENDING    -> Color(0xFFFFC107).copy(alpha = 0.15f)
                                        TransactionStatus.PROCESSING -> Color(0xFF2196F3).copy(alpha = 0.15f)
                                        TransactionStatus.FAILED     -> MaterialTheme.colorScheme.errorContainer
                                    }
                                ) {
                                    Text(
                                        tx.status.name,
                                        modifier   = Modifier.padding(
                                            horizontal = 10.dp, vertical = 4.dp
                                        ),
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize   = 11.sp,
                                        color = when (tx.status) {
                                            TransactionStatus.SUCCESS    -> Color(0xFF4CAF50)
                                            TransactionStatus.PENDING    -> Color(0xFFFFC107)
                                            TransactionStatus.PROCESSING -> Color(0xFF2196F3)
                                            TransactionStatus.FAILED     -> MaterialTheme.colorScheme.error
                                        }
                                    )
                                }
                            }

                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.outlineVariant
                            )

                            TxInvoiceRow("Game",       tx.gameTitle)
                            TxInvoiceRow("Player ID",  tx.playerId)
                            TxInvoiceRow("Item",       tx.item)
                            TxInvoiceRow("Metode",     tx.payment)

                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.outlineVariant
                            )

                            Row(
                                modifier              = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "Total Pembayaran",
                                    fontWeight = FontWeight.ExtraBold,
                                    style      = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    formatRp(tx.totalPrice),
                                    fontWeight = FontWeight.ExtraBold,
                                    color      = Color(0xFFFF6B00),
                                    style      = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape    = RoundedCornerShape(12.dp),
                        colors   = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Row(
                            modifier  = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(Icons.Rounded.Info, null,
                                tint     = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp))
                            Text(
                                "Seluruh Data Transaksi disimpan dengan aman",
                                style    = MaterialTheme.typography.bodySmall,
                                color    = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Tombol Nexus Coin
                    Button(
                        onClick  = { onNavigateToNexusCoin() },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape    = RoundedCornerShape(10.dp),
                        colors   = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1A1A2E)
                        )
                    ) {
                        Icon(Icons.Rounded.MonetizationOn, null,
                            modifier = Modifier.size(18.dp), tint = Color(0xFFFFD700))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Top Up dengan NEXUS Coin",
                            fontWeight = FontWeight.Bold,
                            color      = Color(0xFFFFD700)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TxInvoiceRow(label: String, value: String) {
    Row(
        modifier              = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            style    = MaterialTheme.typography.bodySmall,
            color    = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Text(
            value,
            style      = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            textAlign  = TextAlign.End,
            modifier   = Modifier.weight(1.5f)
        )
    }
}