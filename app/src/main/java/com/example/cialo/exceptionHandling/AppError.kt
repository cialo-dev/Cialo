package com.example.cialo.exceptionHandling

import com.estimote.mustard.rx_goodness.rx_requirements_wizard.Requirement
import com.example.cialo.ui.abstraction.CialoViewModel

open class AppError(val type: AppErrorType, val message: String, val exception: Throwable? = null) {

    companion object {
        fun unspecified(message: String): AppError {
            return AppError(AppErrorType.Unspecified, message);
        }

        fun unspecified(exception: Throwable): AppError {
            return AppError(AppErrorType.Unspecified, exception.toString(), exception);
        }

        fun unspecified(message: String, exception: Throwable): AppError {
            return AppError(AppErrorType.Unspecified, exception.toString(), exception);
        }
    }
}

open class ApiAppError(val statusCode: Int, message: String, ex: Throwable?) : AppError(AppErrorType.ApiError, message, ex){

}

open class CoroutineViewModelAppError(val vm: CialoViewModel, ex: Throwable) : AppError(AppErrorType.CoroutineViewModel, vm.toString(), ex) {

}

open class GoogleSignInAppError(message: String, exception: Throwable? = null) : AppError(AppErrorType.GoogleSignIn, message, exception) {

}

open class EstimotePermissionsError(val requirements: List<Requirement>) :
    AppError(AppErrorType.Permissions, "Missing") {
}