package com.example.regyboxscheduler.repository

import android.app.PendingIntent
import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase

@Entity
data class Schedule(
    @PrimaryKey val classId: String,
    @ColumnInfo(name = "pendingIntent") val pendingIntent: PendingIntent
)

@Dao
interface ScheduleDao {
    @Query("SELECT * FROM schedule")
    fun getAll(): List<Schedule>

    @Query("SELECT * FROM schedule WHERE classId = :classId)")
    fun findById(classId: String): Schedule?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(schedule: Schedule)

    @Delete
    fun delete(schedule: Schedule)
}

@Database(entities = [Schedule::class], version = 1)
abstract class SchedulerDatabase : RoomDatabase() {

    abstract fun scheduleDao(): ScheduleDao

    companion object {
        private var instance: SchedulerDatabase? = null

        @Synchronized
        fun getInstance(ctx: Context): SchedulerDatabase {
            if(instance == null)
                instance = Room.databaseBuilder(ctx.applicationContext, SchedulerDatabase::class.java,
                    "SchedulerDatabase")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build()

            return instance!!
        }

        private val roomCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
            }
        }

    }
}