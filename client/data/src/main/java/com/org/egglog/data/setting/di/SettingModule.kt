package com.org.egglog.data.setting.di

import com.org.egglog.data.setting.usecase.CertificateBadgeUseCaseImpl
import com.org.egglog.data.setting.usecase.DeleteCalendarGroupMapStoreUseCaseImpl
import com.org.egglog.data.setting.usecase.DeleteCalendarGroupUseCaseImpl
import com.org.egglog.data.setting.usecase.DeleteWorkTypeUseCaseImpl
import com.org.egglog.data.setting.usecase.GetAlarmListUseCaseImpl
import com.org.egglog.data.setting.usecase.GetCalendarGroupListUseCaseImpl
import com.org.egglog.data.setting.usecase.GetCalendarGroupMapStoreUseCaseImpl
import com.org.egglog.data.setting.usecase.GetCalendarLinkUseCaseImpl
import com.org.egglog.data.setting.usecase.GetNotificationListUseCaseImpl
import com.org.egglog.data.setting.usecase.GetWorkTypeListUseCaseImpl
import com.org.egglog.data.setting.usecase.PostAskUseCaseImpl
import com.org.egglog.data.setting.usecase.PostCalendarGroupUseCaseImpl
import com.org.egglog.data.setting.usecase.PostCalendarSyncUseCaseImpl
import com.org.egglog.data.setting.usecase.PostWorkTypeUseCaseImpl
import com.org.egglog.data.setting.usecase.SetCalendarGroupMapStoreUseCaseImpl
import com.org.egglog.data.setting.usecase.UpdateAlarmStatusUseCaseImpl
import com.org.egglog.data.setting.usecase.UpdateAlarmUseCaseImpl
import com.org.egglog.data.setting.usecase.UpdateNotificationUseCaseImpl
import com.org.egglog.data.setting.usecase.UpdateWorkTypeUseCaseImpl
import com.org.egglog.domain.setting.usecase.CertificateBadgeUseCase
import com.org.egglog.domain.setting.usecase.DeleteCalendarGroupMapStoreUseCase
import com.org.egglog.domain.setting.usecase.DeleteCalendarGroupUseCase
import com.org.egglog.domain.setting.usecase.DeleteWorkTypeUseCase
import com.org.egglog.domain.setting.usecase.GetAlarmListUseCase
import com.org.egglog.domain.setting.usecase.GetCalendarGroupListUseCase
import com.org.egglog.domain.setting.usecase.GetCalendarGroupMapStoreUseCase
import com.org.egglog.domain.setting.usecase.GetCalendarLinkUseCase
import com.org.egglog.domain.setting.usecase.GetNotificationListUseCase
import com.org.egglog.domain.setting.usecase.GetWorkTypeListUseCase
import com.org.egglog.domain.setting.usecase.PostAskUseCase
import com.org.egglog.domain.setting.usecase.PostCalendarGroupUseCase
import com.org.egglog.domain.setting.usecase.PostCalendarSyncUseCase
import com.org.egglog.domain.setting.usecase.PostWorkTypeUseCase
import com.org.egglog.domain.setting.usecase.SetCalendarGroupMapStoreUseCase
import com.org.egglog.domain.setting.usecase.UpdateAlarmStatusUseCase
import com.org.egglog.domain.setting.usecase.UpdateAlarmUseCase
import com.org.egglog.domain.setting.usecase.UpdateNotificationUseCase
import com.org.egglog.domain.setting.usecase.UpdateWorkTypeUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingModule {
    @Binds
    abstract fun bindGetCalendarGroupListUseCase(uc: GetCalendarGroupListUseCaseImpl): GetCalendarGroupListUseCase

    @Binds
    abstract fun bindGetCalendarGroupMapStoreUseCase(uc: GetCalendarGroupMapStoreUseCaseImpl): GetCalendarGroupMapStoreUseCase

    @Binds
    abstract fun bindDeleteCalendarGroupMapStoreUseCase(uc: DeleteCalendarGroupMapStoreUseCaseImpl): DeleteCalendarGroupMapStoreUseCase

    @Binds
    abstract fun bindSetCalendarGroupMapStoreUseCase(uc: SetCalendarGroupMapStoreUseCaseImpl): SetCalendarGroupMapStoreUseCase

    @Binds
    abstract fun bindDeleteCalendarGroupUseCase(uc: DeleteCalendarGroupUseCaseImpl): DeleteCalendarGroupUseCase

    @Binds
    abstract fun bindPostCalendarSyncUseCase(uc: PostCalendarSyncUseCaseImpl): PostCalendarSyncUseCase

    @Binds
    abstract fun bindPostCalendarGroupUseCase(uc: PostCalendarGroupUseCaseImpl): PostCalendarGroupUseCase

    @Binds
    abstract fun bindGetWorkTypeListUseCase(uc: GetWorkTypeListUseCaseImpl): GetWorkTypeListUseCase

    @Binds
    abstract fun bindPostWorkTypeUseCase(uc: PostWorkTypeUseCaseImpl): PostWorkTypeUseCase

    @Binds
    abstract fun bindDeleteWorkTypeUseCase(uc: DeleteWorkTypeUseCaseImpl): DeleteWorkTypeUseCase

    @Binds
    abstract fun bindUpdateWorkTypeUseCase(uc: UpdateWorkTypeUseCaseImpl): UpdateWorkTypeUseCase

    @Binds
    abstract fun bindPostAskUseCase(uc: PostAskUseCaseImpl): PostAskUseCase

    @Binds
    abstract fun bindGetAlarmListUseCase(uc: GetAlarmListUseCaseImpl): GetAlarmListUseCase

    @Binds
    abstract fun bindUpdateAlarmStatusUseCase(uc: UpdateAlarmStatusUseCaseImpl): UpdateAlarmStatusUseCase

    @Binds
    abstract fun bindUpdateAlarmUseCase(uc: UpdateAlarmUseCaseImpl): UpdateAlarmUseCase

    @Binds
    abstract fun bindGetCalendarLinkUseCase(uc: GetCalendarLinkUseCaseImpl): GetCalendarLinkUseCase

    @Binds
    abstract fun bindGetNotificationListUseCase(uc: GetNotificationListUseCaseImpl): GetNotificationListUseCase

    @Binds
    abstract fun bindUpdateNotificationUseCase(uc: UpdateNotificationUseCaseImpl): UpdateNotificationUseCase

    @Binds
    abstract fun bindCertificateBadgeUseCase(uc: CertificateBadgeUseCaseImpl): CertificateBadgeUseCase
}
