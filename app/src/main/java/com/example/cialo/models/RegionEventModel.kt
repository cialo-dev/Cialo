package com.example.cialo.models

import com.example.cialo.services.database.RegionEventType
import java.time.LocalDateTime

class RegionEventModel(val dateTime: Long, val eventType: RegionEventType);
