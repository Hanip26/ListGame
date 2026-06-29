package com.example.listgame.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listgame.viewmodel.AuthEvent
import com.example.listgame.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: (String) -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit
) {
    val state              by viewModel.loginState.collectAsState()
    val keyboardCtrl       = LocalSoftwareKeyboardController.current
    val focusManager       = LocalFocusManager.current
    val snackbarHostState  = remember { SnackbarHostState() }

    // ── Tangkap one-shot event navigasi ──────────────────────────────────────
    LaunchedEffect(Unit) {
        viewModel.authEvent.collect { event ->
            if (event is AuthEvent.LoginSuccess) {
                onLoginSuccess(event.username)
            }
        }
    }

    // ── Snackbar: pesan sukses setelah register ───────────────────────────────
    val regMsg = state.registerSuccessMessage
    LaunchedEffect(regMsg) {
        if (!regMsg.isNullOrBlank()) {
            snackbarHostState.showSnackbar(regMsg)
            viewModel.clearRegisterSuccessMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                // UX PRO: Tambahkan scroll dan imePadding agar tidak tertutup keyboard
                .imePadding()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Spacer top agar konten tetap di tengah meskipun ada scroll
            Spacer(modifier = Modifier.height(48.dp))

            // ── Logo ──────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .size(100.dp) // Sedikit dikecilkan agar proporsional di layar kecil
                    .clip(RoundedCornerShape(28.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primaryContainer,
                                MaterialTheme.colorScheme.secondaryContainer
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector        = Icons.Rounded.VideogameAsset,
                    contentDescription = "Logo Nexus",
                    modifier           = Modifier.size(56.dp),
                    tint               = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text       = "NEXUS",
                style      = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color      = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text      = "Selamat datang kembali! Masuk untuk melanjutkan.",
                style     = MaterialTheme.typography.bodyMedium,
                color     = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(32.dp))

            // ── Error Banner (Tampil jika ada error general) ──────────────
            if (state.errorMessage != null) {
                Surface(
                    color = MaterialTheme.colorScheme.errorContainer,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Rounded.ErrorOutline,
                            contentDescription = "Error",
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = state.errorMessage!!,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            // ── Field Username / Email ────────────────────────────────────
            OutlinedTextField(
                value         = state.usernameOrEmail,
                onValueChange = viewModel::onLoginUsernameChange,
                label         = { Text("Email") },
                leadingIcon   = {
                    Icon(Icons.Rounded.Person, contentDescription = "Email Icon",
                        tint = MaterialTheme.colorScheme.primary)
                },
                enabled         = !state.isLoading, // UX PRO: Kunci input saat loading
                isError         = state.errorMessage != null,
                modifier        = Modifier.fillMaxWidth(),
                singleLine      = true,
                shape           = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction    = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            Spacer(Modifier.height(16.dp))

            // ── Field Password ────────────────────────────────────────────
            OutlinedTextField(
                value         = state.password,
                onValueChange = viewModel::onLoginPasswordChange,
                label         = { Text("Password") },
                leadingIcon   = {
                    Icon(Icons.Rounded.Lock, contentDescription = "Password Icon",
                        tint = MaterialTheme.colorScheme.primary)
                },
                trailingIcon  = {
                    IconButton(
                        onClick = viewModel::onLoginPasswordVisibilityToggle,
                        enabled = !state.isLoading
                    ) {
                        Icon(
                            imageVector = if (state.isPasswordVisible)
                                Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                            contentDescription = if (state.isPasswordVisible) "Sembunyikan password" else "Tampilkan password"
                        )
                    }
                },
                visualTransformation = if (state.isPasswordVisible)
                    VisualTransformation.None else PasswordVisualTransformation(),
                enabled         = !state.isLoading, // UX PRO: Kunci input saat loading
                isError         = state.errorMessage != null,
                modifier        = Modifier.fillMaxWidth(),
                singleLine      = true,
                shape           = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction    = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    keyboardCtrl?.hide()
                    focusManager.clearFocus()
                    viewModel.submitLogin()
                })
            )

            // ── Lupa Password (Didekatkan ke field password) ──────────────
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = onNavigateToForgotPassword,
                    enabled = !state.isLoading,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        "Lupa Password?",
                        fontWeight = FontWeight.SemiBold,
                        color      = MaterialTheme.colorScheme.primary,
                        fontSize   = 13.sp
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // ── Tombol Login ──────────────────────────────────────────────
            Button(
                onClick  = {
                    keyboardCtrl?.hide()
                    focusManager.clearFocus()
                    viewModel.submitLogin()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape   = RoundedCornerShape(16.dp),
                enabled = !state.isLoading && state.usernameOrEmail.isNotBlank() && state.password.isNotBlank()
                // UX PRO: Tombol otomatis disable jika field masih kosong
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.5.dp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) // Warna menyesuaikan disabled state
                    )
                } else {
                    Text("Masuk", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = "Masuk")
                }
            }

            Spacer(Modifier.height(32.dp))

            // ── Link ke Register ──────────────────────────────────────────
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    "Belum punya akun?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                TextButton(
                    onClick = onNavigateToRegister,
                    enabled = !state.isLoading
                ) {
                    Text(
                        "Daftar sekarang",
                        fontWeight = FontWeight.Bold,
                        color      = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Spacer bottom agar layout bisa di scroll sampai habis dengan lega
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}