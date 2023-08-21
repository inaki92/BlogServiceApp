package com.bignerdranch.android.blognerdranch.utils

import android.util.Log
import retrofit2.Response
import java.text.SimpleDateFormat


suspend fun <T> makeNetworkCall(
    isNetworkAvailable: () -> Boolean,
    request: suspend () -> Response<T>,
    success: suspend (State.SUCCESS<T>) -> Unit,
    error: suspend (State.ERROR) -> Unit
) {

    try {
        if (isNetworkAvailable()) {
            val response = request.invoke()
            if (response.isSuccessful) {
                response.body()?.let {
                    success(State.SUCCESS(it))
                } ?: throw NullBodyException()
            } else {
                throw FailResponseException(response.errorBody()?.string())
            }
        } else {
            throw NoNetworkAvailable()
        }
    } catch (e: Exception) {
        error(State.ERROR(e))
    }
}

fun String?.parseDisplayDate(): String {
    // val formatter = DateTimeFormatter.ISO_INSTANT
    //return LocalDateTime.parse(this, formatter).format()

    try {
        this?.let {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            val outputFormat = SimpleDateFormat("MM-dd-yyyy")
            val date = inputFormat.parse(this)
            return outputFormat.format(date)
        } ?: return "-----"
    } catch (e: Exception) {
        Log.e("Parsing Date", "parseDisplayDate: Error parsing date ${e.localizedMessage}", e)
        return "----"
    }
}