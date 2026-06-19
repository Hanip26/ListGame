package com.example.listgame.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listgame.data.ChangePasswordResult
import com.example.listgame.data.LoginResult
import com.example.listgame.data.RegisterResult
import com.example.listgame.data.UpdateProfileResult
import com.example.listgame.data.UserRepository
import com.example.listgame.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.listgame.data.ResetPasswordResult

data class LoginUiState(
    val usernameOrEmail      : String  = "",
    val password             : String  = "",
    val isPasswordVisible    : Boolean = false,
    val isLoading            : Boolean = false,
    val errorMessage         : String? = null,
    val registerSuccessMessage: String? = null,  // snackbar setelah register
)

data class RegisterUiState(
    val username         : String  = "",
    val email            : String  = "",
    val displayName      : String  = "",
    val password         : String  = "",
    val confirmPassword  : String  = "",
    val isPasswordVisible: Boolean = false,
    val isLoading        : Boolean = false,
    val usernameError    : String? = null,
    val emailError       : String? = null,
    val displayNameError : String? = null,
    val passwordError    : String? = null,
    val confirmPassError : String? = null,
    val generalError     : String? = null,
)

// ── Tambah state baru untuk lupa password ────────────────────────────────────
data class ForgotPasswordUiState(
    val step              : ForgotPasswordStep = ForgotPasswordStep.INPUT_IDENTITY,
    val usernameOrEmail   : String  = "",
    val newPassword       : String  = "",
    val confirmNewPassword: String  = "",
    val isPasswordVisible : Boolean = false,
    val isLoading         : Boolean = false,
    val identityError     : String? = null,
    val newPasswordError  : String? = null,
    val confirmPassError  : String? = null,
    val successMessage    : String? = null,
)

enum class ForgotPasswordStep {
    INPUT_IDENTITY,  // Step 1: masukkan username/email
    INPUT_NEW_PASS,  // Step 2: masukkan password baru
    SUCCESS          // Step 3: berhasil
}

data class ProfileUiState(
    val displayName      : String  = "",
    val email            : String  = "",
    val phone            : String  = "",
    val bio              : String  = "",
    val currentPassword  : String  = "",
    val newPassword      : String  = "",
    val confirmNewPassword: String = "",
    val showCurrentPass  : Boolean = false,
    val showNewPass      : Boolean = false,
    val isProfileLoading : Boolean = false,
    val isPasswordLoading: Boolean = false,
    val profileSuccess   : String? = null,
    val profileError     : String? = null,
    val passwordSuccess  : String? = null,
    val passwordError    : String? = null,
    val emailError       : String? = null,
)

sealed class AuthEvent {
    data class LoginSuccess(val username: String)    : AuthEvent()
    data class RegisterSuccess(val displayName: String) : AuthEvent()
    object ForgotPasswordSuccess                        : AuthEvent()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _loginState    = MutableStateFlow(LoginUiState())
    val loginState: StateFlow<LoginUiState> = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow(RegisterUiState())
    val registerState: StateFlow<RegisterUiState> = _registerState.asStateFlow()

    private val _profileState  = MutableStateFlow(ProfileUiState())
    val profileState: StateFlow<ProfileUiState> = _profileState.asStateFlow()

    private val _authEvent = MutableSharedFlow<AuthEvent>()
    val authEvent: SharedFlow<AuthEvent> = _authEvent.asSharedFlow()

    val isLoggedIn: StateFlow<Boolean> = repository.isLoggedIn
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    val loggedInUsername: StateFlow<String> = repository.loggedInUsername
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "")

    val currentUser: StateFlow<User?> = repository.currentUser
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun onLoginUsernameChange(v: String) =
        _loginState.update { it.copy(usernameOrEmail = v, errorMessage = null) }
    fun onLoginPasswordChange(v: String) =
        _loginState.update { it.copy(password = v, errorMessage = null) }
    fun onLoginPasswordVisibilityToggle() =
        _loginState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }

    /** Dipanggil MainActivity setelah redirect dari Register agar snackbar muncul di LoginScreen. */
    fun setRegisterSuccessMessage(name: String) {
        _loginState.update { it.copy(registerSuccessMessage = "Akun '$name' berhasil dibuat! Silakan masuk.") }
    }

    fun clearRegisterSuccessMessage() {
        _loginState.update { it.copy(registerSuccessMessage = null) }
    }

    fun submitLogin() {
        val s = _loginState.value
        if (s.usernameOrEmail.isBlank() || s.password.isBlank()) {
            _loginState.update { it.copy(errorMessage = "Username/email dan password tidak boleh kosong.") }
            return
        }
        viewModelScope.launch {
            _loginState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val r = repository.loginApi(s.usernameOrEmail, s.password)) {
                is LoginResult.Success -> {
                    _loginState.value = LoginUiState()
                    _authEvent.emit(AuthEvent.LoginSuccess(r.username))
                }
                LoginResult.WrongPassword ->
                    _loginState.update { it.copy(isLoading = false, errorMessage = "Password salah.") }
                LoginResult.UserNotFound ->
                    _loginState.update { it.copy(isLoading = false, errorMessage = "Akun tidak ditemukan. Sudah daftar?") }
            }
        }
    }

    fun onRegisterUsernameChange(v: String)     = _registerState.update { it.copy(username = v, usernameError = null) }
    fun onRegisterEmailChange(v: String)         = _registerState.update { it.copy(email = v, emailError = null) }
    fun onRegisterDisplayNameChange(v: String)   = _registerState.update { it.copy(displayName = v, displayNameError = null) }
    fun onRegisterPasswordChange(v: String)      = _registerState.update { it.copy(password = v, passwordError = null) }
    fun onRegisterConfirmPasswordChange(v: String) = _registerState.update { it.copy(confirmPassword = v, confirmPassError = null) }
    fun onRegisterPasswordVisibilityToggle()     = _registerState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    fun resetRegisterForm()                      { _registerState.value = RegisterUiState() }

    fun submitRegister() {
        val s = _registerState.value; var hasError = false
        if (s.username.isBlank()) { _registerState.update { it.copy(usernameError = "Username tidak boleh kosong.") }; hasError = true }
        else if (s.username.length < 3) { _registerState.update { it.copy(usernameError = "Username minimal 3 karakter.") }; hasError = true }
        else if (!s.username.matches(Regex("^[a-zA-Z0-9_]+\$"))) { _registerState.update { it.copy(usernameError = "Hanya huruf, angka, dan underscore.") }; hasError = true }
        if (s.email.isBlank()) { _registerState.update { it.copy(emailError = "Email tidak boleh kosong.") }; hasError = true }
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(s.email).matches()) { _registerState.update { it.copy(emailError = "Format email tidak valid.") }; hasError = true }
        if (s.displayName.isBlank()) { _registerState.update { it.copy(displayNameError = "Nama tampilan tidak boleh kosong.") }; hasError = true }
        if (s.password.length < 6) { _registerState.update { it.copy(passwordError = "Password minimal 6 karakter.") }; hasError = true }
        if (s.confirmPassword != s.password) { _registerState.update { it.copy(confirmPassError = "Password tidak cocok.") }; hasError = true }
        if (hasError) return

        viewModelScope.launch {
            _registerState.update { it.copy(isLoading = true) }
            when (repository.registerApi(s.username, s.email, s.password, s.displayName)) {
                RegisterResult.Success -> {
                    val name = s.displayName.trim()
                    _registerState.value = RegisterUiState()
                    _authEvent.emit(AuthEvent.RegisterSuccess(name))
                }
                RegisterResult.UsernameTaken ->
                    _registerState.update { it.copy(isLoading = false, usernameError = "Username sudah dipakai.") }
                RegisterResult.EmailTaken ->
                    _registerState.update { it.copy(isLoading = false, emailError = "Email sudah terdaftar.") }
            }
        }
    }


    fun initProfileForm(user: User) {
        _profileState.update {
            it.copy(
                displayName = user.displayName,
                email       = user.email,
                phone       = user.phone,
                bio         = user.bio,
                profileSuccess = null,
                profileError   = null,
                passwordSuccess = null,
                passwordError   = null,
            )
        }
    }

    fun onProfileDisplayNameChange(v: String) =
        _profileState.update { it.copy(displayName = v, profileError = null, profileSuccess = null) }
    fun onProfileEmailChange(v: String) =
        _profileState.update { it.copy(email = v, emailError = null, profileError = null, profileSuccess = null) }
    fun onProfilePhoneChange(v: String) =
        _profileState.update { it.copy(phone = v) }
    fun onProfileBioChange(v: String) =
        _profileState.update { it.copy(bio = v) }

    fun submitProfileUpdate(username: String) {
        val s = _profileState.value
        if (s.displayName.isBlank()) {
            _profileState.update { it.copy(profileError = "Nama tidak boleh kosong.") }; return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(s.email).matches()) {
            _profileState.update { it.copy(emailError = "Format email tidak valid.") }; return
        }
        viewModelScope.launch {
            _profileState.update { it.copy(isProfileLoading = true, profileError = null, profileSuccess = null) }
            when (repository.updateProfile(username, s.displayName, s.email, s.phone, s.bio)) {
                UpdateProfileResult.Success ->
                    _profileState.update { it.copy(isProfileLoading = false, profileSuccess = "Profil berhasil diperbarui!") }
                UpdateProfileResult.EmailTaken ->
                    _profileState.update { it.copy(isProfileLoading = false, emailError = "Email sudah dipakai akun lain.") }
                UpdateProfileResult.UserNotFound ->
                    _profileState.update { it.copy(isProfileLoading = false, profileError = "User tidak ditemukan.") }
            }
        }
    }

<<<<<<< HEAD
    // ── Forgot Password state ─────────────────────────────────────────────────────
    private val _forgotState = MutableStateFlow(ForgotPasswordUiState())
    val forgotState: StateFlow<ForgotPasswordUiState> = _forgotState.asStateFlow()

    fun resetForgotForm() { _forgotState.value = ForgotPasswordUiState() }

    fun onForgotIdentityChange(v: String) =
        _forgotState.update { it.copy(usernameOrEmail = v, identityError = null) }

    fun onForgotNewPasswordChange(v: String) =
        _forgotState.update { it.copy(newPassword = v, newPasswordError = null) }

    fun onForgotConfirmPasswordChange(v: String) =
        _forgotState.update { it.copy(confirmNewPassword = v, confirmPassError = null) }

    fun onForgotPasswordVisibilityToggle() =
        _forgotState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }

    // Step 1 — Verifikasi username/email
    fun submitForgotIdentity() {
        val s = _forgotState.value
        if (s.usernameOrEmail.isBlank()) {
            _forgotState.update {
                it.copy(identityError = "Username atau email tidak boleh kosong.")
            }
            return
        }
        viewModelScope.launch {
            _forgotState.update { it.copy(isLoading = true, identityError = null) }
            // Cek apakah username/email terdaftar
            val exists = repository.checkUserExists(s.usernameOrEmail)
            if (exists) {
                _forgotState.update {
                    it.copy(
                        isLoading = false,
                        step      = ForgotPasswordStep.INPUT_NEW_PASS
                    )
                }
            } else {
                _forgotState.update {
                    it.copy(
                        isLoading    = false,
                        identityError = "Akun dengan username/email tersebut tidak ditemukan."
                    )
                }
            }
        }
    }

    // Step 2 — Reset password
    fun submitNewPassword() {
        val s = _forgotState.value
        var hasError = false

        if (s.newPassword.length < 6) {
            _forgotState.update {
                it.copy(newPasswordError = "Password minimal 6 karakter.")
            }
            hasError = true
        }
        if (s.confirmNewPassword != s.newPassword) {
            _forgotState.update {
                it.copy(confirmPassError = "Konfirmasi password tidak cocok.")
            }
            hasError = true
        }
        if (hasError) return

        viewModelScope.launch {
            _forgotState.update { it.copy(isLoading = true) }
            when (repository.resetPassword(s.usernameOrEmail, s.newPassword)) {
                ResetPasswordResult.Success -> {
                    _forgotState.update {
                        it.copy(
                            isLoading      = false,
                            step           = ForgotPasswordStep.SUCCESS,
                            successMessage = "Password berhasil direset! Silakan masuk."
                        )
                    }
                    _authEvent.emit(AuthEvent.ForgotPasswordSuccess)
                }
                ResetPasswordResult.UserNotFound -> {
                    _forgotState.update {
                        it.copy(
                            isLoading     = false,
                            identityError = "Akun tidak ditemukan.",
                            step          = ForgotPasswordStep.INPUT_IDENTITY
                        )
                    }
                }
            }
        }
    }

    // ── Ubah password ─────────────────────────────────────────────────────────
=======
>>>>>>> origin/main
    fun onCurrentPasswordChange(v: String) =
        _profileState.update { it.copy(currentPassword = v, passwordError = null, passwordSuccess = null) }
    fun onNewPasswordChange(v: String) =
        _profileState.update { it.copy(newPassword = v, passwordError = null) }
    fun onConfirmNewPasswordChange(v: String) =
        _profileState.update { it.copy(confirmNewPassword = v, passwordError = null) }
    fun onToggleCurrentPassVisibility() =
        _profileState.update { it.copy(showCurrentPass = !it.showCurrentPass) }
    fun onToggleNewPassVisibility() =
        _profileState.update { it.copy(showNewPass = !it.showNewPass) }

    fun submitChangePassword(username: String) {
        val s = _profileState.value
        if (s.currentPassword.isBlank() || s.newPassword.isBlank()) {
            _profileState.update { it.copy(passwordError = "Semua field password harus diisi.") }; return
        }
        if (s.newPassword.length < 6) {
            _profileState.update { it.copy(passwordError = "Password baru minimal 6 karakter.") }; return
        }
        if (s.newPassword != s.confirmNewPassword) {
            _profileState.update { it.copy(passwordError = "Konfirmasi password tidak cocok.") }; return
        }
        viewModelScope.launch {
            _profileState.update { it.copy(isPasswordLoading = true, passwordError = null, passwordSuccess = null) }
            when (repository.changePassword(username, s.currentPassword, s.newPassword)) {
                ChangePasswordResult.Success -> {
                    _profileState.update {
                        it.copy(
                            isPasswordLoading  = false,
                            passwordSuccess    = "Password berhasil diubah!",
                            currentPassword    = "",
                            newPassword        = "",
                            confirmNewPassword = ""
                        )
                    }
                }
                ChangePasswordResult.WrongCurrentPassword ->
                    _profileState.update { it.copy(isPasswordLoading = false, passwordError = "Password saat ini salah.") }
                ChangePasswordResult.UserNotFound ->
                    _profileState.update { it.copy(isPasswordLoading = false, passwordError = "User tidak ditemukan.") }
            }
        }
    }

    fun logout() { viewModelScope.launch { repository.logout() } }
}
