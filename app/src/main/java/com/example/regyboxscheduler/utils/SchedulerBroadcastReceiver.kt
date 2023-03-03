package com.example.regyboxscheduler.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.*
import com.example.regyboxscheduler.DependenciesContainer
import org.jsoup.Jsoup

class SchedulerBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val repo = (context as DependenciesContainer)
        val timestamp = intent?.getStringExtra("TIMESTAMP") ?: return
        val scheduledClassId = intent.getStringExtra("ID") ?: return

        try {
            val doc = Jsoup.parse(repo.regyboxServices.getClasses(timestamp.padEnd(13, '0')))
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

                if (classId == scheduledClassId) {
                    // o segundo tem tres elementos col com hora, vagas e butão inscrever
                    val second = rows[3*i-2].getElementsByClass("col")
                    val button = second[2].select("button").first()!!.attr("onClick")
                    val url = button.substringAfter("inscrever?','").substringBefore("')")
                    repo.regyboxServices.scheduleClass(url)
                    repo.sharedPrefs.prefs.edit().remove(classId).apply()
                    break
                }
            }
        } catch (e: Exception) {
            println(e.message)
        }
    }
}