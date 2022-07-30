package com.example.cialo.utils

open class Operation internal constructor(val isSuccess: Boolean, val message: String?) {
    companion object {
        fun createSuccess(): Operation {
            return Operation(true, null);
        }

        fun createFailed(message: String): Operation {
            return Operation(false, message);
        }
    }
}

open class ValueOperation<TValue> internal constructor(val value: TValue?, val succeed: Boolean, val message: String?) {
    companion object {
        fun <TValue> createSuccess(value: TValue): ValueOperation<TValue> {
            return ValueOperation(value, true, null);
        }

        fun <TValue> createFailed(message: String): ValueOperation<TValue> {
            return ValueOperation(null, false, message);
        }
    }
}

class HttpOperation(val statusCode: Int, isSuccess: Boolean, message: String) : Operation(isSuccess, message) {
    companion object {
        fun createSuccess(): HttpOperation {
            return HttpOperation(200, true, "");
        }

        fun createFailed(statusCode: Int, message: String): HttpOperation {
            return HttpOperation(statusCode, false, message);
        }
    }
}

class HttpValueOperation<TValue>(val statusCode: Int, value: TValue?, succeed: Boolean, message: String) : ValueOperation<TValue>(value, succeed, message) {
    companion object {
        fun <TValue> createSuccess(value: TValue): HttpValueOperation<TValue> {
            return HttpValueOperation(200, value, true, "");
        }

        fun <TValue> createFailed(statusCode: Int, message: String): HttpValueOperation<TValue> {
            return HttpValueOperation(statusCode, null, false, message);
        }
    }
}

