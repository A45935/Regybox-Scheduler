package com.example.regyboxscheduler.scheduler

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.regyboxscheduler.services.RegyboxServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

// one day in seconds
private const val DAY = 86400

class ScheduleViewModel(
    private val services: RegyboxServices
) : ViewModel() {

    //current epoch time in seconds
    private val _timestamp = MutableStateFlow(System.currentTimeMillis()/1000)
    val timestamp = _timestamp.asStateFlow()

    private val _classes = MutableStateFlow<List<GymClass>>(emptyList())
    val classes = _classes.asStateFlow()

    fun getClasses() {
        viewModelScope.launch(Dispatchers.IO) {
            _classes.value =
                try {
                    val doc = Jsoup.parse(services.getClasses(_timestamp.value.toString().padEnd(13, '0')))
                    val classes = mutableListOf<GymClass>()

                    // cada aula tem 3 elementos row no-gap
                    val rows = doc.getElementsByClass("row no-gap")

                    val classCount = rows.size / 3

                    // em cada elemento row no-gap
                    for (i in 1 .. classCount) {
                        // o segundo tem tres elementos col com hora, vagas e butão inscrever
                        val second = rows[3*i-2].getElementsByClass("col")

                        if (second[2].childrenSize() == 0) { // o botão inscrever ainda não existe
                            // o primeiro tem dois elementos col-50 com nome da aula e localização
                            val first = rows[3*i-3].getElementsByClass("col-50")

                            // o terceiro tem um elementos col-80 com informações temporais sobre a aula
                            //"Aula/treino a decorrer" ou "Prazo terminado" ou "Aula/treino concluído" ou remaining time until schedule ("Inscrições em") ou remaining time until class
                            val third = rows[3*i-1].getElementsByClass("col-80")

                            val detailsThird = third[0].select("iframe").first()!!.attr("src")
                            val classId = detailsThird.substringAfter("feed_id=").substringBefore('&')
                            val timeToSchedule = _timestamp.value + detailsThird.substringAfter("tempo=").substringBefore('&').toLong()

                            classes.add(GymClass(classId, first[0].ownText(), second[0].ownText(), timeToSchedule.toString()))
                        }
                    }

                    classes
                } catch (e: Exception) {
                    println(e.message)
                    emptyList()
                }
        }
    }

    // Esta função é chamada quando abrem as inscrições, realizando scraping da informação necessária para marcar a aula (id_rato, x)
    fun scheduleClass(selectedClass: GymClass) {
        viewModelScope.launch(Dispatchers.IO) {
            //selectedClass.scheduled = !selectedClass.scheduled
            //TODO add to database
            //TODO workManager
            try {
                val doc = Jsoup.parse(services.getClasses(_timestamp.value.toString().padEnd(13, '0')))
                // cada aula tem 3 elementos row no-gap
                val rows = doc.getElementsByClass("row no-gap")

                val classCount = rows.size / 3

                // em cada elemento row no-gap
                for (i in 1 .. classCount) {
                    // o terceiro tem um elementos col-80 com informações temporais sobre a aula
                    //"Aula/treino a decorrer" ou "Prazo terminado" ou "Aula/treino concluído" ou remaining time until schedule ("Inscrições em") ou remaining time until class
                    val third = rows[3*i-1].getElementsByClass("col-80")
                    val detailsThird = third[0].select("iframe").first()!!.attr("src")
                    val classId = detailsThird.substringAfter("feed_id=").substringBefore('&')

                    if (classId == selectedClass.classId) {
                        // o segundo tem tres elementos col com hora, vagas e butão inscrever
                        val second = rows[3*i-2].getElementsByClass("col")
                        val button = second[2].select("button").first()!!.attr("onClick")
                        val url = button.substringAfter("inscrever?','").substringBefore("')")
                        services.scheduleClass(url)
                        break
                    }
                }
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    fun cancelClass(selectedClass: GymClass) {
        viewModelScope.launch(Dispatchers.IO) {
            //selectedClass.scheduled = !selectedClass.scheduled
            //TODO remove from database
        }
    }

    fun increaseDay(){
        _timestamp.value += DAY
        getClasses()
    }

    fun decreaseDay(){
        _timestamp.value -= DAY
        getClasses()
    }
}
