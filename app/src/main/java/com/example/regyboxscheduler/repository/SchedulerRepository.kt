package com.example.regyboxscheduler.repository

import android.app.Application

class SchedulerRepository(application: Application): ScheduleDao {

    private val db = SchedulerDatabase.getInstance(application)
    private val schedulerDao = db.scheduleDao()

    override fun getAll(): List<Schedule> {
        return schedulerDao.getAll()
    }

    override fun findById(classId: String): Schedule? {
        return schedulerDao.findById(classId)
    }

    override fun insert(schedule: Schedule) {
        schedulerDao.insert(schedule)
    }

    override fun delete(schedule: Schedule) {
        schedulerDao.delete(schedule)
    }
}