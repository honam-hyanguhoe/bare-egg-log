package com.org.egglog.data.group.di

import com.org.egglog.data.community.usecase.GetHotPostListUseCaseImpl
import com.org.egglog.data.group.usecase.ChangeLeaderUseCaseImpl
import com.org.egglog.data.group.usecase.CreateGroupUseCaseImpl
import com.org.egglog.data.group.usecase.DeleteMemberUseCaseImpl
import com.org.egglog.data.group.usecase.ExitGroupUseCaseImpl
import com.org.egglog.data.group.usecase.GetDutyTagUseCaseImpl
import com.org.egglog.data.group.usecase.GetGroupDutyUseCaseImpl
import com.org.egglog.data.group.usecase.GetGroupInfoUseCaseImpl
import com.org.egglog.data.group.usecase.GetGroupListUseCaseImpl
import com.org.egglog.data.group.usecase.GetInvitationCodeUseCaseImpl
import com.org.egglog.data.group.usecase.GetMembersWorkUseCaseImpl
import com.org.egglog.data.group.usecase.InviteMemberUseCaseImpl
import com.org.egglog.data.group.usecase.UpdateGroupInfoUseCaseImpl
import com.org.egglog.domain.community.usecase.GetHotPostListUseCase
import com.org.egglog.domain.group.usecase.ChangeLeaderUseCase
import com.org.egglog.domain.group.usecase.CreateGroupUseCase
import com.org.egglog.domain.group.usecase.DeleteMemberUseCase
import com.org.egglog.domain.group.usecase.ExitGroupUseCase
import com.org.egglog.domain.group.usecase.GetGroupDutyUseCase
import com.org.egglog.domain.group.usecase.GetGroupInfoUseCase
import com.org.egglog.domain.group.usecase.GetGroupListUseCase
import com.org.egglog.domain.group.usecase.GetInvitationCodeUseCase
import com.org.egglog.domain.group.usecase.GetMembersWorkUseCase
import com.org.egglog.domain.group.usecase.InviteMemberUseCase
import com.org.egglog.domain.group.usecase.UpdateGroupInfoUseCase
import com.org.egglog.domain.group.usecase.getDutyTagUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class GroupModule {
    @Binds
    abstract fun bindGetGroupListUseCaseImpl(uc: GetGroupListUseCaseImpl): GetGroupListUseCase

    @Binds
    abstract fun bindCreateGroupUseCaseImpl(uc : CreateGroupUseCaseImpl) : CreateGroupUseCase

    @Binds
    abstract fun getGroupInfoUseCaseImpl(uc : GetGroupInfoUseCaseImpl) : GetGroupInfoUseCase

    @Binds
    abstract fun getGroupDutyUseCaseImpl(uc : GetGroupDutyUseCaseImpl) : GetGroupDutyUseCase

    @Binds
    abstract fun inviteMemberUseCaseImpl(uc : InviteMemberUseCaseImpl) : InviteMemberUseCase

    @Binds
    abstract fun getInvitationCodeUseCaseImpl(uc : GetInvitationCodeUseCaseImpl) : GetInvitationCodeUseCase

    @Binds
    abstract fun getMembersWorkUseCaseImpl(uc : GetMembersWorkUseCaseImpl) : GetMembersWorkUseCase

    @Binds
    abstract fun updateGroupInfoUseCaseImpl(uc : UpdateGroupInfoUseCaseImpl) : UpdateGroupInfoUseCase

    @Binds
    abstract fun deleteMemberUseCaseImpl(uc : DeleteMemberUseCaseImpl) : DeleteMemberUseCase

    @Binds
    abstract fun changeLeaderUseCaseImpl(uc : ChangeLeaderUseCaseImpl) : ChangeLeaderUseCase

    @Binds
    abstract fun exitGroupUseCaseImpl(uc : ExitGroupUseCaseImpl) : ExitGroupUseCase

    @Binds
    abstract fun getDutyTagUseCaseImpl(uc : GetDutyTagUseCaseImpl) : getDutyTagUseCase

}