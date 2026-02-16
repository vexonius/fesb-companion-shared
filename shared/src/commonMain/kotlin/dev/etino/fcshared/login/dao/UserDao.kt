package dev.etino.fcshared.login.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import dev.etino.fcshared.login.user.models.UserRoom
import kotlinx.coroutines.flow.Flow


@Dao
interface UserDao {

    @Insert(onConflict = REPLACE)
    fun insert(user: UserRoom)

    @Query("SELECT * FROM userroom")
    fun getUser(): UserRoom

    @Query("DELETE FROM userroom")
    fun deleteAllUserData()

    @Query("SELECT * FROM userroom LIMIT 1")
    fun observeUserChanges(): Flow<UserRoom?>
}
