package com.org.egglog.data.scheduler.usecase

import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.org.egglog.domain.scheduler.usecase.WorkerUseCase
import javax.inject.Inject

class WorkerUseCaseImpl @Inject constructor(
    private val workManager: WorkManager
) : WorkerUseCase {

    override fun workerDailyWork() {
        val workRequest: PeriodicWorkRequest = MyWorker.createWorkRequest()

        workManager.enqueueUniquePeriodicWork(
            "DailyWork",
            androidx.work.ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }
}
