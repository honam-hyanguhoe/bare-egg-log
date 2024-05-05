package com.org.egglog.domain.auth.usecase

import com.org.egglog.domain.auth.model.Refresh
import com.org.egglog.domain.auth.model.UserParam

interface LoginUseCase {
<<<<<<< HEAD
    suspend operator fun invoke(type: String): Result<Refresh?>
}
=======
    suspend operator fun invoke(provider: String, userParam: UserParam): Result<Refresh?>
}
>>>>>>> f46170b8b68da6f225201568164697c40591240f
