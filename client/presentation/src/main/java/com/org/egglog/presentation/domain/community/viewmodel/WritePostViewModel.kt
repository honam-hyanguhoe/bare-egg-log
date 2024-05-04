package com.org.egglog.presentation.domain.community.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.org.egglog.domain.community.usecase.WritePostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class WritePostViewModel @Inject constructor(
    private val writePostUseCase: WritePostUseCase
) : ViewModel(), ContainerHost<PostState, Nothing> {
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
        Log.d("커뮤니티", "제목 ${state.title} 내용 ${state.content}")
        println("커뮤니티 Title: ${state.title}, Content: ${state.content}")
        val postResult = writePostUseCase(
            boardTitle = state.title,
            boardContent = state.content,
            pictureOne = "",
            pictureTwo = "",
            pictureThree = "",
            pictureFour = ""
        )

        Log.d("커뮤니티 writePostViewModel", "$postResult")
    }
}


data class PostState(
    val title : String = "",
    val content :  String = ""
)