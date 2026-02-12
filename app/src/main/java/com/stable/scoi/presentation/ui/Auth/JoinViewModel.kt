package com.stable.scoi.presentation.ui.Auth

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.stable.scoi.data.api.auth.ApiKeyInfo
import com.stable.scoi.data.api.auth.SignUpRequest
import com.stable.scoi.data.local.PreferenceManager
import com.stable.scoi.data.util.EncryptionUtil
import com.stable.scoi.domain.repository.auth.AuthRepository
import com.stable.scoi.presentation.base.BaseViewModel
import com.stable.scoi.presentation.ui.bio.BioEvent
import com.stable.scoi.util.SLOG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class JoinViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferenceManager: PreferenceManager // ★ 1. 생성자 매개변수에 private val 추가
) : BaseViewModel<JoinState, JoinEvent>(JoinState()) {
    private var timerJob: Job? = null

    fun onKoreanNameChanged(input: String) = updateState { copy(koreanName = input) }
    fun onEnglishNameChanged(input: String) = updateState { copy(englishName = input) }
    fun updateResidentNumber(input: String) = updateState { copy(residentNumber = input) }
    fun onPhoneAuthNumberChanged(input: String) = updateState { copy(phoneNumber = input) }
    fun onAuthCodeChanged(input: String) = updateState { copy(verificationCode = input) }

    fun onPinChanged(input: String) =
        updateState { copy(simplePassword = input, isButtonEnabled = input.length == 6) }


    fun sendSms() {
        val phone = uiState.value.phoneNumber
        if (phone.isBlank()) {
            emitEvent(JoinEvent.ShowError("휴대폰 번호를 입력해주세요."))
            return
        }

        viewModelScope.launch {
            authRepository.sendSms(phone)
                .onSuccess { response ->
                    val remainingSeconds = calculateRemainingSeconds(response.expiredAt)
                    startTimer(remainingSeconds)

                    updateState { copy(isCodeSendEnabled = true) }
                    emitEvent(JoinEvent.ShowError("인증번호가 발송되었습니다."))
                }
                .onFailure { e ->
                    emitEvent(JoinEvent.ShowError(e.message ?: "전송 실패"))
                }
        }
    }

    // --- SMS 인증 확인 ---
    fun verifySms() {
        val currentState = uiState.value
        val phone = currentState.phoneNumber
        val code = currentState.verificationCode

        if (code.length != 6) {
            emitEvent(JoinEvent.ShowError("인증번호 6자리를 입력해주세요."))
            return
        }
        if (!currentState.isTimerRunning) {
            emitEvent(JoinEvent.ShowError("인증 시간이 만료되었습니다."))
            return
        }

        viewModelScope.launch {
            authRepository.verifySms(phone, code)
                .onSuccess { response ->
                    stopTimer()

                    preferenceManager.saveVerificationSuccess()

                    updateState {
                        copy(
                            isVerified = true,
                            verificationToken = response.verificationToken
                        )
                    }

                    // 2. Fragment로 성공 이벤트 전송 (필요시 토큰을 담아서 전송 가능)
                    emitEvent(JoinEvent.VerifySuccess(response.verificationToken))
                }
                .onFailure {
                    emitEvent(JoinEvent.ShowError("인증번호가 일치하지 않습니다."))
                }
        }
    }

    // 지문 등록 여부
    fun onBiometricSuccess() {
        updateState { copy(isBioRegistered = true) }
        emitEvent(JoinEvent.NavigateToRegDone)
    }

    private fun startTimer(seconds: Long) {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            updateState { copy(isTimerRunning = true, leftTime = seconds) }

            var remaining = seconds
            while (remaining > 0) {
                delay(1000L)
                remaining--
                updateState { copy(leftTime = remaining) }
            }

            updateState { copy(isTimerRunning = false, leftTime = 0) }
            emitEvent(JoinEvent.ShowError("인증 시간이 초과되었습니다."))
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        updateState { copy(isTimerRunning = false) }
    }

    private fun calculateRemainingSeconds(expiredAtString: String): Long {
        return try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val expiredTime = LocalDateTime.parse(expiredAtString, formatter)
            val currentTime = LocalDateTime.now()

            val diff = ChronoUnit.SECONDS.between(currentTime, expiredTime)
            if (diff < 0) 0L else diff
        } catch (e: Exception) {
            180L
        }
    }

    fun submitSignUp(exchange: String, apiKey: String, secretKey: String) {
        val currentState = uiState.value
        updateState { copy(isLoading = true) }

        viewModelScope.launch {
            try {
                // 1. 보안이 필요한 필드들을 AES/CBC/PKCS5로 암호화
                val encryptedSecretKey = EncryptionUtil.encrypt(secretKey)
                val encryptedSimplePassword = EncryptionUtil.encrypt(currentState.simplePassword)

                SLOG.D(encryptedSimplePassword.toString())
                // 2. 개별 API 키 정보 포장
                val newApiKey = ApiKeyInfo(
                    exchangeType = exchange,
                    publicKey = apiKey,              // 퍼블릭 키는 보통 평문 전송
                    secretKey = encryptedSecretKey   // 시크릿 키는 암호화!
                )

                // 3. 전체 회원가입 요청 객체 조립
                val request = SignUpRequest(
                    koreanName = currentState.koreanName,
                    englishName = currentState.englishName,
                    residentNumber = currentState.residentNumber,
                    phoneNumber = currentState.phoneNumber,
                    simplePassword = encryptedSimplePassword,
                    apiKeys = listOf(newApiKey),
                    memberType = "INDIVIDUAL",
                    isBioRegistered = currentState.isBioRegistered,
                    verificationToken = currentState.verificationToken
                )

                // 4. 서버로 전송
                authRepository.signUp(request)
                    .onSuccess {
                        android.util.Log.d("JOIN_DEBUG", "회원가입 성공!")
                        emitEvent(JoinEvent.NavigateToRegDone)
                    }
                    .onFailure { e ->
                        android.util.Log.e("JOIN_DEBUG", "회원가입 실패: ${e.message}")
                        emitEvent(JoinEvent.ShowError(e.message ?: "회원가입 실패"))
                    }
            } catch (e: Exception) {
                android.util.Log.e("JOIN_DEBUG", "암호화 중 오류 발생: ${e.message}")
                emitEvent(JoinEvent.ShowError("보안 처리 중 오류가 발생했습니다."))
            } finally {
                updateState { copy(isLoading = false) }
            }
        }
    }
}
