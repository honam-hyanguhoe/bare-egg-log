package com.org.egglog.data.myCalendar.di

import com.org.egglog.data.myCalendar.usecase.CreatePersonalScheduleUseCaseImpl
import com.org.egglog.data.myCalendar.usecase.CreateWorkScheduleUseCaseImpl
import com.org.egglog.data.myCalendar.usecase.DeletePersonalScheduleUseCaseImpl
import com.org.egglog.data.myCalendar.usecase.EditWorkScheduleUseCaseImpl
import com.org.egglog.data.myCalendar.usecase.GetDetailPersonalScheduleUseCaseImpl
import com.org.egglog.data.myCalendar.usecase.GetPersonalScheduleUseCaseImpl
import com.org.egglog.data.myCalendar.usecase.GetWorkListUseCaseImpl
import com.org.egglog.data.myCalendar.usecase.GetWorkTypeListUseCaseImpl
import com.org.egglog.data.myCalendar.usecase.ModifyPersonalScheduleUseCaseImpl
import com.org.egglog.domain.myCalendar.usecase.CreatePersonalScheduleUseCase
import com.org.egglog.domain.myCalendar.usecase.CreateWorkScheduleUseCase
import com.org.egglog.domain.myCalendar.usecase.DeletePersonalScheduleUseCase
import com.org.egglog.domain.myCalendar.usecase.EditWorkScheduleUseCase
import com.org.egglog.domain.myCalendar.usecase.GetDetailPersonalScheduleUseCase
import com.org.egglog.domain.myCalendar.usecase.GetPersonalScheduleUseCase
import com.org.egglog.domain.myCalendar.usecase.GetWorkListUseCase
import com.org.egglog.domain.myCalendar.usecase.GetWorkTypeListUseCase
import com.org.egglog.domain.myCalendar.usecase.ModifyPersonalScheduleUseCase
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

    @Binds
    abstract fun bindDeletePersonalScheduleUseCaseImpl(uc: DeletePersonalScheduleUseCaseImpl): DeletePersonalScheduleUseCase

    @Binds
    abstract fun bindGetDetailPersonalScheduleUseCaseImpl(uc: GetDetailPersonalScheduleUseCaseImpl): GetDetailPersonalScheduleUseCase

    @Binds
    abstract fun bindModifyPersonalScheduleUseCaseImpl(uc: ModifyPersonalScheduleUseCaseImpl): ModifyPersonalScheduleUseCase

}