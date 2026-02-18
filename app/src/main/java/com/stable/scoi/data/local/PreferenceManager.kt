package com.stable.scoi.data.local

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton // ★ 싱글톤 추가

@Singleton // ★ 앱 전체에서 하나의 인스턴스만 공유하도록 설정
class PreferenceManager @Inject constructor( // ★ 클래스명 대문자로 변경
    @ApplicationContext context: Context
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("scoi_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_ACCESS_TOKEN = "ACCESS_TOKEN"
        private const val KEY_REFRESH_TOKEN = "REFRESH_TOKEN"
        private const val KEY_SMS_EXPIRED_AT = "SMS_EXPIRED_AT"
        private const val KEY_VERIFICATION_TOKEN = "VERIFICATION_TOKEN"
        private const val KEY_PHONE_NUMBER = "PHONE_NUMBER"
        private const val KEY_VERIFY_EXPIRE_TIME = "KEY_VERIFY_EXPIRE_TIME"
    }

    // --- Access Token ---
    fun saveAccessToken(token: String) {
        prefs.edit().putString(KEY_ACCESS_TOKEN, token).apply()
    }
    fun getAccessToken(): String = prefs.getString(KEY_ACCESS_TOKEN, "") ?: ""

    // --- Refresh Token ---
    fun saveRefreshToken(token: String) {
        prefs.edit().putString(KEY_REFRESH_TOKEN, token).apply()
    }
    fun getRefreshToken(): String = prefs.getString(KEY_REFRESH_TOKEN, "") ?: ""

    // --- Verification Token (로그인/가입용) ---
    fun saveVerificationToken(token: String) {
        prefs.edit().putString(KEY_VERIFICATION_TOKEN, token).apply()
    }
    // ViewModel에서 쓰기 편하게 리턴 타입을 String으로 통일했습니다.
    fun getVerificationToken(): String = prefs.getString(KEY_VERIFICATION_TOKEN, "") ?: ""

    // --- Phone Number (로그인 시 필수!) ---
    // ★ 누락되었던 저장 함수를 추가했습니다.
    fun savePhoneNumber(number: String) {
        prefs.edit().putString(KEY_PHONE_NUMBER, number).apply()
    }
    fun getPhoneNumber(): String = prefs.getString(KEY_PHONE_NUMBER, "") ?: ""

    // --- SMS Expired ---
    fun saveSmsExpiredAt(expiredAt: String) {
        prefs.edit().putString(KEY_SMS_EXPIRED_AT, expiredAt).apply()
    }
    fun getSmsExpiredAt(): String = prefs.getString(KEY_SMS_EXPIRED_AT, "") ?: ""

    fun saveVerificationSuccess() {
        val currentTime = System.currentTimeMillis()
        val tenMinutesInMillis = 10 * 60 * 1000
        val expireTime = currentTime + tenMinutesInMillis
        prefs.edit().putLong(KEY_VERIFY_EXPIRE_TIME, expireTime).apply()
    }

    fun isVerificationValid(): Boolean {
        val expireTime = prefs.getLong(KEY_VERIFY_EXPIRE_TIME, 0)
        val currentTime = System.currentTimeMillis()

        if (expireTime == 0L) return false
        return currentTime < expireTime
    }

    // 데이터 초기화
    fun clear() {
        prefs.edit().clear().apply()
    }
}