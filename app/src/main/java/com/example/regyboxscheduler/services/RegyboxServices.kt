package com.example.regyboxscheduler.services

import com.example.regyboxscheduler.utils.SharedPrefs
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request

private const val HOST = "https://www.regibox.pt/app/app_nova/php"

class RegyboxServices (
    private val httpClient: OkHttpClient,
    sharedPrefs: SharedPrefs
) {

    private val clientWithCookie = httpClient.newBuilder()
        .addInterceptor { chain ->
            val originalRequest = chain.request()

            val builder = originalRequest.newBuilder()
                .header("Cookie", "regybox_boxes=${sharedPrefs.cookies!!.boxes}; regybox_user=${sharedPrefs.cookies!!.user}")

            val newRequest = builder.build()
            chain.proceed(newRequest)
        }.build()

    fun login (idBox: String, username: String, password: String): String? {
        val formBody = FormBody.Builder()
            .add("id_box", idBox)
            .add("login", username)
            .add("password", password)
            .build()

        val request = Request.Builder()
            .url("$HOST/login/scripts/verifica_acesso.php?lang=pt")
            .post(formBody)
            .build()

        httpClient.newCall(request).execute().use { response ->
            if (response.isSuccessful) {
                val responseString = response.body.string().split("\n")
                println(responseString)

                if (responseString.size == 1 || !responseString[1].startsWith("\t\twindow.open(\"../../../set_session.php?z="))
                    println("Invalid parameters!")
                else {
                    val cookie = responseString[1].substringAfter("window.open(\"../../../set_session.php?z=").substringBefore("\",\"_self\");")
                    println(cookie)
                    return cookie
                }
            }
            return null
        }
    }

    fun getAulas (timestamp: Long) {
        val request = Request.Builder()
            .url("$HOST/aulas/aulas.php?valor1=$timestamp")
            .build()

        clientWithCookie.newCall(request).execute().use { response ->
            println(response.body.string())
        }
    }

    fun marcarAula (idAula: String, data: String, ano: String, idRato: String, x: String) {
        val request = Request.Builder()
            .url("$HOST/aulas/marca_aulas.php?id_aula=$idAula&data=$data&source=mes&ano=$ano&id_rato=$idRato&x=$x")
            .build()

        clientWithCookie.newCall(request).execute().use { response ->
            println(response.body.string())
        }
    }

    fun cancelarAula (idAula: String, data: String, ano: String, x: String) {
        val request = Request.Builder()
            .url("$HOST/aulas/cancela_aula.php?id_aula=$idAula&data=$data&source=mes&ano=$ano&x=$x")
            .build()

        clientWithCookie.newCall(request).execute().use { response ->
            println(response.body.string())
        }
    }
}