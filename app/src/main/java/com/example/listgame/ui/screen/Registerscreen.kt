package com.example.listgame.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listgame.viewmodel.AuthEvent
import com.example.listgame.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    val state        by viewModel.registerState.collectAsState()
    val keyboardCtrl = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        viewModel.authEvent.collect { event ->
            if (event is AuthEvent.RegisterSuccess) {
                onRegisterSuccess(event.username)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buat Akun Baru", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = {
                        // BUG FIX: reset form sebelum kembali ke Login
                        // agar saat user buka Register lagi formnya bersih
                        viewModel.resetRegisterForm()
                        onNavigateBack()
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
                .padding(horizontal = 24.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text("Halo, Gamer Baru! 👋",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold)
            Text(
                "Isi data di bawah untuk mulai petualanganmu di NEXUS.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(16.dp))
            SectionLabel("Informasi Akun")

            AuthTextField(
                value         = state.username,
                onValueChange = viewModel::onRegisterUsernameChange,
                label         = "Username",
                placeholder   = "contoh: gamer_keren",
                leadingIcon   = Icons.Rounded.AlternateEmail,
                errorText     = state.usernameError,
                imeAction     = ImeAction.Next,
                onNext        = { focusManager.moveFocus(FocusDirection.Down) }
            )

            AuthTextField(
                value         = state.email,
                onValueChange = viewModel::onRegisterEmailChange,
                label         = "Email",
                placeholder   = "contoh: kamu@email.com",
                leadingIcon   = Icons.Rounded.Email,
                errorText     = state.emailError,
                keyboardType  = KeyboardType.Email,
                imeAction     = ImeAction.Next,
                onNext        = { focusManager.moveFocus(FocusDirection.Down) }
            )

            AuthTextField(
                value         = state.displayName,
                onValueChange = viewModel::onRegisterDisplayNameChange,
                label         = "Nama Tampilan",
                placeholder   = "Nama yang terlihat pemain lain",
                leadingIcon   = Icons.Rounded.Badge,
                errorText     = state.displayNameError,
                imeAction     = ImeAction.Next,
                onNext        = { focusManager.moveFocus(FocusDirection.Down) }
            )

            Spacer(Modifier.height(8.dp))
            SectionLabel("Keamanan")

            OutlinedTextField(
                value         = state.password,
                onValueChange = viewModel::onRegisterPasswordChange,
                label         = { Text("Password") },
                placeholder   = { Text("Minimal 6 karakter") },
                leadingIcon   = { Icon(Icons.Rounded.Lock, null, tint = MaterialTheme.colorScheme.primary) },
                trailingIcon  = {
                    IconButton(onClick = viewModel::onRegisterPasswordVisibilityToggle) {
                        Icon(if (state.isPasswordVisible) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility, null)
                    }
                },
                visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                isError         = state.passwordError != null,
                supportingText  = {
                    if (state.passwordError != null)
                        Text(state.passwordError!!, color = MaterialTheme.colorScheme.error)
                    else
                        Text("Gunakan kombinasi huruf & angka", color = MaterialTheme.colorScheme.onSurfaceVariant)
                },
                modifier        = Modifier.fillMaxWidth(),
                singleLine      = true,
                shape           = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )

            OutlinedTextField(
                value         = state.confirmPassword,
                onValueChange = viewModel::onRegisterConfirmPasswordChange,
                label         = { Text("Konfirmasi Password") },
                placeholder   = { Text("Ulangi password kamu") },
                leadingIcon   = { Icon(Icons.Rounded.LockOpen, null, tint = MaterialTheme.colorScheme.primary) },
                visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                isError         = state.confirmPassError != null,
                supportingText  = {
                    if (state.confirmPassError != null)
                        Text(state.confirmPassError!!, color = MaterialTheme.colorScheme.error)
                },
                modifier        = Modifier.fillMaxWidth(),
                singleLine      = true,
                shape           = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    keyboardCtrl?.hide()
                    viewModel.submitRegister()
                })
            )

            if (state.generalError != null) {
                Spacer(Modifier.height(4.dp))
                Text(state.generalError!!, color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 4.dp))
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick  = { keyboardCtrl?.hide(); viewModel.submitRegister() },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape    = RoundedCornerShape(16.dp),
                enabled  = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(Modifier.size(22.dp), strokeWidth = 2.5.dp,
                        color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Icon(Icons.Rounded.HowToReg, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Buat Akun", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Sudah punya akun?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                TextButton(onClick = {
                    viewModel.resetRegisterForm()
                    onNavigateBack()
                }) {
                    Text("Masuk", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(text, style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(bottom = 4.dp, top = 8.dp))
}

@Composable
private fun AuthTextField(
    value: String, onValueChange: (String) -> Unit,
    label: String, placeholder: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    errorText: String?,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onNext: () -> Unit = {}
) {
    OutlinedTextField(
        value = value, onValueChange = onValueChange,
        label = { Text(label) }, placeholder = { Text(placeholder) },
        leadingIcon = { Icon(leadingIcon, null, tint = MaterialTheme.colorScheme.primary) },
        isError = errorText != null,
        supportingText = { if (errorText != null) Text(errorText, color = MaterialTheme.colorScheme.error) },
        modifier = Modifier.fillMaxWidth(), singleLine = true,
        shape = RoundedCornerShape(16.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = KeyboardActions(onNext = { onNext() })
    )
}