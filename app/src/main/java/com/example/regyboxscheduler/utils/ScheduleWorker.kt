package com.example.regyboxscheduler.utils

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.regyboxscheduler.DependenciesContainer
import org.jsoup.Jsoup

// Este worker é executado quando abrem as inscrições, realizando scraping da informação necessária para marcar a aula (id_rato, x)
class SchedulerWorker (appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params)
{
    override suspend fun doWork(): Result {
        val services = (applicationContext as DependenciesContainer).regyboxServices
        val timestamp = inputData.getString("TIMESTAMP")
        val scheduledClassId = inputData.getString("ID")

        return try {
            require(timestamp != null  && scheduledClassId != null)

            val doc = Jsoup.parse(services.getClasses(timestamp.padEnd(13, '0')))
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
                    services.scheduleClass(url)
                    break
                }
            }
            Result.success()
        }
        catch (e: Exception) {
            Result.failure()
        }
    }
}