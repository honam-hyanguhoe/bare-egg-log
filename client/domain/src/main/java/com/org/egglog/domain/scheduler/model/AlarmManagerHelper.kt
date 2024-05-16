package com.org.egglog.domain.scheduler.model

object AlarmManagerHelper {
    private val alarms = mutableMapOf<Int, AlarmData>()

    fun addAlarm(alarmData: AlarmData) {
        alarms[alarmData.key] = alarmData
    }

    fun getAlarm(key: Int): AlarmData? {
        return alarms[key]
    }

    fun deleteAlarm(key: Int) {
        alarms.remove(key)
    }
}
