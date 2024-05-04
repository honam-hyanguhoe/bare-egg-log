package com.org.egglog.presentation.domain.community.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class WritePostViewModel @Inject constructor() : ViewModel(), ContainerHost<PostState, Nothing> {
    override val container = container<PostState, Nothing>(PostState())

    fun onTitleChange(title: String) = intent {
        reduce {
            state.copy(title = title)
        }
    }

    fun onContentChange(content: String) = intent {
        reduce {
            state.copy(content = content)
        }
    }

    fun onPostClick() = intent {
        Log.i("커뮤니티", "제목 ${state.title} 내용 ${state.content}")
        println("커뮤니티 Title: ${state.title}, Content: ${state.content}")
    }
}

data class PostState(
    val title : String = "",
    val content :  String = ""
)