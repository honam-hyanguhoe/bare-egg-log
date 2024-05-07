package com.org.egglog.data.main.di

import com.org.egglog.data.community.posteditor.usecase.WritePostUserCaseImpl
import com.org.egglog.data.main.usecase.CountRemainingDutyUseCaseImpl
import com.org.egglog.data.main.usecase.GetWeeklyWorkUseCaseImpl
import com.org.egglog.data.main.usecase.GetWorkStatsUseCaseImpl
import com.org.egglog.domain.community.posteditor.usecase.WritePostUseCase
import com.org.egglog.domain.main.usecase.CountRemainingDutyUseCase
import com.org.egglog.domain.main.usecase.GetWeeklyWorkUseCase
import com.org.egglog.domain.main.usecase.GetWorkStatsUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MainModule {
    @Binds
    abstract fun bindCountRemainingDutyUseCase(uc : CountRemainingDutyUseCaseImpl) : CountRemainingDutyUseCase

    @Binds
    abstract fun bindGetWeeklyWorkUseCase(uc : GetWeeklyWorkUseCaseImpl) : GetWeeklyWorkUseCase

    @Binds
    abstract fun bindGetWorkStatsUseCase(uc : GetWorkStatsUseCaseImpl) : GetWorkStatsUseCase
}