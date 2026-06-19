package com.example.listgame.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
    // Dibaca dari state (bukan event) agar tidak hilang saat recompose
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
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ── Logo ──────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(32.dp))
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
                    contentDescription = "Logo",
                    modifier           = Modifier.size(72.dp),
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
            Spacer(Modifier.height(4.dp))
            Text(
                text      = "Masuk ke akunmu",
                style     = MaterialTheme.typography.bodyMedium,
                color     = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(36.dp))

            // ── Field Username / Email ────────────────────────────────────
            OutlinedTextField(
                value         = state.usernameOrEmail,
                onValueChange = viewModel::onLoginUsernameChange,
                label         = { Text("Email") },
                leadingIcon   = {
                    Icon(Icons.Rounded.Person, null,
                        tint = MaterialTheme.colorScheme.primary)
                },
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

            Spacer(Modifier.height(12.dp))

            // ── Field Password ────────────────────────────────────────────
            OutlinedTextField(
                value         = state.password,
                onValueChange = viewModel::onLoginPasswordChange,
                label         = { Text("Password") },
                leadingIcon   = {
                    Icon(Icons.Rounded.Lock, null,
                        tint = MaterialTheme.colorScheme.primary)
                },
                trailingIcon  = {
                    IconButton(onClick = viewModel::onLoginPasswordVisibilityToggle) {
                        Icon(
                            imageVector = if (state.isPasswordVisible)
                                Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                            contentDescription = "Toggle password"
                        )
                    }
                },
                visualTransformation = if (state.isPasswordVisible)
                    VisualTransformation.None else PasswordVisualTransformation(),
                isError         = state.errorMessage != null,
                supportingText  = {
                    if (state.errorMessage != null) {
                        Text(
                            state.errorMessage!!,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
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
                    viewModel.submitLogin()
                })
            )

            Spacer(Modifier.height(28.dp))

            // ── Lupa Password — letakkan setelah OutlinedTextField password ──────────────
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onNavigateToForgotPassword) {
                    Text(
                        "Lupa Password?",
                        fontWeight = FontWeight.SemiBold,
                        color      = MaterialTheme.colorScheme.primary,
                        fontSize   = 13.sp
                    )
                }
            }

// Spacer yang sudah ada tetap
            Spacer(Modifier.height(12.dp))

            // ── Tombol Login ──────────────────────────────────────────────
            Button(
                onClick  = {
                    keyboardCtrl?.hide()
                    viewModel.submitLogin()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape   = RoundedCornerShape(16.dp),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        strokeWidth = 2.5.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Masuk", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Rounded.ArrowForward, null)
                }
            }

            Spacer(Modifier.height(20.dp))

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
                TextButton(onClick = onNavigateToRegister) {
                    Text(
                        "Daftar sekarang",
                        fontWeight = FontWeight.Bold,
                        color      = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}