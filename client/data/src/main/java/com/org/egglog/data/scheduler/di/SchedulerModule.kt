package com.org.egglog.data.scheduler.di

import android.app.AlarmManager
import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkManager
import com.org.egglog.data.scheduler.usecase.*
import com.org.egglog.domain.scheduler.usecase.GetAlarmFindListUseCase
import com.org.egglog.domain.scheduler.usecase.GetAlarmPersonalScheduleUseCase
import com.org.egglog.domain.scheduler.usecase.NotificationUseCase
import com.org.egglog.domain.scheduler.usecase.SchedulerUseCase
import com.org.egglog.domain.scheduler.usecase.WorkerUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import dagger.multibindings.ClassKey
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SchedulerModule {
    @Binds
    abstract fun bindSchedulerUseCase(uc: SchedulerUseCaseImpl): SchedulerUseCase

    @Binds
    abstract fun bindWorkerUseCase(uc: WorkerUseCaseImpl): WorkerUseCase

    @Binds
    abstract fun bindGetAlarmFindListUseCase(uc: GetAlarmFindListUseCaseImpl): GetAlarmFindListUseCase

    @Binds
    abstract fun bindGetAlarmPersonalScheduleUseCase(uc: GetAlarmPersonalScheduleUseCaseImpl): GetAlarmPersonalScheduleUseCase

    @Binds
    abstract fun bindNotificationUseCase(uc: NotificationUseCaseImpl): NotificationUseCase

    @Binds
    @IntoMap
    @ClassKey(MyWorker::class)
    abstract fun bindMyWorkerFactory(factory: MyWorker.Factory): ChildWorkerFactory

    companion object {
        @Provides
        @Singleton
        fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
            return WorkManager.getInstance(context)
        }

        @Provides
        @Singleton
        fun provideCustomWorkerFactory(
            workerFactories: Map<Class<out ListenableWorker>, @JvmSuppressWildcards Provider<ChildWorkerFactory>>
        ): CustomWorkerFactory {
            return CustomWorkerFactory(workerFactories)
        }

        @Provides
        @Singleton
        fun provideAlarmManager(@ApplicationContext context: Context): AlarmManager {
            return context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        }

        @Provides
        @Singleton
        fun provideWorkerFactories(
            myWorkerFactory: MyWorker.Factory
        ): Map<Class<out ListenableWorker>, Provider<ChildWorkerFactory>> {
            return mapOf(
                MyWorker::class.java to Provider { myWorkerFactory }
            )
        }
    }
}
