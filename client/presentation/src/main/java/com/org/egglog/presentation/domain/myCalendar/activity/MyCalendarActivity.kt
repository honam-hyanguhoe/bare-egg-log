package com.org.egglog.presentation.domain.myCalendar.activity

import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.org.egglog.presentation.domain.myCalendar.navigation.MyCalendarNavigationHost
import com.org.egglog.presentation.theme.ClientTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyCalendarActivity:  AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val deepLinkUri: Uri? = intent?.data
        setContent {
            ClientTheme {
                MyCalendarNavigationHost(deepLinkUri = deepLinkUri)
            }
        }
    }

}