package com.stable.scoi.data.base

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.serialization.Serializable
import retrofit2.HttpException
import kotlin.code

@Serializable
sealed interface ApiState<out T> {
    data class Success<T>(val data: T) : ApiState<T>
    data class Fail(val failState: FailState) : ApiState<Nothing>
}

data class FailState(
    val success: Boolean = false,
    val code: String,
    val message: String
) {
    companion object {
        private const val EMPTY = ""
        val default = FailState(
            success = DEFAULT_ERROR,
            code = EMPTY,
            message = EMPTY
        )
    }
}

const val DEFAULT_ERROR = false


suspend inline fun <reified T> apiCall(
    crossinline apiCall: suspend () -> ApiResponse<T>
): ApiState<T> {
    return try {
        val response = apiCall()

        if (response.isSuccess) {
            ApiState.Success(response.result)
        } else {
            ApiState.Fail(
                FailState(success = false, code = response.code, message = "null")
            )
        }

    } catch (e: HttpException) {
        //TODO 공통 에러
        val errorBodyStr = e.response()?.errorBody()?.string()
        val type = object : TypeToken<ApiResponse<T>>() {}.type

        val errorResponse: ApiResponse<T>? = try {
            Gson().fromJson(errorBodyStr, type)
        } catch (parseException: Exception) {
            null
        }

        val code = errorResponse?.code ?: e.code().toString()
        val message = errorResponse?.message ?: e.message()

        ApiState.Fail(FailState(success = false, code = code, message = message))

    } catch (e: Exception) {
        e.printStackTrace()
        ApiState.Fail(
            FailState(success = false, code = "400", message = e.message ?: "Unknown Error")
        )
    } as ApiState<T>
}