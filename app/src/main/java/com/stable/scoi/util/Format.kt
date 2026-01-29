package com.stable.scoi.util

object Format {
    fun formatWon(raw: String): String? {
        val digits = raw.filter { it.isDigit() }
        if (digits.isBlank()) return null

        val value = digits.toLongOrNull() ?: return null

        val formatted = java.text.NumberFormat.getInstance(java.util.Locale.KOREA).format(value)
        return "${formatted}원"
    }

    fun unformatWon(raw: String): String {
        return raw.filter { it.isDigit() }
    }
}