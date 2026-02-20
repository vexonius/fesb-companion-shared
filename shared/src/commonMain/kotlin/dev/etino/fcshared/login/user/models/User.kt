package dev.etino.fcshared.login.user.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserRoom(
    @PrimaryKey
    var id: Int = ID,
    val fullName: String = "",
    var username: String = "",
    var password: String = ""
) {
    constructor(user: User) : this(
        id = ID,
        fullName = user.fullName,
        username = user.username,
        password = user.password
    )

    companion object {
        const val ID = 0
    }
}

data class User(
    val fullName: String,
    var username: String,
    var password: String
) {

    constructor(userRoom: UserRoom) : this(userRoom.fullName, userRoom.username, userRoom.password)

    val email: String
        get() = "$username@fesb.hr"

}

