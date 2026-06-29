package com.example.listgame.ui.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listgame.model.TransactionStatus
import com.example.listgame.navigation.LocalBackStack
import com.example.listgame.viewmodel.AppViewModel
import com.example.listgame.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    appViewModel : AppViewModel,
    onLogout     : () -> Unit
) {
    val backStack        = LocalBackStack.current
    val user             by authViewModel.currentUser.collectAsState()
    val state            by authViewModel.profileState.collectAsState()
    val transactions     by appViewModel.transactions.collectAsState()
    val nexusCoinBalance by appViewModel.nexusCoinBalance.collectAsState()

    val totalRevenue = transactions.filter { it.status == TransactionStatus.SUCCESS }
        .sumOf { it.totalPrice }

    var isProfileExpanded  by remember { mutableStateOf(false) }
    var isSecurityExpanded by remember { mutableStateOf(false) }
    var isHelpExpanded     by remember { mutableStateOf(false) }
    var isPrivacyExpanded  by remember { mutableStateOf(false) }
    var showLogoutDialog   by remember { mutableStateOf(false) }
    var showTierDialog     by remember { mutableStateOf(false) }

    LaunchedEffect(user) {
        user?.let { authViewModel.initProfileForm(it) }
    }

    val username           = user?.username ?: ""
    val nexusCoinsText     = "%,d".format(nexusCoinBalance).replace(",", ".")
    val totalPembelianText = "Rp ${"%,d".format(totalRevenue).replace(',', '.')}"

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

    // ── Dialog Tier ───────────────────────────────────────────────────────────
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
                        "Total Pembelian Kamu Saat Ini:\n$totalPembelianText",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = userTierColor
                    )
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )
                    TierItem("1. Member Junior",     "Rp 0 - Rp 100.000",           userTier == "Member Junior",     Color(0xFF00E676))
                    TierItem("2. Member Senior",     "Rp 100.000 - Rp 500.000",     userTier == "Member Senior",     Color(0xFF00B0FF))
                    TierItem("3. Member Terhormat",  "Rp 500.000 - Rp 1.000.000",   userTier == "Member Terhormat",  Color(0xFFE040FB))
                    TierItem("4. Member Juragan",    "Rp 1.000.000 - Rp 2.000.000", userTier == "Member Juragan",    Color(0xFFFF9100))
                    TierItem("5. Sultan Tier Member","Di atas Rp 2.000.000",         userTier == "Sultan Tier Member",Color(0xFFFF1744))
                }
            },
            shape = RoundedCornerShape(24.dp),
            containerColor = MaterialTheme.colorScheme.surface
        )
    }

    // ── Dialog Logout ─────────────────────────────────────────────────────────
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title   = { Text("Konfirmasi Logout") },
            text    = { Text("Yakin ingin keluar dari akun ini?") },
            confirmButton = {
                TextButton(onClick = { showLogoutDialog = false; onLogout() }) {
                    Text("Keluar",
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Batal",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold)
                }
            },
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(6.dp)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profil", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = { backStack.removeLastOrNull() }) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {

            // ── Header Avatar ─────────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(contentAlignment = Alignment.BottomEnd) {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text       = (user?.displayName ?: "?").take(1).uppercase(),
                            fontSize   = 38.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color      = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .clickable { isProfileExpanded = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Rounded.Edit, null,
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.onPrimary)
                    }
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    text       = user?.displayName ?: "-",
                    style      = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text  = user?.email ?: "-",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.height(8.dp))

                // Tier chip
                Surface(
                    color        = userTierColor.copy(alpha = 0.15f),
                    contentColor = userTierColor,
                    shape        = RoundedCornerShape(20.dp),
                    modifier     = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { showTierDialog = true }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Rounded.Verified, null, modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(userTier, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // ── Info Nexus Coin + Total Pembelian ─────────────────────────────
            Card(
                modifier  = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape     = RoundedCornerShape(12.dp),
                colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Rounded.MonetizationOn, null,
                        tint = Color(0xFFFFA000),
                        modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Column(Modifier.weight(1f)) {
                        Text("NEXUS Coins",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(nexusCoinsText,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp)
                    }
                    VerticalDivider(
                        modifier = Modifier
                            .height(28.dp)
                            .padding(horizontal = 8.dp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                    )
                    Icon(Icons.Rounded.ShoppingBag, null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Total Pembelian",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(totalPembelianText,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // ── Seksi: AKUN ───────────────────────────────────────────────────
            SettingsSectionHeader("AKUN")

            SettingsGroup {
                // Ubah Profil
                SettingsRow(
                    icon     = Icons.Rounded.Person,
                    iconTint = MaterialTheme.colorScheme.primary,
                    title    = "Ubah Profil",
                    subtitle = "Nama, email, nomor HP, bio",
                    expanded = isProfileExpanded,
                    onClick  = { isProfileExpanded = !isProfileExpanded }
                )

                if (isProfileExpanded) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Column(modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)) {

                        Row(Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            ProfileField(
                                modifier      = Modifier.weight(1f),
                                label         = "Nama Tampilan",
                                value         = state.displayName,
                                onValueChange = authViewModel::onProfileDisplayNameChange,
                                placeholder   = "Nama tampilan"
                            )
                            ProfileField(
                                modifier      = Modifier.weight(1f),
                                label         = "Username",
                                value         = username,
                                onValueChange = {},
                                placeholder   = "-",
                                enabled       = false
                            )
                        }

                        Row(Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            ProfileField(
                                modifier      = Modifier.weight(1f),
                                label         = "Alamat Email",
                                value         = state.email,
                                onValueChange = authViewModel::onProfileEmailChange,
                                placeholder   = "email@contoh.com",
                                keyboardType  = KeyboardType.Email,
                                errorText     = state.emailError
                            )
                            ProfileField(
                                modifier      = Modifier.weight(1f),
                                label         = "No. Handphone",
                                value         = state.phone,
                                onValueChange = authViewModel::onProfilePhoneChange,
                                placeholder   = "08xx-xxxx-xxxx",
                                keyboardType  = KeyboardType.Phone
                            )
                        }

                        ProfileField(
                            modifier      = Modifier.fillMaxWidth(),
                            label         = "Bio Singkat",
                            value         = state.bio,
                            onValueChange = authViewModel::onProfileBioChange,
                            placeholder   = "Ceritakan sedikit tentang dirimu...",
                            maxLines      = 3,
                            singleLine    = false
                        )

                        if (state.profileError != null) {
                            Text(state.profileError!!,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall)
                        }
                        if (state.profileSuccess != null) {
                            Text(state.profileSuccess!!,
                                color = Color(0xFF4CAF50),
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold)
                        }

                        Button(
                            onClick  = { authViewModel.submitProfileUpdate(username) },
                            enabled  = !state.isProfileLoading,
                            shape    = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth().height(48.dp)
                        ) {
                            if (state.isProfileLoading) {
                                CircularProgressIndicator(Modifier.size(18.dp),
                                    strokeWidth = 2.dp,
                                    color = MaterialTheme.colorScheme.onPrimary)
                            } else {
                                Text("Simpan Perubahan", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // ── Seksi: KEAMANAN & BANTUAN ─────────────────────────────────────
            SettingsSectionHeader("KEAMANAN & BANTUAN")

            SettingsGroup {
                // Ubah Kata Sandi
                SettingsRow(
                    icon     = Icons.Rounded.Lock,
                    iconTint = Color(0xFF5C6BC0),
                    title    = "Ubah Kata Sandi",
                    subtitle = "Perbarui kata sandi secara berkala",
                    expanded = isSecurityExpanded,
                    onClick  = { isSecurityExpanded = !isSecurityExpanded }
                )

                if (isSecurityExpanded) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Column(modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)) {

                        PasswordField(
                            modifier      = Modifier.fillMaxWidth(),
                            label         = "Kata Sandi Saat Ini",
                            placeholder   = "Masukkan kata sandi lama",
                            value         = state.currentPassword,
                            onValueChange = authViewModel::onCurrentPasswordChange,
                            isVisible     = state.showCurrentPass,
                            onToggle      = authViewModel::onToggleCurrentPassVisibility
                        )

                        Row(Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            PasswordField(
                                modifier      = Modifier.weight(1f),
                                label         = "Kata Sandi Baru",
                                placeholder   = "Sandi baru",
                                value         = state.newPassword,
                                onValueChange = authViewModel::onNewPasswordChange,
                                isVisible     = state.showNewPass,
                                onToggle      = authViewModel::onToggleNewPassVisibility
                            )
                            PasswordField(
                                modifier      = Modifier.weight(1f),
                                label         = "Konfirmasi Sandi",
                                placeholder   = "Ulangi sandi",
                                value         = state.confirmNewPassword,
                                onValueChange = authViewModel::onConfirmNewPasswordChange,
                                isVisible     = state.showNewPass,
                                onToggle      = {}
                            )
                        }

                        if (state.passwordError != null) {
                            Text(state.passwordError!!,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall)
                        }
                        if (state.passwordSuccess != null) {
                            Text(state.passwordSuccess!!,
                                color = Color(0xFF4CAF50),
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold)
                        }

                        Button(
                            onClick  = { authViewModel.submitChangePassword(username) },
                            enabled  = !state.isPasswordLoading,
                            shape    = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            colors   = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary)
                        ) {
                            if (state.isPasswordLoading) {
                                CircularProgressIndicator(Modifier.size(18.dp),
                                    strokeWidth = 2.dp,
                                    color = MaterialTheme.colorScheme.onSecondary)
                            } else {
                                Text("Perbarui Kata Sandi", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                // Pusat Bantuan
                SettingsRow(
                    icon     = Icons.Rounded.Info,
                    iconTint = Color(0xFF26A69A),
                    title    = "Pusat Bantuan",
                    subtitle = "FAQ dan dukungan pelanggan",
                    expanded = isHelpExpanded,
                    onClick  = { isHelpExpanded = !isHelpExpanded }
                )

                if (isHelpExpanded) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        HelpFaqItem(
                            question = "Bagaimana cara melakukan top up NEXUS Coins?",
                            answer   = "Buka menu Top Up, pilih nominal yang diinginkan, lalu selesaikan pembayaran melalui metode yang tersedia. Saldo akan otomatis bertambah setelah pembayaran berhasil."
                        )
                        HelpFaqItem(
                            question = "Transaksi saya gagal tapi saldo sudah terpotong, bagaimana?",
                            answer   = "Tunggu maksimal 24 jam untuk proses otomatis. Jika saldo belum kembali, hubungi tim dukungan melalui email atau live chat dengan menyertakan ID transaksi."
                        )
                        HelpFaqItem(
                            question = "Bagaimana cara menghubungi tim dukungan?",
                            answer   = "Kamu bisa menghubungi kami melalui email support@nexus.id atau live chat yang tersedia setiap hari pukul 09.00 - 21.00 WIB."
                        )
                    }
                }

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                // Kebijakan Privasi
                SettingsRow(
                    icon     = Icons.Rounded.Shield,
                    iconTint = Color(0xFF8D6E63),
                    title    = "Kebijakan Privasi",
                    subtitle = "Ketentuan penggunaan NEXUS",
                    expanded = isPrivacyExpanded,
                    onClick  = { isPrivacyExpanded = !isPrivacyExpanded }
                )

                if (isPrivacyExpanded) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            "Kami berkomitmen untuk melindungi data pribadi pengguna NEXUS. Berikut ringkasan kebijakan privasi kami:",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        PrivacyPoint("Data yang dikumpulkan hanya digunakan untuk keperluan layanan, verifikasi akun, dan peningkatan kualitas aplikasi.")
                        PrivacyPoint("Informasi pribadi tidak akan dibagikan kepada pihak ketiga tanpa persetujuan pengguna, kecuali diwajibkan oleh hukum.")
                        PrivacyPoint("Pengguna dapat meminta penghapusan akun dan data terkait melalui Pusat Bantuan.")
                        PrivacyPoint("Kebijakan ini dapat diperbarui sewaktu-waktu dan akan diinformasikan melalui aplikasi.")
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // ── Seksi: SESI ───────────────────────────────────────────────────
            SettingsGroup {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showLogoutDialog = true }
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.errorContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.AutoMirrored.Rounded.Logout, null,
                            tint     = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(18.dp))
                    }
                    Text(
                        "Log Out",
                        style      = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color      = MaterialTheme.colorScheme.error,
                        modifier   = Modifier.weight(1f)
                    )
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

// ── Helper UI Components ──────────────────────────────────────────────────────

@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        text     = title,
        style    = MaterialTheme.typography.labelSmall,
        color    = MaterialTheme.colorScheme.onSurfaceVariant,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
    )
}

@Composable
private fun SettingsGroup(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape     = RoundedCornerShape(14.dp),
        colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(content = content)
    }
}

@Composable
private fun SettingsRow(
    icon    : ImageVector,
    iconTint: Color,
    title   : String,
    subtitle: String,
    expanded: Boolean = false,
    onClick : () -> Unit = {}
) {
    val rotation by animateFloatAsState(targetValue = if (expanded) 180f else 0f, label = "arrowRotation")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(iconTint.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null,
                tint     = iconTint,
                modifier = Modifier.size(18.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(title,
                style      = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold)
            Text(subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Icon(Icons.Rounded.KeyboardArrowDown, null,
            modifier = Modifier
                .size(20.dp)
                .rotate(rotation),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f))
    }
}

@Composable
private fun HelpFaqItem(question: String, answer: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(Icons.Rounded.HelpOutline, null,
                tint     = Color(0xFF26A69A),
                modifier = Modifier.size(16.dp))
            Text(
                question,
                style      = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                modifier   = Modifier.weight(1f)
            )
        }
        Text(
            answer,
            style    = MaterialTheme.typography.bodySmall,
            color    = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 24.dp)
        )
    }
}

@Composable
private fun PrivacyPoint(text: String) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(Icons.Rounded.Circle, null,
            tint     = Color(0xFF8D6E63),
            modifier = Modifier
                .padding(top = 5.dp)
                .size(6.dp))
        Text(
            text,
            style    = MaterialTheme.typography.bodySmall,
            color    = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
    }
}

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
            Text(name,
                fontWeight = if (isActive) FontWeight.ExtraBold else FontWeight.SemiBold,
                color = textColor, fontSize = 14.sp)
            Text(range,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        if (isActive) {
            Icon(Icons.Rounded.CheckCircle, null,
                tint     = tierColor,
                modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
private fun ProfileField(
    modifier     : Modifier = Modifier,
    label        : String,
    value        : String,
    onValueChange: (String) -> Unit,
    placeholder  : String      = "",
    enabled      : Boolean     = true,
    keyboardType : KeyboardType = KeyboardType.Text,
    errorText    : String?     = null,
    maxLines     : Int         = 1,
    singleLine   : Boolean     = true
) {
    Column(modifier = modifier) {
        Text(label,
            style      = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color      = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier   = Modifier.padding(bottom = 6.dp))
        OutlinedTextField(
            value          = value,
            onValueChange  = onValueChange,
            placeholder    = {
                Text(placeholder,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f))
            },
            enabled        = enabled,
            isError        = errorText != null,
            supportingText = if (errorText != null) {{
                Text(errorText, color = MaterialTheme.colorScheme.error)
            }} else null,
            modifier       = Modifier.fillMaxWidth(),
            singleLine     = singleLine,
            maxLines       = maxLines,
            shape          = RoundedCornerShape(8.dp),
            colors         = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor   = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f)
            ),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
        )
    }
}

@Composable
private fun PasswordField(
    modifier     : Modifier,
    label        : String,
    placeholder  : String,
    value        : String,
    onValueChange: (String) -> Unit,
    isVisible    : Boolean,
    onToggle     : () -> Unit
) {
    Column(modifier = modifier) {
        Text(label,
            style      = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color      = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier   = Modifier.padding(bottom = 6.dp))
        OutlinedTextField(
            value                = value,
            onValueChange        = onValueChange,
            placeholder          = {
                Text(placeholder,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f))
            },
            visualTransformation = if (isVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            trailingIcon         = if (onToggle != {}) {{
                IconButton(onClick = onToggle) {
                    Icon(
                        if (isVisible) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                        null,
                        modifier = Modifier.size(20.dp),
                        tint     = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }} else null,
            modifier        = Modifier.fillMaxWidth(),
            singleLine      = true,
            shape           = RoundedCornerShape(8.dp),
            colors          = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
    }
}