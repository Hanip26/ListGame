package com.example.listgame.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listgame.navigation.LocalBackStack
import com.example.listgame.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: AuthViewModel,
    onLogout: () -> Unit
) {
    val backStack    = LocalBackStack.current
    val user         by viewModel.currentUser.collectAsState()
    val state        by viewModel.profileState.collectAsState()

    // Inisialisasi form dari data user hanya sekali (atau saat user berubah)
    LaunchedEffect(user) {
        user?.let { viewModel.initProfileForm(it) }
    }

    val username = user?.username ?: ""

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profil Saya", fontWeight = FontWeight.Bold) },
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
            // ── Avatar header ─────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(vertical = 28.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Avatar inisial
                    Box(
                        modifier = Modifier
                            .size(84.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text       = (user?.displayName ?: "?").take(1).uppercase(),
                            fontSize   = 36.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color      = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                    Text(user?.displayName ?: "-",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold)
                    Text("@${user?.username ?: "-"}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Spacer(Modifier.height(16.dp))

            // ================================================================
            // SECTION 1 — Edit Profil
            // ================================================================
            ProfileSectionCard(title = "Profil", subtitle = "Informasi ini bersifat rahasia, jadi berhati-hatilah dengan apa yang kamu bagikan.") {

                // Nama & Username (2 kolom)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ProfileField(
                        modifier      = Modifier.weight(1f),
                        label         = "Nama kamu",
                        value         = state.displayName,
                        onValueChange = viewModel::onProfileDisplayNameChange,
                        placeholder   = "Nama tampilan"
                    )
                    ProfileField(
                        modifier      = Modifier.weight(1f),
                        label         = "Username",
                        value         = username,
                        onValueChange = {},            // username tidak bisa diubah
                        placeholder   = "-",
                        enabled       = false
                    )
                }

                Spacer(Modifier.height(4.dp))

                // Email & Telepon (2 kolom)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ProfileField(
                        modifier      = Modifier.weight(1f),
                        label         = "Alamat Email",
                        value         = state.email,
                        onValueChange = viewModel::onProfileEmailChange,
                        placeholder   = "email@contoh.com",
                        keyboardType  = KeyboardType.Email,
                        errorText     = state.emailError
                    )
                    ProfileField(
                        modifier      = Modifier.weight(1f),
                        label         = "No. Handphone",
                        value         = state.phone,
                        onValueChange = viewModel::onProfilePhoneChange,
                        placeholder   = "08xx-xxxx-xxxx",
                        keyboardType  = KeyboardType.Phone
                    )
                }

                Spacer(Modifier.height(4.dp))

                // Bio (full width)
                ProfileField(
                    modifier      = Modifier.fillMaxWidth(),
                    label         = "Bio",
                    value         = state.bio,
                    onValueChange = viewModel::onProfileBioChange,
                    placeholder   = "Ceritakan sedikit tentang dirimu...",
                    maxLines      = 3,
                    singleLine    = false
                )

                Spacer(Modifier.height(8.dp))

                // Feedback
                if (state.profileError != null) {
                    Text(state.profileError!!, color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall)
                    Spacer(Modifier.height(4.dp))
                }
                if (state.profileSuccess != null) {
                    Text(state.profileSuccess!!, color = Color(0xFF4CAF50),
                        style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(4.dp))
                }

                Button(
                    onClick  = { viewModel.submitProfileUpdate(username) },
                    enabled  = !state.isProfileLoading,
                    shape    = RoundedCornerShape(10.dp),
                    modifier = Modifier.height(44.dp)
                ) {
                    if (state.isProfileLoading) {
                        CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary)
                    } else {
                        Text("Ubah Profil", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // ================================================================
            // SECTION 2 — Ubah Kata Sandi
            // ================================================================
            ProfileSectionCard(
                title    = "Ubah Kata Sandi",
                subtitle = "Pastikan kamu mengingat kata sandi baru kamu sebelum mengubahnya."
            ) {
                // Password saat ini
                PasswordField(
                    modifier      = Modifier.fillMaxWidth(),
                    label         = "Kata Sandi Saat Ini",
                    placeholder   = "Kata Sandi Saat Ini",
                    value         = state.currentPassword,
                    onValueChange = viewModel::onCurrentPasswordChange,
                    isVisible     = state.showCurrentPass,
                    onToggle      = viewModel::onToggleCurrentPassVisibility
                )

                Spacer(Modifier.height(4.dp))

                // Password baru & konfirmasi (2 kolom)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    PasswordField(
                        modifier      = Modifier.weight(1f),
                        label         = "Kata Sandi Baru",
                        placeholder   = "Kata Sandi Baru",
                        value         = state.newPassword,
                        onValueChange = viewModel::onNewPasswordChange,
                        isVisible     = state.showNewPass,
                        onToggle      = viewModel::onToggleNewPassVisibility
                    )
                    PasswordField(
                        modifier      = Modifier.weight(1f),
                        label         = "Konfirmasi Kata Sandi Baru",
                        placeholder   = "Konfirmasi Kata Sandi Baru",
                        value         = state.confirmNewPassword,
                        onValueChange = viewModel::onConfirmNewPasswordChange,
                        isVisible     = state.showNewPass,
                        onToggle      = {}
                    )
                }

                Spacer(Modifier.height(8.dp))

                if (state.passwordError != null) {
                    Text(state.passwordError!!, color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall)
                    Spacer(Modifier.height(4.dp))
                }
                if (state.passwordSuccess != null) {
                    Text(state.passwordSuccess!!, color = Color(0xFF4CAF50),
                        style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(4.dp))
                }

                Button(
                    onClick  = { viewModel.submitChangePassword(username) },
                    enabled  = !state.isPasswordLoading,
                    shape    = RoundedCornerShape(10.dp),
                    modifier = Modifier.height(44.dp),
                    colors   = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    if (state.isPasswordLoading) {
                        CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onSecondary)
                    } else {
                        Text("Ubah Kata Sandi", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── Tombol Logout ─────────────────────────────────────────────
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape     = RoundedCornerShape(16.dp),
                colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Keluar dari Akun",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onErrorContainer)
                        Text("Sesi kamu akan diakhiri.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.7f))
                    }
                    OutlinedButton(
                        onClick = onLogout,
                        shape   = RoundedCornerShape(10.dp),
                        colors  = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(Icons.Rounded.Logout, null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Keluar", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

// ── Reusable Composables ──────────────────────────────────────────────────────

@Composable
private fun ProfileSectionCard(
    title   : String,
    subtitle: String,
    content : @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier  = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
            Text(subtitle, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp, bottom = 16.dp))
            HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp))
            content()
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
            modifier = Modifier.padding(bottom = 4.dp))
        OutlinedTextField(
            value         = value,
            onValueChange = onValueChange,
            placeholder   = { Text(placeholder, style = MaterialTheme.typography.bodyMedium) },
            enabled       = enabled,
            isError       = errorText != null,
            supportingText = if (errorText != null) {{ Text(errorText, color = MaterialTheme.colorScheme.error) }} else null,
            modifier      = Modifier.fillMaxWidth(),
            singleLine    = singleLine,
            maxLines      = maxLines,
            shape         = RoundedCornerShape(10.dp),
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
            modifier = Modifier.padding(bottom = 4.dp))
        OutlinedTextField(
            value                = value,
            onValueChange        = onValueChange,
            placeholder          = { Text(placeholder, style = MaterialTheme.typography.bodyMedium) },
            visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = if (onToggle != {}) {{
                IconButton(onClick = onToggle) {
                    Icon(
                        if (isVisible) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                        null, modifier = Modifier.size(18.dp)
                    )
                }
            }} else null,
            modifier  = Modifier.fillMaxWidth(),
            singleLine = true,
            shape      = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
    }
}