package dev.etino.fcshared.Utils

enum class SecureField {

    USERNAME, PASSWORD;

    val value: String
        get() {
            return when (this) {
                USERNAME -> "username"
                PASSWORD -> "password"
            }
        }

}