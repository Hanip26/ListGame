package com.example.listgame.ui.screen

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
    appViewModel: AppViewModel,
    onLogout: () -> Unit
) {
    val backStack    = LocalBackStack.current
    val user         by authViewModel.currentUser.collectAsState()
    val state        by authViewModel.profileState.collectAsState()

    val transactions     by appViewModel.transactions.collectAsState()
    val nexusCoinBalance by appViewModel.nexusCoinBalance.collectAsState()

    val totalRevenue = transactions.filter { it.status == TransactionStatus.SUCCESS }
        .sumOf { it.totalPrice }

    var isSecurityExpanded by remember { mutableStateOf(false) }
    var showLogoutDialog   by remember { mutableStateOf(false) }
    var showTierDialog     by remember { mutableStateOf(false) }

    LaunchedEffect(user) {
        user?.let { authViewModel.initProfileForm(it) }
    }

    val username = user?.username ?: ""
    val nexusCoinsText = "%,d".format(nexusCoinBalance).replace(",", ".")
    val totalPembelianText = "Rp ${"%,d".format(totalRevenue).replace(',', '.').ifBlank { "0" }}"

    // ── LOGIKA PENENTUAN TIER DINAMIS ──
    val userTier = when {
        totalRevenue <= 100000 -> "Member Junior"
        totalRevenue <= 500000 -> "Member Senior"
        totalRevenue <= 1000000 -> "Member Terhormat"
        totalRevenue <= 2000000 -> "Member Juragan"
        else -> "Sultan Tier Member"
    }

    // ── LOGIKA PENENTUAN WARNA TIER BARU YANG SINKRON ──
    val userTierColor = when {
        totalRevenue <= 100000 -> Color(0xFF00E676)    // Hijau Terang
        totalRevenue <= 500000 -> Color(0xFF00B0FF)    // Biru Terang
        totalRevenue <= 1000000 -> Color(0xFFE040FB)   // Ungu kearah pink cerah
        totalRevenue <= 2000000 -> Color(0xFFFF9100)   // Orange cerah
        else -> Color(0xFFFF1744)                      // Merah menyala
    }

    val headerGradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
            MaterialTheme.colorScheme.surface
        )
    )

    // ── POP-UP DIALOG DAFTAR TIER MEMBERSHIP DENGAN WARNA BARU ──
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
                    Icon(Icons.Rounded.MilitaryTech, contentDescription = null, tint = userTierColor, modifier = Modifier.size(28.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Tingkatan Loyalitas Member", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Total Pembelian Kamu Saat Ini:\n$totalPembelianText",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = userTierColor
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                    TierItem(name = "1. Member Junior", range = "Rp 0 - Rp 100.000", isActive = userTier == "Member Junior", tierColor = Color(0xFF00E676))
                    TierItem(name = "2. Member Senior", range = "Rp 100.000 - Rp 500.000", isActive = userTier == "Member Senior", tierColor = Color(0xFF00B0FF))
                    TierItem(name = "3. Member Terhormat", range = "Rp 500.000 - Rp 1.000.000", isActive = userTier == "Member Terhormat", tierColor = Color(0xFFE040FB))
                    TierItem(name = "4. Member Juragan", range = "Rp 1.000.000 - Rp 2.000.000", isActive = userTier == "Member Juragan", tierColor = Color(0xFFFF9100))
                    TierItem(name = "5. Sultan Tier Member", range = "Di atas Rp 2.000.000", isActive = userTier == "Sultan Tier Member", tierColor = Color(0xFFFF1744))
                }
            },
            shape = RoundedCornerShape(24.dp),
            containerColor = MaterialTheme.colorScheme.surface
        )
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text(text = "Konfirmasi Logout") },
            text = { Text(text = "Yakin ingin keluar dari akun ini?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    }
                ) {
                    Text("Keluar", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Batal", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(6.dp)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profil Saya", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = { backStack.removeLastOrNull() }) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(headerGradient)
                    .padding(top = 16.dp, bottom = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text       = (user?.displayName ?: "?").take(1).uppercase(),
                            fontSize   = 38.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color      = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = user?.displayName ?: "-",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "@$username",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(Modifier.height(8.dp))

                    // KOTAKAN CHIP MEMBER SEKARANG MEMILIKI WARNA YANG DINAMIS & CLICKABLE
                    Surface(
                        color = userTierColor.copy(alpha = 0.15f),
                        contentColor = userTierColor,
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { showTierDialog = true }
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Rounded.Verified, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(userTier, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFA000).copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.MonetizationOn,
                                contentDescription = "NEXUS Coins Icon",
                                tint = Color(0xFFFFA000),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text("NEXUS Coins", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(nexusCoinsText, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                    }

                    VerticalDivider(
                        modifier = Modifier
                            .height(32.dp)
                            .padding(horizontal = 8.dp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                    )

                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.ShoppingBag,
                                contentDescription = "Total Pembelian Icon",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text("Total Pembelian", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(totalPembelianText, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            ProfileSectionCard(
                title = "Informasi Akun",
                subtitle = "Kelola detail profil publik dan data kontak kamu."
            ) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
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

                Spacer(Modifier.height(12.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
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

                Spacer(Modifier.height(12.dp))

                ProfileField(
                    modifier      = Modifier.fillMaxWidth(),
                    label         = "Bio Singkat",
                    value         = state.bio,
                    onValueChange = authViewModel::onProfileBioChange,
                    placeholder   = "Ceritakan sedikit tentang dirimu...",
                    maxLines      = 3,
                    singleLine    = false
                )

                Spacer(Modifier.height(16.dp))

                if (state.profileError != null) {
                    Text(state.profileError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    Spacer(Modifier.height(4.dp))
                }
                if (state.profileSuccess != null) {
                    Text(state.profileSuccess!!, color = Color(0xFF4CAF50), style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(4.dp))
                }

                Button(
                    onClick  = { authViewModel.submitProfileUpdate(username) },
                    enabled  = !state.isProfileLoading,
                    shape    = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    if (state.isProfileLoading) {
                        CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp, color = MaterialTheme.colorScheme.onPrimary)
                    } else {
                        Text("Simpan Perubahan", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            ExpandableProfileSectionCard(
                title      = "Keamanan Akun",
                subtitle   = "Perbarui kata sandi secara berkala untuk menjaga keamanan data top-up.",
                isExpanded = isSecurityExpanded,
                onToggle   = { isSecurityExpanded = !isSecurityExpanded }
            ) {
                PasswordField(
                    modifier      = Modifier.fillMaxWidth(),
                    label         = "Kata Sandi Saat Ini",
                    placeholder   = "Masukkan kata sandi lama",
                    value         = state.currentPassword,
                    onValueChange = authViewModel::onCurrentPasswordChange,
                    isVisible     = state.showCurrentPass,
                    onToggle      = authViewModel::onToggleCurrentPassVisibility
                )

                Spacer(Modifier.height(12.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
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

                Spacer(Modifier.height(16.dp))

                if (state.passwordError != null) {
                    Text(state.passwordError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    Spacer(Modifier.height(4.dp))
                }
                if (state.passwordSuccess != null) {
                    Text(state.passwordSuccess!!, color = Color(0xFF4CAF50), style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(4.dp))
                }

                Button(
                    onClick  = { authViewModel.submitChangePassword(username) },
                    enabled  = !state.isPasswordLoading,
                    shape    = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors   = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    if (state.isPasswordLoading) {
                        CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp, color = MaterialTheme.colorScheme.onSecondary)
                    } else {
                        Text("Perbarui Kata Sandi", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape     = RoundedCornerShape(12.dp),
                colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Sesi Aplikasi", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
                        Text("Keluar jika ingin beralih akun.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    OutlinedButton(
                        onClick = { showLogoutDialog = true },
                        shape   = RoundedCornerShape(8.dp),
                        colors  = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                        border  = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.error.copy(alpha = 0.4f))
                    ) {
                        Icon(Icons.AutoMirrored.Rounded.Logout, null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Log Out", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun TierItem(name: String, range: String, isActive: Boolean, tierColor: Color) {
    val backgroundColor = if (isActive) tierColor.copy(alpha = 0.15f) else Color.Transparent
    val textColor = if (isActive) tierColor else MaterialTheme.colorScheme.onSurface

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(name, fontWeight = if (isActive) FontWeight.ExtraBold else FontWeight.SemiBold, color = textColor, fontSize = 14.sp)
            Text(range, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        if (isActive) {
            Icon(
                imageVector = Icons.Rounded.CheckCircle,
                contentDescription = "Tier Aktif",
                tint = tierColor,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun ProfileSectionCard(
    title   : String,
    subtitle: String,
    content : @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier  = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape     = RoundedCornerShape(12.dp),
        colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.5.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(subtitle, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                modifier = Modifier.padding(top = 2.dp, bottom = 12.dp))
            HorizontalDivider(modifier = Modifier.padding(bottom = 14.dp), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))
            content()
        }
    }
}

@Composable
private fun ExpandableProfileSectionCard(
    title     : String,
    subtitle  : String,
    isExpanded: Boolean,
    onToggle  : () -> Unit,
    content   : @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier  = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape     = RoundedCornerShape(12.dp),
        colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.5.dp),
        onClick   = onToggle
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(subtitle, style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                        modifier = Modifier.padding(top = 2.dp))
                }
                IconButton(onClick = onToggle) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                        contentDescription = if (isExpanded) "Tutup" else "Buka",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (isExpanded) {
                HorizontalDivider(modifier = Modifier.padding(vertical = 14.dp), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))
                content()
            }
        }
    }
}

@Composable
private fun ProfileField(
    modifier     : Modifier = Modifier,
    label        : String,
    value        : String,
    onValueChange: (String) -> Unit,
    placeholder  : String  = "",
    enabled      : Boolean = true,
    keyboardType : KeyboardType = KeyboardType.Text,
    errorText    : String? = null,
    maxLines     : Int     = 1,
    singleLine   : Boolean = true
) {
    Column(modifier = modifier) {
        Text(label, style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 6.dp))
        OutlinedTextField(
            value         = value,
            onValueChange = onValueChange,
            placeholder   = { Text(placeholder, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)) },
            enabled       = enabled,
            isError       = errorText != null,
            supportingText = if (errorText != null) {{ Text(errorText, color = MaterialTheme.colorScheme.error) }} else null,
            modifier      = Modifier.fillMaxWidth(),
            singleLine    = singleLine,
            maxLines      = maxLines,
            shape         = RoundedCornerShape(8.dp),
            colors        = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
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
        Text(label, style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 6.dp))
        OutlinedTextField(
            value                = value,
            onValueChange        = onValueChange,
            placeholder          = { Text(placeholder, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)) },
            visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = if (onToggle != {}) {{
                IconButton(onClick = onToggle) {
                    Icon(
                        if (isVisible) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                        null, modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }} else null,
            modifier  = Modifier.fillMaxWidth(),
            singleLine = true,
            shape      = RoundedCornerShape(8.dp),
            colors        = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
    }
}