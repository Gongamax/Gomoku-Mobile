package pt.isel.pdm.gomokuroyale.authentication.domain

private const val MIN_USERNAME_LENGTH = 3
const val MAX_USERNAME_LENGTH = 30
private const val MIN_PASSWORD_LENGTH = 8
const val MAX_PASSWORD_LENGTH = 127

private const val USERNAME_REGEX = "^[a-zA-Z0-9_]*$"
private const val PASSWORD_REGEX = "^[a-zA-Z0-9_]*$"

private const val PASSWORD_HASH_ALGORITHM = "SHA-256"

private const val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$"


fun validateUsername(username: String): Boolean {
    return username.length in MIN_USERNAME_LENGTH..MAX_USERNAME_LENGTH &&
            username.matches(USERNAME_REGEX.toRegex())
}

fun validatePassword(password: String): Boolean {
    return password.length in MIN_PASSWORD_LENGTH..MAX_PASSWORD_LENGTH &&
            password.matches(PASSWORD_REGEX.toRegex())
}

fun validateEmail(email: String): Boolean {
    return email.matches(EMAIL_REGEX.toRegex())
}