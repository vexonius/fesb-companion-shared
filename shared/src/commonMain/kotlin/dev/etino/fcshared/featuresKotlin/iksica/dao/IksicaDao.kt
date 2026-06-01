package dev.etino.fcshared.featuresKotlin.iksica.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import dev.etino.fcshared.featuresKotlin.iksica.models.ReceiptRoom
import dev.etino.fcshared.featuresKotlin.iksica.models.StudentDataRoom


@Dao
interface IksicaDao {
    @Query("DELETE FROM receiptroom")
    suspend fun deleteAllReceipts()

    @Query("DELETE FROM studentdataroom")
    suspend fun deleteStudent()

    @Insert(onConflict = REPLACE)
    suspend fun insert(studentData: StudentDataRoom)

    @Insert(onConflict = REPLACE)
    suspend fun insert(receipts: List<ReceiptRoom>)

    @Query("SELECT * FROM studentdataroom")
    suspend fun readData(): StudentDataRoom?

    @Query("SELECT * FROM receiptroom")
    suspend fun readReceipts(): List<ReceiptRoom>
}