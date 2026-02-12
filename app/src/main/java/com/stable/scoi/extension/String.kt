package com.stable.scoi.extension

fun String?.isJsonObject(): Boolean = this?.startsWith("{") == true && this.endsWith("}")
fun String?.isJsonArray(): Boolean = this?.startsWith("[") == true && this.endsWith("]")

fun String.toPhoneNumber(): String {
    if (this.length != 11) return this

    val front = this.substring(0, 3)
    val middle = this.substring(3,7)
    val last = this.substring(7,11)

    return "$front-$middle-$last"
}