package com.example.cialo.ui.abstraction

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cialo.exceptionHandling.AppError
import com.example.cialo.exceptionHandling.CoroutineViewModelAppError
import com.example.cialo.exceptionHandling.ViewModelErrorState
import com.example.cialo.services.logging.ILog
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import org.koin.java.KoinJavaComponent

abstract class CialoViewModel : ViewModel() {
    protected val Logger: ILog by KoinJavaComponent.inject(ILog::class.java)
    protected val CancellableJobs: MutableList<Job> = mutableListOf();

    val errorState: MutableLiveData<ViewModelErrorState> = MutableLiveData(ViewModelErrorState());

    protected fun getCoroutineExceptionHandler(): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { _, throwable ->
            onError(CoroutineViewModelAppError(this, throwable))
        }
    }

    open fun onError(error: AppError) {
        Logger.error(error);

        errorState.value!!.message = this.getUiMessage(error)
    }

    open fun getUiMessage(error: AppError): String {
        return error.message;
    }

    override fun onCleared() {
        super.onCleared()

        for (job in CancellableJobs) {
            job.cancel()
        }
    }
}