package com.example.cialo.exceptionHandling

enum class AppErrorType(value: Int) {
    Unspecified(0),
    ApiError(1),
    Permissions(10),
    GoogleSignIn(100),
    CoroutineViewModel(200),
    EstimoteInitialization(500)
}