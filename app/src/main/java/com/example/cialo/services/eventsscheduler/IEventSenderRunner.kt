package com.example.cialo.services.eventsscheduler

import com.example.cialo.utils.Operation

interface IEventSenderRunner {
    fun run() : Operation
}