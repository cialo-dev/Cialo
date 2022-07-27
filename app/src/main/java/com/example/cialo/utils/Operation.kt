package com.example.cialo.utils

class Operation private constructor(val isSuccess: Boolean, val message: String?) {
    companion object {
        fun createSuccess(): Operation {
            return Operation(true, null);
        }

        fun createFailed(message: String): Operation {
            return Operation(false, message);
        }
    }
}

class ValueOperation<TValue> private constructor(val value: TValue?, val succeed: Boolean, val message: String?) {
    companion object {
        fun <TValue> createSuccess(value: TValue): ValueOperation<TValue> {
            return ValueOperation(value, true, null);
        }

        fun <TValue> createFailed(message: String): ValueOperation<TValue> {
            return ValueOperation(null, false, message);
        }
    }
}

