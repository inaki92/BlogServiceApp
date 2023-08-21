package com.bignerdranch.android.blognerdranch.utils

class NullBodyException(message: String? = "Null body in response") : Exception(message)
class FailResponseException(message: String?) : Exception(message)
class NoNetworkAvailable(message: String? = "No internet connection available") : Exception(message)
class NoPostSelectedException(message: String? = "No post selected to retrieve details") : Exception(message)
class NoPostIdFoundException(message: String? = "No ID found for the selected post") : Exception(message)