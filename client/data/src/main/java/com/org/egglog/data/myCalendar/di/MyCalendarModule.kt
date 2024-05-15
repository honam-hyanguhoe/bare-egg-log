package com.org.egglog.data.myCalendar.di

import com.org.egglog.data.myCalendar.usecase.CreatePersonalScheduleUseCaseImpl
import com.org.egglog.data.myCalendar.usecase.CreateWorkScheduleUseCaseImpl
import com.org.egglog.data.myCalendar.usecase.EditWorkScheduleUseCaseImpl
import com.org.egglog.data.myCalendar.usecase.GetPersonalScheduleUseCaseImpl
import com.org.egglog.data.myCalendar.usecase.GetWorkListUseCaseImpl
import com.org.egglog.data.myCalendar.usecase.GetWorkTypeListUseCaseImpl
import com.org.egglog.domain.myCalendar.usecase.CreatePersonalScheduleUseCase
import com.org.egglog.domain.myCalendar.usecase.CreateWorkScheduleUseCase
import com.org.egglog.domain.myCalendar.usecase.EditWorkScheduleUseCase
import com.org.egglog.domain.myCalendar.usecase.GetPersonalScheduleUseCase
import com.org.egglog.domain.myCalendar.usecase.GetWorkListUseCase
import com.org.egglog.domain.myCalendar.usecase.GetWorkTypeListUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class MyCalendarModule {

    @Binds
    abstract fun bindGetWorkTypeListUseCaseImpl(uc: GetWorkTypeListUseCaseImpl): GetWorkTypeListUseCase

    @Binds
    abstract fun bindCreatePersonalScheduleUseCaseImpl(uc: CreatePersonalScheduleUseCaseImpl): CreatePersonalScheduleUseCase

    @Binds
    abstract fun bindCreateWorkScheduleUseCaseImpl(uc: CreateWorkScheduleUseCaseImpl): CreateWorkScheduleUseCase

    @Binds
    abstract fun bindEditWorkScheduleUseCaseImpl(uc: EditWorkScheduleUseCaseImpl): EditWorkScheduleUseCase

    @Binds
    abstract fun bindGetWorkListUseCaseImpl(uc: GetWorkListUseCaseImpl): GetWorkListUseCase

    @Binds
    abstract fun bindGetPersonalScheduleUseCaseImpl(uc: GetPersonalScheduleUseCaseImpl): GetPersonalScheduleUseCase

}