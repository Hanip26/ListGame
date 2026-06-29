package com.example.listgame.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listgame.data.DummyData
import com.example.listgame.model.TopUpOption
import com.example.listgame.navigation.LocalBackStack
import com.example.listgame.navigation.Route
import com.example.listgame.viewmodel.AppViewModel
import com.example.listgame.model.PackageViewModel

// ── Data Classes ──────────────────────────────────────────────────────────────

data class PaymentMethod(
    val id: String,
    val name: String,
    val description: String,
    val isBestPrice: Boolean = false,
    val isDisabled: Boolean = false
)

data class PaymentGroup(
    val groupName: String,
    val methods: List<PaymentMethod>
)

val validPromoCodes = mapOf(
    "NEXUS10" to 10,
    "HEMAT15" to 15,
    "GAME20"  to 20,
    "DISKON5" to 5
)

// ── Screen Utama ──────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopUpScreen(gameId: Int, appViewModel: AppViewModel) {
    val backStack = LocalBackStack.current
    val game      = DummyData.popularGames.find { it.id == gameId }
    val packageViewModel = remember {
        PackageViewModel()
    }
    LaunchedEffect(gameId) {
        packageViewModel.loadPackages(gameId)
    }
    val packages by packageViewModel.packages.collectAsState()
    // ── Saldo Nexus Coin (sinkron dengan halaman NEXUS Coins) ─────────────────
    val nexusCoinBalance by appViewModel.nexusCoinBalance.collectAsState()

    // ── State Form ────────────────────────────────────────────────────────────
    var username        by remember { mutableStateOf("") }
    var isUsernameError by remember { mutableStateOf(false) }
    var playerId        by remember { mutableStateOf("") }
    var isPlayerIdError by remember { mutableStateOf(false) }
    var selectedOption  by remember { mutableStateOf<TopUpOption?>(null) }
    var selectedPayment by remember { mutableStateOf<PaymentMethod?>(null) }
    var quantity        by remember { mutableStateOf(1) }
    var expandedGroup   by remember { mutableStateOf<String?>(null) }

    // Kode Promo
    var promoInput     by remember { mutableStateOf("") }
    var promoDiscount  by remember { mutableStateOf(0) }
    var promoMessage   by remember { mutableStateOf("") }
    var promoIsValid   by remember { mutableStateOf<Boolean?>(null) }
    var showPromoSheet by remember { mutableStateOf(false) }

    // Detail Kontak
    var whatsappNumber by remember { mutableStateOf("") }
    var isWaError      by remember { mutableStateOf(false) }

    // ── Kalkulasi Harga ───────────────────────────────────────────────────────
    val basePrice      = selectedOption?.price
        ?.replace("Rp ", "")?.replace(".", "")?.toIntOrNull() ?: 0
    val subtotal       = basePrice * quantity
    val discountAmount = (subtotal * promoDiscount) / 100
    val adminFee       = if (selectedPayment?.id == "nexus_coin") 0 else 675
    val totalPrice     = subtotal - discountAmount + adminFee

    fun formatRp(value: Int) = "Rp ${"%,d".format(value).replace(',', '.')}"
    fun formatCoin(value: Int) = "%,d".format(value).replace(',', '.')

    // ── Cek Saldo Nexus Coin ──────────────────────────────────────────────────
    // Jika bayar dengan Nexus Coin, biaya admin = 0, jadi total yang harus
    // dipotong dari saldo adalah subtotal dikurangi diskon.
    val nexusCoinRequiredAmount = subtotal - discountAmount
    val hasEnoughNexusCoin = selectedOption == null ||
            nexusCoinBalance >= nexusCoinRequiredAmount

    // ── Grup Pembayaran ───────────────────────────────────────────────────────
    val paymentGroups = listOf(
        PaymentGroup("Nexus Coin", listOf(
            PaymentMethod(
                id          = "nexus_coin",
                name        = "Nexus Coin",
                description = if (hasEnoughNexusCoin)
                    "Bebas Biaya Admin • Saldo: ${formatCoin(nexusCoinBalance)} Coin"
                else
                    "Saldo tidak cukup • Saldo: ${formatCoin(nexusCoinBalance)} Coin",
                isBestPrice = true,
                isDisabled  = !hasEnoughNexusCoin
            )
        )),
        PaymentGroup("QRIS", listOf(
            PaymentMethod(
                "qris", "QRIS",
                "DANA, OVO, ShopeePay, GoPay, BCA, dll.",
                isBestPrice = true
            )
        )),
        PaymentGroup("E-Wallet", listOf(
            PaymentMethod("ovo",       "OVO",       "Bayar dengan OVO"),
            PaymentMethod("gopay",     "GoPay",     "Bayar dengan GoPay"),
            PaymentMethod("dana",      "DANA",      "Bayar dengan DANA"),
            PaymentMethod("shopeepay", "ShopeePay", "Bayar dengan ShopeePay")
        )),
        PaymentGroup("Virtual Account", listOf(
            PaymentMethod("va_bca",     "BCA Virtual Account",     "ATM / m-Banking BCA"),
            PaymentMethod("va_bni",     "BNI Virtual Account",     "ATM / m-Banking BNI"),
            PaymentMethod("va_bri",     "BRI Virtual Account",     "ATM / m-Banking BRI"),
            PaymentMethod("va_mandiri", "Mandiri Virtual Account", "ATM / m-Banking Mandiri"),
            PaymentMethod("va_bsi",     "BSI Virtual Account",     "ATM / m-Banking BSI")
        )),
        PaymentGroup("Convenience Store", listOf(
            PaymentMethod("alfamart",  "Alfamart",  "Bayar di kasir Alfamart"),
            PaymentMethod("indomaret", "Indomaret", "Bayar di kasir Indomaret"),
            PaymentMethod("lawson",    "Lawson",    "Bayar di kasir Lawson")
        )),
        PaymentGroup("Transfer Bank", listOf(
            PaymentMethod("tf_bri",     "Transfer BRI",     "Rekening BRI"),
            PaymentMethod("tf_bca",     "Transfer BCA",     "Rekening BCA"),
            PaymentMethod("tf_mandiri", "Transfer Mandiri", "Rekening Mandiri")
        ))
    )

    // Jika nominal/diskon berubah sehingga saldo Nexus Coin yang tadinya
    // cukup jadi tidak cukup lagi, batalkan pilihan Nexus Coin secara otomatis.
    LaunchedEffect(selectedPayment?.id, hasEnoughNexusCoin) {
        if (selectedPayment?.id == "nexus_coin" && !hasEnoughNexusCoin) {
            selectedPayment = null
        }
    }

    // ── Validasi Promo ────────────────────────────────────────────────────────
    fun applyPromo() {
        val code = promoInput.trim().uppercase()
        if (validPromoCodes.containsKey(code)) {
            promoDiscount = validPromoCodes[code]!!
            promoMessage  = "Promo berhasil! Diskon $promoDiscount% diterapkan."
            promoIsValid  = true
        } else {
            promoDiscount = 0
            promoMessage  = "Kode promo tidak valid atau sudah kadaluarsa."
            promoIsValid  = false
        }
    }

    // ── Bottom Sheet Promo ────────────────────────────────────────────────────
    if (showPromoSheet) {
        ModalBottomSheet(onDismissRequest = { showPromoSheet = false }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "Promo Tersedia",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
                HorizontalDivider()
                validPromoCodes.forEach { (code, persen) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                promoInput = code
                                applyPromo()
                                showPromoSheet = false
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    code,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    "Diskon $persen% untuk semua nominal",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.primary
                            ) {
                                Text(
                                    "$persen% OFF",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.padding(
                                        horizontal = 10.dp, vertical = 6.dp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // ── Scaffold ──────────────────────────────────────────────────────────────
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Top Up ${game?.title ?: "Game"}",
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
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
        },
        bottomBar = {
            Surface(shadowElevation = 12.dp) {
                val isNexusCoinInsufficient =
                    selectedPayment?.id == "nexus_coin" && !hasEnoughNexusCoin

                Button(
                    onClick = {
                        var hasError = false
                        if (username.isBlank()) {
                            isUsernameError = true
                            hasError = true
                        }
                        if (playerId.isBlank()) {
                            isPlayerIdError = true
                            hasError = true
                        }
                        if (whatsappNumber.isBlank()) {
                            isWaError = true
                            hasError = true
                        }
                        if (!hasError &&
                            selectedOption != null &&
                            selectedPayment != null &&
                            !isNexusCoinInsufficient
                        ) {
                            backStack.add(
                                Route.OrderConfirmation(
                                    gameId         = gameId,
                                    username       = username,
                                    playerId       = playerId,
                                    amount         = selectedOption!!.amount,
                                    quantity       = quantity,
                                    paymentName    = selectedPayment!!.name,
                                    totalPrice     = totalPrice,
                                    subtotal       = subtotal,
                                    adminFee       = adminFee,
                                    discountAmount = discountAmount,
                                    promoDiscount  = promoDiscount,
                                    whatsappNumber = whatsappNumber
                                )
                            )
                        }
                    },
                    enabled = selectedOption != null && selectedPayment != null &&
                            !isNexusCoinInsufficient,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(
                        Icons.Rounded.ShoppingCart, null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = when {
                            selectedOption == null  -> "Pilih Nominal Terlebih Dahulu"
                            selectedPayment == null -> "Pilih Metode Pembayaran"
                            isNexusCoinInsufficient -> "Saldo Nexus Coin Tidak Cukup"
                            else -> "Pesan Sekarang! • ${formatRp(totalPrice)}"
                        },
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
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
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            // ── STEP 1: Informasi Akun ────────────────────────────────────
            StepCard(1, "Masukkan Informasi Akun") {
                OutlinedTextField(
                    value = username,
                    onValueChange = {
                        username = it
                        isUsernameError = false
                    },
                    label = { Text("Username / Nickname") },
                    placeholder = { Text("Contoh: Affun") },
                    leadingIcon = { Icon(Icons.Rounded.Person, null) },
                    isError = isUsernameError,
                    supportingText = {
                        if (isUsernameError) Text("Username tidak boleh kosong!")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    value = playerId,
                    onValueChange = {
                        playerId = it
                        isPlayerIdError = false
                    },
                    label = { Text("Player ID") },
                    placeholder = { Text("Contoh: 123456789") },
                    leadingIcon = { Icon(Icons.Rounded.Tag, null) },
                    isError = isPlayerIdError,
                    supportingText = {
                        if (isPlayerIdError) Text("Player ID tidak boleh kosong!")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
            }

            // ── STEP 2: Pilih Nominal ─────────────────────────────────────
            StepCard(2, "Pilih Nominal") {
                if (packages.isEmpty()) {
                    Text(
                        "Top up tidak tersedia untuk game ini.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 600.dp)
                    ) {
                        items(packages) { pkg ->
                            val option = TopUpOption(
                                amount = pkg.amount,
                                price = "Rp ${pkg.price}",
                                bonus = pkg.bonus ?: ""
                            )

                            TopUpOptionCard(
                                option = option,
                                isSelected = selectedOption?.amount == option.amount,
                                onClick = {
                                    selectedOption = option
                                }
                            )
                        }
                    }
                }
            }

            // ── STEP 3: Jumlah Pembelian ──────────────────────────────────
            StepCard(3, "Masukkan Jumlah Pembelian") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = quantity.toString(),
                        onValueChange = { v ->
                            val n = v.toIntOrNull()
                            if (n != null && n in 1..99) quantity = n
                            else if (v.isEmpty()) quantity = 1
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        label = { Text("Jumlah") }
                    )
                    FilledIconButton(
                        onClick = { if (quantity > 1) quantity-- },
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Icon(
                            Icons.Rounded.Remove, "Kurang",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                    FilledIconButton(
                        onClick = { if (quantity < 99) quantity++ },
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Icon(
                            Icons.Rounded.Add, "Tambah",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                if (selectedOption != null) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Subtotal ($quantity item):",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            formatRp(subtotal),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // ── STEP 4: Pilih Pembayaran ──────────────────────────────────
            StepCard(4, "Pilih Pembayaran") {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    paymentGroups.forEach { group ->
                        PaymentGroupSection(
                            group           = group,
                            selectedPayment = selectedPayment,
                            isExpanded      = expandedGroup == group.groupName,
                            onToggle        = {
                                expandedGroup =
                                    if (expandedGroup == group.groupName) null
                                    else group.groupName
                            },
                            onSelectPayment = { method ->
                                selectedPayment = method
                                expandedGroup   = null
                            },
                            onTopUpClick = { backStack.add(Route.NexusCoinTopUp) }
                        )
                    }
                }
            }

            // ── STEP 5: Kode Promo ────────────────────────────────────────
            StepCard(5, "Kode Promo") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = promoInput,
                        onValueChange = {
                            promoInput    = it.uppercase()
                            promoIsValid  = null
                            promoMessage  = ""
                            promoDiscount = 0
                        },
                        placeholder = { Text("Ketik Kode Promo Kamu") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f),
                        isError = promoIsValid == false,
                        trailingIcon = {
                            if (promoIsValid == true)
                                Icon(
                                    Icons.Rounded.CheckCircle, null,
                                    tint = Color(0xFF4CAF50)
                                )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text
                        )
                    )
                    Button(
                        onClick  = { applyPromo() },
                        shape    = RoundedCornerShape(12.dp),
                        enabled  = promoInput.isNotBlank()
                    ) {
                        Text("Gunakan", fontWeight = FontWeight.Bold)
                    }
                }

                if (promoMessage.isNotEmpty()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = if (promoIsValid == true)
                                Icons.Rounded.CheckCircle
                            else Icons.Rounded.Info,
                            contentDescription = null,
                            tint = if (promoIsValid == true) Color(0xFF4CAF50)
                            else MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            promoMessage,
                            style = MaterialTheme.typography.labelMedium,
                            color = if (promoIsValid == true) Color(0xFF4CAF50)
                            else MaterialTheme.colorScheme.error
                        )
                    }
                }

                OutlinedButton(
                    onClick  = { showPromoSheet = true },
                    shape    = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Rounded.LocalOffer, null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Pakai Promo Yang Tersedia")
                }
            }

            // ── STEP 6: Detail Kontak ─────────────────────────────────────
            StepCard(6, "Detail Kontak") {
                Text(
                    "No. WhatsApp",
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyMedium
                )
                OutlinedTextField(
                    value = whatsappNumber,
                    onValueChange = {
                        whatsappNumber = it
                        isWaError = false
                    },
                    leadingIcon = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(start = 12.dp, end = 4.dp)
                        ) {
                            Text("🇮🇩", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "+62",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            VerticalDivider(
                                modifier = Modifier.height(24.dp),
                                color = MaterialTheme.colorScheme.outlineVariant
                            )
                        }
                    },
                    placeholder = { Text("8xxxxxxxxx") },
                    isError = isWaError,
                    supportingText = {
                        if (isWaError)
                            Text("Nomor WhatsApp tidak boleh kosong!")
                        else
                            Text(
                                "**Nomor ini akan dihubungi jika terjadi masalah",
                                style = MaterialTheme.typography.labelSmall
                            )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone
                    )
                )

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Rounded.Info, null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            "**Nomor ini akan kami hubungi jika terjadi masalah",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // ── Ringkasan Pesanan ─────────────────────────────────────────
            if (selectedOption != null) {
                OrderSummaryCard(
                    game            = game?.title ?: "-",
                    selectedOption  = selectedOption!!,
                    quantity        = quantity,
                    selectedPayment = selectedPayment,
                    subtotal        = subtotal,
                    discountAmount  = discountAmount,
                    promoDiscount   = promoDiscount,
                    adminFee        = adminFee,
                    totalPrice      = totalPrice,
                    formatRp        = ::formatRp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

// ── Komponen: Logo Pembayaran ─────────────────────────────────────────────────

@Composable
fun PaymentLogo(methodId: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(44.dp)
            .clip(RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        when (methodId) {

            "nexus_coin" -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF1A1A2E)),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(30.dp)) {
                    drawCircle(color = Color(0xFFFFD700),
                        radius = size.minDimension / 2)
                    drawCircle(color = Color(0xFFFFA000),
                        radius = size.minDimension / 2 - 3.dp.toPx())
                }
                Text("N", color = Color(0xFF1A1A2E),
                    fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
            }

            "qris" -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)) {
                    val cell = size.width / 7f
                    val qrColor = Color(0xFF1A237E)
                    drawQrCorner(this, 0f, 0f, cell, qrColor)
                    drawQrCorner(this, 4 * cell, 0f, cell, qrColor)
                    drawQrCorner(this, 0f, 4 * cell, cell, qrColor)
                    listOf(
                        Pair(3f, 3f), Pair(4f, 3f), Pair(3f, 4f),
                        Pair(5f, 5f), Pair(6f, 5f), Pair(5f, 6f),
                        Pair(6f, 6f), Pair(4f, 5f)
                    ).forEach { (cx, cy) ->
                        drawRect(
                            color = qrColor,
                            topLeft = Offset(cx * cell + 1, cy * cell + 1),
                            size = Size(cell - 2, cell - 2)
                        )
                    }
                }
            }

            "ovo" -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF4C2A86)),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Canvas(modifier = Modifier.size(10.dp)) {
                        drawCircle(color = Color(0xFF00D4AA))
                    }
                    Spacer(modifier = Modifier.width(2.dp))
                    Text("OVO", color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 10.sp, letterSpacing = 0.5.sp)
                }
            }

            "gopay" -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF00AED6)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Go", color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 10.sp, lineHeight = 11.sp)
                    Text("Pay", color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 10.sp, lineHeight = 11.sp)
                }
            }

            "dana" -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF118EEA)),
                contentAlignment = Alignment.Center
            ) {
                Text("DANA", color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 9.sp, letterSpacing = 0.3.sp)
            }

            "shopeepay" -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFEE4D2D)),
                contentAlignment = Alignment.Center
            ) {
                Text("SPay", color = Color.White,
                    fontWeight = FontWeight.ExtraBold, fontSize = 9.sp)
            }

            "va_bca", "tf_bca" -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF003D82)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("BCA", color = Color.White,
                        fontWeight = FontWeight.ExtraBold, fontSize = 11.sp)
                    Canvas(modifier = Modifier
                        .width(28.dp)
                        .height(3.dp)) {
                        drawRect(color = Color(0xFF009FE3),
                            size = Size(size.width * 0.6f, size.height))
                    }
                }
            }

            "va_bni" -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFF6600)),
                contentAlignment = Alignment.Center
            ) {
                Text("BNI", color = Color.White,
                    fontWeight = FontWeight.ExtraBold, fontSize = 11.sp)
            }

            "va_bri", "tf_bri" -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF0066AE)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("BRI", color = Color.White,
                        fontWeight = FontWeight.ExtraBold, fontSize = 11.sp)
                    Box(modifier = Modifier
                        .size(width = 24.dp, height = 3.dp)
                        .background(Color(0xFFFFD700)))
                }
            }

            "va_mandiri", "tf_mandiri" -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF003087)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(modifier = Modifier.width(32.dp)) {
                        listOf(
                            Color(0xFFFFD700), Color(0xFF003087),
                            Color(0xFFFFD700), Color(0xFF003087),
                            Color(0xFFFFD700)
                        ).forEach { c ->
                            Box(modifier = Modifier
                                .weight(1f)
                                .height(4.dp)
                                .background(c))
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("mandiri", color = Color(0xFFFFD700),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 7.sp, letterSpacing = 0.2.sp)
                }
            }

            "va_bsi" -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF3D7A4A)),
                contentAlignment = Alignment.Center
            ) {
                Text("BSI", color = Color.White,
                    fontWeight = FontWeight.ExtraBold, fontSize = 11.sp)
            }

            "alfamart" -> Box(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFE8192C)))
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .align(Alignment.BottomCenter)
                    .background(Color(0xFF003087)))
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("alfa\nmart", color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 8.sp, textAlign = TextAlign.Center,
                        lineHeight = 9.sp)
                }
            }

            "indomaret" -> Box(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFE31E24)))
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .align(Alignment.TopCenter)
                    .background(Color(0xFFFDD835)))
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("indomaret", color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 7.sp, letterSpacing = 0.2.sp)
                }
            }

            "lawson" -> Box(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(Color(0xFF0066B3)))
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(Color.White))
                }
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Lawson", color = Color.White,
                        fontWeight = FontWeight.ExtraBold, fontSize = 8.sp)
                }
            }

            else -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.AccountBalanceWallet, null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp))
            }
        }
    }
}

// Helper QR corner
fun drawQrCorner(
    scope: DrawScope,
    x: Float,
    y: Float,
    cell: Float,
    color: Color
) {
    with(scope) {
        drawRect(color = color,
            topLeft = Offset(x, y),
            size = Size(cell * 3, cell * 3))
        drawRect(color = Color.White,
            topLeft = Offset(x + cell, y + cell),
            size = Size(cell, cell))
        drawRect(color = color,
            topLeft = Offset(x + cell * 1.2f, y + cell * 1.2f),
            size = Size(cell * 0.6f, cell * 0.6f))
    }
}

// ── Komponen: Ringkasan Pesanan ───────────────────────────────────────────────

@Composable
fun OrderSummaryCard(
    game: String,
    selectedOption: TopUpOption,
    quantity: Int,
    selectedPayment: PaymentMethod?,
    subtotal: Int,
    discountAmount: Int,
    promoDiscount: Int,
    adminFee: Int,
    totalPrice: Int,
    formatRp: (Int) -> String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Rounded.Receipt, null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp))
                Text("Ringkasan Pesanan",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium)
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(game,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium)
                    Text(selectedOption.amount,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    if (selectedOption.bonus.isNotEmpty()) {
                        Text(selectedOption.bonus,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF4CAF50),
                            fontWeight = FontWeight.Bold)
                    }
                }
                Text(selectedOption.price,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary)
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )

            SummaryRow(
                "Metode Pembayaran",
                selectedPayment?.name ?: "Belum dipilih",
                valueColor = if (selectedPayment == null)
                    MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.onSurface
            )
            SummaryRow("Harga", selectedOption.price)
            SummaryRow("Jumlah Pembelian", "$quantity item")

            if (promoDiscount > 0) {
                SummaryRow(
                    "Diskon $promoDiscount%",
                    "- ${formatRp(discountAmount)}",
                    valueColor = Color(0xFF4CAF50)
                )
            }

            SummaryRow(
                "Biaya Admin",
                if (adminFee == 0) "Gratis" else formatRp(adminFee),
                valueColor = if (adminFee == 0) Color(0xFF4CAF50)
                else MaterialTheme.colorScheme.onSurface
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Total Pembayaran",
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.titleMedium)
                Text(formatRp(totalPrice),
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

// ── Komponen: Baris Ringkasan ─────────────────────────────────────────────────

@Composable
fun SummaryRow(
    label: String,
    value: String,
    valueColor: Color = Color.Unspecified
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f))
        Text(value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = if (valueColor == Color.Unspecified)
                MaterialTheme.colorScheme.onSurface else valueColor,
            textAlign = TextAlign.End)
    }
}

// ── Komponen: StepCard ────────────────────────────────────────────────────────

@Composable
fun StepCard(
    stepNumber: Int,
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(stepNumber.toString(),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp)
                }
                Text(title,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium)
            }
            content()
        }
    }
}

// ── Komponen: Kartu Nominal ───────────────────────────────────────────────────

@Composable
fun TopUpOptionCard(
    option: TopUpOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            if (isSelected) 6.dp else 2.dp
        ),
        border = if (isSelected) CardDefaults.outlinedCardBorder() else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(Icons.Rounded.Diamond, null,
                tint = if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(28.dp))
            Text(option.amount,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                textAlign = TextAlign.Center)
            Text(option.price,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary)
            if (option.bonus.isNotEmpty()) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFF4CAF50).copy(alpha = 0.15f)
                ) {
                    Text(option.bonus,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32),
                        modifier = Modifier.padding(
                            horizontal = 6.dp, vertical = 2.dp
                        ))
                }
            }
        }
    }
}

// ── Komponen: Grup Pembayaran ─────────────────────────────────────────────────

@Composable
fun PaymentGroupSection(
    group: PaymentGroup,
    selectedPayment: PaymentMethod?,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    onSelectPayment: (PaymentMethod) -> Unit,
    onTopUpClick: (() -> Unit)? = null
) {
    val isGroupSelected = group.methods.any { it.id == selectedPayment?.id }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = if (isGroupSelected) 2.dp else 1.dp,
                color = if (isGroupSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggle() }
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (isGroupSelected) {
                    Icon(Icons.Rounded.CheckCircle, null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp))
                }
                Column {
                    Text(group.groupName,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyLarge)
                    if (isGroupSelected) {
                        Text(selectedPayment?.name ?: "",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
            Icon(
                imageVector = if (isExpanded) Icons.Rounded.KeyboardArrowUp
                else Icons.Rounded.KeyboardArrowDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (isExpanded) {
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            group.methods.forEachIndexed { index, method ->
                PaymentMethodItem(
                    method       = method,
                    isSelected   = selectedPayment?.id == method.id,
                    onClick      = { if (!method.isDisabled) onSelectPayment(method) },
                    onTopUpClick = onTopUpClick
                )
                if (index < group.methods.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = MaterialTheme.colorScheme.outlineVariant
                            .copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}

// ── Komponen: Item Metode Pembayaran ──────────────────────────────────────────

@Composable
fun PaymentMethodItem(
    method: PaymentMethod,
    isSelected: Boolean,
    onClick: () -> Unit,
    onTopUpClick: (() -> Unit)? = null
) {
    val contentAlpha = if (method.isDisabled) 0.45f else 1f

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !method.isDisabled) { onClick() }
            .background(
                if (isSelected)
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                else Color.Transparent
            )
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ✅ Logo custom per metode pembayaran
            PaymentLogo(
                methodId = method.id,
                modifier = Modifier.alpha(contentAlpha)
            )

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(method.name,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = contentAlpha))
                    if (method.isBestPrice && !method.isDisabled) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFFFF6B00)
                        ) {
                            Text("BEST PRICE",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(
                                    horizontal = 4.dp, vertical = 2.dp
                                ))
                        }
                    }
                }
                Text(method.description,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (method.isDisabled)
                        MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.onSurfaceVariant)
            }

            RadioButton(
                selected = isSelected,
                onClick  = onClick,
                enabled  = !method.isDisabled
            )
        }

        // ── Saldo tidak cukup: tampilkan tombol Top Up ────────────────────
        if (method.isDisabled && method.id == "nexus_coin" && onTopUpClick != null) {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick  = onTopUpClick,
                shape    = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth().height(36.dp),
                colors   = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFFFFA726)
                )
            ) {
                Icon(Icons.Rounded.AddCircle, null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Top Up Nexus Coin", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }
    }
}

// ── Komponen: Baris Info Dialog ───────────────────────────────────────────────

@Composable
fun InfoRow(
    label: String,
    value: String,
    valueColor: Color = Color.Unspecified,
    bold: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("$label:",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
            color = if (valueColor == Color.Unspecified)
                MaterialTheme.colorScheme.onSurface else valueColor)
    }
}