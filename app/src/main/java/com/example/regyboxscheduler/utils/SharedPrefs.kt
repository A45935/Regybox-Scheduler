package com.example.regyboxscheduler.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPrefs (private val context: Context) {

    val prefs: SharedPreferences by lazy {
        context.getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE)
    }

    private val boxCookie = "regybox_boxes"
    private val userCookie = "regybox_user"

    var cookies: Cookies?
        get() {
            val savedBoxesCookie = prefs.getString(boxCookie, null)
            val savedUserCookie = prefs.getString(userCookie,null)

            return if (savedBoxesCookie != null && savedUserCookie != null)
                Cookies(savedBoxesCookie, savedUserCookie)
            else
                null
        }

        set(value) {
            if (value == null)
                prefs.edit()
                    .remove(boxCookie)
                    .remove(userCookie)
                    .apply()
            else
                prefs.edit()
                    .putString(boxCookie, value.boxes)
                    .putString(userCookie, value.user)
                    .apply()
        }

    private val boxName = "savedBoxName"
    private val boxId = "savedBoxId"

    var selectedBox: Box?
        get() {
            val savedBoxName = prefs.getString(boxName, null)
            val savedBoxId = prefs.getString(boxId,null)

            return if (savedBoxName != null && savedBoxId != null)
                Box(savedBoxName, savedBoxId)
            else
                null
        }

        set(value) {
            if (value == null)
                prefs.edit()
                    .remove(boxName)
                    .remove(boxId)
                    .apply()
            else
                prefs.edit()
                    .putString(boxName, value.name)
                    .putString(boxId, value.id)
                    .apply()
        }
}

data class Cookies (val boxes: String, val user: String)

data class Box (val name: String, val id: String)