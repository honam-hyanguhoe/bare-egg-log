package com.org.egglog.data.scheduler.di

import android.app.AlarmManager
import android.content.Context
import com.org.egglog.data.scheduler.usecase.SchedulerUseCaseImpl
import com.org.egglog.domain.scheduler.SchedulerUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SchedulerModule {
    @Binds
    abstract fun bindSchedulerUseCase(uc: SchedulerUseCaseImpl): SchedulerUseCase

    companion object {
        @Provides
        fun provideAlarmManager(@ApplicationContext context: Context): AlarmManager {
            return context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        }
    }
}