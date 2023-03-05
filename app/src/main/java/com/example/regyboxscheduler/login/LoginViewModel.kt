package com.example.regyboxscheduler.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.regyboxscheduler.TAG
import com.example.regyboxscheduler.services.RegyboxServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class LoginViewModel(
    private val services: RegyboxServices
) : ViewModel() {

    private val _boxes = MutableStateFlow<HashMap<String, String>>(hashMapOf())
    val boxes = _boxes.asStateFlow()

    fun login(boxId: String, username: String, password: String, callback: (String?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val cookie = services.login(boxId, username, password)
                callback(cookie)
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }
    }

    fun getBoxes() {
        viewModelScope.launch(Dispatchers.IO) {
            _boxes.value =
                try {
                    val doc = Jsoup.parse(services.getBoxes())
                    val boxes = hashMapOf<String, String>()
                    val rows = doc.getElementsByClass("item-inner")

                    for (i in 1 until rows.size){
                        val boxName = rows[i].child(0).ownText()
                        val boxId = rows[i].attr("onclick").substringAfter("abre_login('").substringBefore("')")

                        boxes[boxName] = boxId
                    }

                    boxes
                } catch (e: Exception) {
                    Log.e(TAG, e.toString())
                    hashMapOf()
                }
        }
    }
}
