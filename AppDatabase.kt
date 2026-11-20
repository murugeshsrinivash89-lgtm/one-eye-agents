/*
 * ONE EYE CORE v1.0
 * AppDatabase.kt
 *
 * DEMO IMPLEMENTATION - Room local persistence.
 * Requires Room runtime, Room KTX, and Room compiler dependencies.
 */

package com.oneeyecore.demo

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "task_logs")
data class TaskLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val taskName: String,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "emotion_check_ins")
data class EmotionCheckInEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val mood: String,
    val stressLevel: Int,
    val fatigueLevel: Int,
    val createdAt: Long = System.currentTimeMillis()
)

@Dao
interface OneEyeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskLogEntity): Long

    @Query("UPDATE task_logs SET isCompleted = 1 WHERE id = :taskId")
    suspend fun completeTask(taskId: Long)

    @Query("SELECT * FROM task_logs ORDER BY createdAt DESC")
    fun observeTasks(): Flow<List<TaskLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCheckIn(checkIn: EmotionCheckInEntity): Long

    @Query("SELECT * FROM emotion_check_ins ORDER BY createdAt DESC")
    fun observeCheckIns(): Flow<List<EmotionCheckInEntity>>
}

@Database(
    entities = [TaskLogEntity::class, EmotionCheckInEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun oneEyeDao(): OneEyeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "one_eye_core.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
