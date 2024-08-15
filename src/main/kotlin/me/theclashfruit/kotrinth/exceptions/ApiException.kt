package me.theclashfruit.kotrinth.exceptions

import me.theclashfruit.kotrinth.utils.ApiError

class ApiException : Exception {
    constructor(error: ApiError) : super(error.description)
    constructor(error: ApiError, cause: Throwable) : super(error.description, cause)
}