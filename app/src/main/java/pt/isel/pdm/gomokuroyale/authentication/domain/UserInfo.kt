package pt.isel.pdm.gomokuroyale.authentication.domain

data class User(
    val id: Id,
    val username: String,
    val email: Email,
) {

    init {
        require(validateUser(this))
    }
    private fun validateUser(user: User): Boolean {
        return validateUsername(user.username) && validateEmail(user.email.value)
    }
}

data class PasswordValidationInfo(val validationInfo: String) {
    init {
        require(validatePasswordValidationInfo(this))
    }
    private fun validatePasswordValidationInfo(passwordValidationInfo: PasswordValidationInfo): Boolean {
        return passwordValidationInfo.validationInfo.isNotBlank()
    }
}

data class Email(val value: String) {
    init {
        require(validateEmail(this))
    }
    private fun validateEmail(email: Email): Boolean {
        return email.value.isNotBlank()
    }
}

data class Id(val value: Int)


data class UserInfo(val accessToken: String, val username: String) {
    init {
        require(validateUserInfo(this))
    }

    private fun validateUserInfo(userInfo: UserInfo): Boolean {
        return userInfo.accessToken.isNotBlank() &&
                userInfo.username.isNotBlank()
    }
}