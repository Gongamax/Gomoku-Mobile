package pt.isel.pdm.gomokuroyale.authentication.domain

data class User(val username: String, val email: String, val password: String) {

    init {
        require(validateUser(this))
    }

}

fun validateUser(user: User): Boolean {
    return user.username.isNotBlank() && user.password.isNotBlank() &&
            user.password.length >= 8 && user.email.isNotBlank()
            && user.email.contains("@")
}

fun validateRegister(user: User, passwordConfirmation: String): Boolean{
    return user.password == passwordConfirmation && validateUser(user)
}

data class UserInfo(val accessToken: String, val username: String) {

    init {
        require(validateUserInfo(this))
    }

    private fun validateUserInfo(userInfo: UserInfo): Boolean {
        return userInfo.accessToken.isNotBlank() &&
                userInfo.username.isNotBlank()
    }
}