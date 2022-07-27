package com.example.cialo.services.proximity

import com.example.cialo.utils.Operation

interface IProximityRunner {

    suspend fun run() : Operation;
    fun stop();
}