package com.example.listgame.ui.screen

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listgame.navigation.LocalBackStack
import com.example.listgame.viewmodel.AuthEvent
import com.example.listgame.viewmodel.AuthViewModel
import com.example.listgame.viewmodel.ForgotPasswordStep

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    viewModel     : AuthViewModel,
    onResetSuccess: () -> Unit   // callback ke LoginScreen setelah berhasil
) {
    val backStack    = LocalBackStack.current
    val state        by viewModel.forgotState.collectAsState()
    val keyboardCtrl = LocalSoftwareKeyboardController.current

    // Tangkap event sukses → navigasi balik ke Login
    LaunchedEffect(Unit) {
        viewModel.authEvent.collect { event ->
            if (event is AuthEvent.ForgotPasswordSuccess) {
                onResetSuccess()
            }
        }
    }

    // Reset form saat screen pertama kali tampil
    LaunchedEffect(Unit) {
        viewModel.resetForgotForm()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lupa Password", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = {
                        if (state.step == ForgotPasswordStep.INPUT_NEW_PASS) {
                            // Kembali ke step 1
                            viewModel.resetForgotForm()
                        } else {
                            viewModel.resetForgotForm()
                            backStack.removeLastOrNull()
                        }
                    }) {
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
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(32.dp))

            // ── Indikator Step ────────────────────────────────────────────
            StepIndicator(currentStep = state.step)

            Spacer(Modifier.height(32.dp))

            // ── Konten animasi per step ───────────────────────────────────
            AnimatedContent(
                targetState = state.step,
                transitionSpec = {
                    slideInHorizontally { it } + fadeIn() togetherWith
                            slideOutHorizontally { -it } + fadeOut()
                },
                label = "ForgotStep"
            ) { step ->
                when (step) {

                    // ─── Step 1: Input username / email ───────────────────
                    ForgotPasswordStep.INPUT_IDENTITY -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Ikon
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .background(
                                        MaterialTheme.colorScheme.primaryContainer
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Rounded.ManageAccounts, null,
                                    tint     = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(44.dp)
                                )
                            }

                            // Judul
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    "Verifikasi Identitas",
                                    fontWeight = FontWeight.ExtraBold,
                                    style      = MaterialTheme.typography.headlineSmall
                                )
                                Text(
                                    "Masukkan username atau email yang terdaftar pada akunmu.",
                                    style     = MaterialTheme.typography.bodyMedium,
                                    color     = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center
                                )
                            }

                            Spacer(Modifier.height(8.dp))

                            // Input
                            OutlinedTextField(
                                value         = state.usernameOrEmail,
                                onValueChange = viewModel::onForgotIdentityChange,
                                label         = { Text("Username atau Email") },
                                placeholder   = { Text("contoh: gamer123 atau user@email.com") },
                                leadingIcon   = {
                                    Icon(
                                        Icons.Rounded.PersonSearch, null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                },
                                isError       = state.identityError != null,
                                supportingText = {
                                    if (state.identityError != null)
                                        Text(
                                            state.identityError!!,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                },
                                modifier        = Modifier.fillMaxWidth(),
                                singleLine      = true,
                                shape           = RoundedCornerShape(16.dp),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Email,
                                    imeAction    = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(onDone = {
                                    keyboardCtrl?.hide()
                                    viewModel.submitForgotIdentity()
                                })
                            )

                            // Tombol lanjut
                            Button(
                                onClick = {
                                    keyboardCtrl?.hide()
                                    viewModel.submitForgotIdentity()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                shape   = RoundedCornerShape(16.dp),
                                enabled = !state.isLoading &&
                                        state.usernameOrEmail.isNotBlank()
                            ) {
                                if (state.isLoading) {
                                    CircularProgressIndicator(
                                        modifier    = Modifier.size(22.dp),
                                        strokeWidth = 2.5.dp,
                                        color       = MaterialTheme.colorScheme.onPrimary
                                    )
                                } else {
                                    Text(
                                        "Lanjutkan",
                                        fontSize   = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Icon(Icons.AutoMirrored.Rounded.ArrowForward, null)
                                }
                            }

                            // Info box
                            Card(
                                shape  = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                                )
                            ) {
                                Row(
                                    modifier  = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Icon(
                                        Icons.Rounded.Info, null,
                                        tint     = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        "Pastikan kamu memasukkan username atau email yang " +
                                                "sama persis dengan yang digunakan saat mendaftar.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }
                            }
                        }
                    }

                    // ─── Step 2: Input password baru ──────────────────────
                    ForgotPasswordStep.INPUT_NEW_PASS -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Ikon
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .background(
                                        MaterialTheme.colorScheme.primaryContainer
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Rounded.LockReset, null,
                                    tint     = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(44.dp)
                                )
                            }

                            // Judul
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    "Buat Password Baru",
                                    fontWeight = FontWeight.ExtraBold,
                                    style      = MaterialTheme.typography.headlineSmall
                                )
                                // Tampilkan akun yang direset
                                Surface(
                                    shape  = RoundedCornerShape(8.dp),
                                    color  = MaterialTheme.colorScheme.primaryContainer
                                ) {
                                    Text(
                                        "Akun: ${state.usernameOrEmail}",
                                        modifier   = Modifier.padding(
                                            horizontal = 14.dp, vertical = 6.dp
                                        ),
                                        fontWeight = FontWeight.SemiBold,
                                        color      = MaterialTheme.colorScheme.primary,
                                        fontSize   = 13.sp
                                    )
                                }
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    "Buat password baru yang kuat dan mudah diingat.",
                                    style     = MaterialTheme.typography.bodyMedium,
                                    color     = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center
                                )
                            }

                            Spacer(Modifier.height(4.dp))

                            // Input password baru
                            OutlinedTextField(
                                value         = state.newPassword,
                                onValueChange = viewModel::onForgotNewPasswordChange,
                                label         = { Text("Password Baru") },
                                placeholder   = { Text("Minimal 6 karakter") },
                                leadingIcon   = {
                                    Icon(
                                        Icons.Rounded.Lock, null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                },
                                trailingIcon  = {
                                    IconButton(
                                        onClick = viewModel::onForgotPasswordVisibilityToggle
                                    ) {
                                        Icon(
                                            imageVector = if (state.isPasswordVisible)
                                                Icons.Rounded.VisibilityOff
                                            else Icons.Rounded.Visibility,
                                            contentDescription = "Toggle password"
                                        )
                                    }
                                },
                                visualTransformation = if (state.isPasswordVisible)
                                    VisualTransformation.None
                                else PasswordVisualTransformation(),
                                isError        = state.newPasswordError != null,
                                supportingText = {
                                    if (state.newPasswordError != null)
                                        Text(
                                            state.newPasswordError!!,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    else
                                        Text(
                                            "Gunakan kombinasi huruf & angka",
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                },
                                modifier        = Modifier.fillMaxWidth(),
                                singleLine      = true,
                                shape           = RoundedCornerShape(16.dp),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Password,
                                    imeAction    = ImeAction.Next
                                )
                            )

                            // Input konfirmasi password
                            OutlinedTextField(
                                value         = state.confirmNewPassword,
                                onValueChange = viewModel::onForgotConfirmPasswordChange,
                                label         = { Text("Konfirmasi Password Baru") },
                                placeholder   = { Text("Ulangi password baru") },
                                leadingIcon   = {
                                    Icon(
                                        Icons.Rounded.LockOpen, null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                },
                                visualTransformation = if (state.isPasswordVisible)
                                    VisualTransformation.None
                                else PasswordVisualTransformation(),
                                isError        = state.confirmPassError != null,
                                supportingText = {
                                    if (state.confirmPassError != null)
                                        Text(
                                            state.confirmPassError!!,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                },
                                modifier        = Modifier.fillMaxWidth(),
                                singleLine      = true,
                                shape           = RoundedCornerShape(16.dp),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Password,
                                    imeAction    = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(onDone = {
                                    keyboardCtrl?.hide()
                                    viewModel.submitNewPassword()
                                })
                            )

                            // Indikator kekuatan password
                            if (state.newPassword.isNotEmpty()) {
                                PasswordStrengthIndicator(password = state.newPassword)
                            }

                            // Tombol reset
                            Button(
                                onClick = {
                                    keyboardCtrl?.hide()
                                    viewModel.submitNewPassword()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                shape   = RoundedCornerShape(16.dp),
                                enabled = !state.isLoading &&
                                        state.newPassword.isNotBlank() &&
                                        state.confirmNewPassword.isNotBlank()
                            ) {
                                if (state.isLoading) {
                                    CircularProgressIndicator(
                                        modifier    = Modifier.size(22.dp),
                                        strokeWidth = 2.5.dp,
                                        color       = MaterialTheme.colorScheme.onPrimary
                                    )
                                } else {
                                    Icon(
                                        Icons.Rounded.LockReset, null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        "Reset Password",
                                        fontSize   = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    // ─── Step 3: Berhasil ─────────────────────────────────
                    ForgotPasswordStep.SUCCESS -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Ikon sukses
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF4CAF50)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Rounded.CheckCircle, null,
                                    tint     = Color.White,
                                    modifier = Modifier.size(60.dp)
                                )
                            }

                            Text(
                                "Password Berhasil Direset!",
                                fontWeight = FontWeight.ExtraBold,
                                style      = MaterialTheme.typography.headlineSmall,
                                textAlign  = TextAlign.Center
                            )
                            Text(
                                "Password akunmu sudah berhasil diperbarui. " +
                                        "Silakan masuk dengan password baru.",
                                style     = MaterialTheme.typography.bodyMedium,
                                color     = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )

                            Spacer(Modifier.height(8.dp))

                            // Tombol kembali ke login
                            Button(
                                onClick  = {
                                    viewModel.resetForgotForm()
                                    backStack.removeLastOrNull()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                shape  = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF4CAF50)
                                )
                            ) {
                                Icon(Icons.Rounded.Login, null,
                                    modifier = Modifier.size(20.dp))
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    "Masuk Sekarang",
                                    fontSize   = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

// ── Komponen: Indikator Step ──────────────────────────────────────────────────

@Composable
private fun StepIndicator(currentStep: ForgotPasswordStep) {
    val steps = listOf(
        "Verifikasi" to ForgotPasswordStep.INPUT_IDENTITY,
        "Password Baru" to ForgotPasswordStep.INPUT_NEW_PASS,
        "Selesai" to ForgotPasswordStep.SUCCESS
    )
    val currentIdx = steps.indexOfFirst { it.second == currentStep }

    Row(
        modifier              = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        steps.forEachIndexed { index, (label, _) ->
            val isDone   = index < currentIdx
            val isActive = index == currentIdx

            // Lingkaran step
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isDone   -> Color(0xFF4CAF50)
                                isActive -> MaterialTheme.colorScheme.primary
                                else     -> MaterialTheme.colorScheme.outlineVariant
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isDone) {
                        Icon(
                            Icons.Rounded.Check, null,
                            tint     = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    } else {
                        Text(
                            "${index + 1}",
                            color      = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize   = 14.sp
                        )
                    }
                }
                Text(
                    label,
                    fontSize   = 10.sp,
                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                    color      = when {
                        isDone   -> Color(0xFF4CAF50)
                        isActive -> MaterialTheme.colorScheme.primary
                        else     -> MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }

            // Garis penghubung
            if (index < steps.lastIndex) {
                Box(
                    modifier = Modifier
                        .width(48.dp)
                        .height(2.dp)
                        .padding(bottom = 20.dp)
                        .background(
                            if (isDone) Color(0xFF4CAF50)
                            else MaterialTheme.colorScheme.outlineVariant
                        )
                )
            }
        }
    }
}

// ── Komponen: Indikator Kekuatan Password ────────────────────────────────────

@Composable
private fun PasswordStrengthIndicator(password: String) {
    val strength = when {
        password.length < 6                          -> 0 // Terlalu pendek
        password.length < 8                          -> 1 // Lemah
        password.any { it.isDigit() } &&
                password.any { it.isLetter() }               -> 3 // Kuat
        else                                         -> 2 // Sedang
    }

    val (label, color, fraction) = when (strength) {
        0    -> Triple("Terlalu pendek", MaterialTheme.colorScheme.error,         0.15f)
        1    -> Triple("Lemah",          Color(0xFFFF9800),                        0.35f)
        2    -> Triple("Sedang",         Color(0xFFFFC107),                        0.65f)
        else -> Triple("Kuat",           Color(0xFF4CAF50),                        1.0f)
    }

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Kekuatan password",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                label,
                style      = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color      = color
            )
        }
        LinearProgressIndicator(
            progress         = { fraction },
            modifier         = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color            = color,
            trackColor       = MaterialTheme.colorScheme.outlineVariant
        )
    }
}