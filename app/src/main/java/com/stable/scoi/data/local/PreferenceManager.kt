package com.stable.scoi.data.local

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject // jakarta -> javax (Hilt 환경에 맞게 수정)

class PreferenceManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences("scoi_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_ACCESS_TOKEN = "ACCESS_TOKEN"
        private const val KEY_REFRESH_TOKEN = "REFRESH_TOKEN"
        private const val KEY_SMS_EXPIRED_AT = "SMS_EXPIRED_AT"
        private const val KEY_VERIFICATION_TOKEN = "VERIFICATION_TOKEN"
        private const val KEY_PHONE_NUMBER = "PHONE_NUMBER"
        private const val KEY_VERIFY_EXPIRE_TIME = "KEY_VERIFY_EXPIRE_TIME"
    }

    fun saveAccessToken(token: String) {
        prefs.edit().putString(KEY_ACCESS_TOKEN, token).apply()
    }

    fun getAccessToken(): String = prefs.getString(KEY_ACCESS_TOKEN, "") ?: ""

    fun saveRefreshToken(token: String) {
        prefs.edit().putString(KEY_REFRESH_TOKEN, token).apply()
    }

    fun getRefreshToken(): String? = prefs.getString(KEY_REFRESH_TOKEN, null)

    fun saveSmsExpiredAt(expiredAt: String) {
        prefs.edit().putString(KEY_SMS_EXPIRED_AT, expiredAt).apply()
    }

    fun getSmsExpiredAt(): String? = prefs.getString(KEY_SMS_EXPIRED_AT, null)

    fun saveVerificationToken(token: String) {
        prefs.edit().putString(KEY_VERIFICATION_TOKEN, token).apply()
    }

    fun getVerificationToken(): String? = prefs.getString(KEY_VERIFICATION_TOKEN, null)

    fun getPhoneNumber(): String = prefs.getString(KEY_PHONE_NUMBER, "") ?: ""


    fun saveVerificationSuccess() {
        val currentTime = System.currentTimeMillis()
        val tenMinutesInMillis = 10 * 60 * 1000 // 10분
        val expireTime = currentTime + tenMinutesInMillis
        prefs.edit().putLong(KEY_VERIFY_EXPIRE_TIME, expireTime).apply()
    }


    fun isVerificationValid(): Boolean {
        val expireTime = prefs.getLong(KEY_VERIFY_EXPIRE_TIME, 0)
        val currentTime = System.currentTimeMillis()

        if (expireTime == 0L) return false
        return currentTime < expireTime // 현재 시간이 만료 전이면 true
    }

    // 데이터 초기화
    fun clear() {
        prefs.edit().clear().apply()
    }
}