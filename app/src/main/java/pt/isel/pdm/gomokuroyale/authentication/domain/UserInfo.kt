package pt.isel.pdm.gomokuroyale.authentication.domain

data class User(val username: String, val email: String, val password: String) {

    init {
        require(validateUser(this))
    }
    private fun validateUser(user: User): Boolean {
        return validateUsername(user.username) && validateEmail(user.email) &&
                validatePassword(user.password)
    }
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