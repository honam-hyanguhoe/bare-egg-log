package com.org.egglog.presentation.domain.group.navigation

import com.org.egglog.presentation.domain.main.navigation.MainRoute

sealed class GroupRoute(val name : String){
    data object GroupListScreen : GroupRoute("GroupListScreen")
    data object GroupDetailScreen : GroupRoute("GroupDetailScreen")
    data object MemberManageScreen : GroupRoute("MemberManageScreen")
    data object InviteScreen : GroupRoute("invite/{code}/{name}")
}