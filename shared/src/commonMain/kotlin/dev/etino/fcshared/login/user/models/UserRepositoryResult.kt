package dev.etino.fcshared.login.user.models

sealed class UserRepositoryResult {

    sealed class LoginResult : UserRepositoryResult() {
        data class Success(val data: User) : LoginResult()
        class Failure(throwable: Throwable) : LoginResult()
    }

}
